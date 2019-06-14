package com.vegvisir.core.blockdag;

import com.isaacsheff.charlotte.proto.Block;
import com.isaacsheff.charlotte.proto.Reference;
import com.isaacsheff.charlotte.proto.CryptoId;
import com.vegvisir.core.config.Config;
import com.vegvisir.core.datatype.proto.Block.VectorClock;

import java.sql.Ref;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockDAGv2 extends BlockDAG {


    /**
     * A hash map mapping cryptoId -> blockchain.
     */
    private HashMap<String, Blockchain> blockchains;

    private final Set<Reference> leadingSet;

    public BlockDAGv2() {
        super();
        blockchains = new HashMap<>();
        leadingSet = new HashSet<>();
    }

    /**
     * Verify all transactions and signature for the block. If all checks are passed, then append this block to the block dag.
     * TODO: check block transactions
     *
     * @param block
     * @return
     */
    @Override
    public boolean verifyBlock(Block block) {
        for (Reference r :  block.getVegvisirBlock().getBlock().getParentsList()) {
            if(!this.blockStorage.containsKey(r))
                return false;
        }
        return Config.checkSignature(block.getVegvisirBlock().getBlock().toByteArray(),
                block.getVegvisirBlock().getSignature());
    }


    /**
     * Add blocks to the dag. This will delegate to each blockchain to add blocks.
     * @param blocks a set of blocks to be appended.
     */
    public void addBlocks(Iterable<Block> blocks) {
        com.isaacsheff.charlotte.proto.CryptoId blockId;
        Reference blockRef;
        for (Block block : blocks) {
            if (!verifyBlock(block))
                return;
            blockId = block.getVegvisirBlock().getBlock().getCryptoID();
            if (!blockchains.containsKey(BlockUtil.cryptoId2Str(blockId))) {
                if (validatePeer(blockId)) {
                    addNewChain(blockId);
                } else {
                    return;
                }
            }
            synchronized (leadingSet) {
                blockRef = blockchains.get(BlockUtil.cryptoId2Str(block.getVegvisirBlock().getBlock().getCryptoID())).appendBlock(block);
                leadingSet.removeAll(block.getVegvisirBlock().getBlock().getParentsList());
                leadingSet.add(blockRef);
            }
        }
    }


//    /**
//     * Append blocks to chain with @cryptoId.
//     * @param blocks a set of blocks to be appended.
//     * @param cryptoId the id of the chain.
//     */
//    public void addBlocks(Iterable<Block> blocks, com.isaacsheff.charlotte.proto.CryptoId cryptoId) {
//        if (!blockchains.containsKey(cryptoId)) {
//            if (!validatePeer(cryptoId))
//                return;
//            addNewChain(cryptoId);
//        }
//        blockchains.get(cryptoId).appendBlocks(blocks);
//    }


    /**
     * Add all bocks in @blocks to the dag. This is a alias of addBlocks in V2.
     * @param blocks a collection of blocks to be append to the block dag.
     */
    @Override
    public void addAllBlocks(Iterable<Block> blocks) {
        addBlocks(blocks);
    }


    /**
     * Put a new chain to the blockchain map.
     * @param id the id of the node. This is also the key to be used.
     */
    protected synchronized void addNewChain(com.isaacsheff.charlotte.proto.CryptoId id) {
        if (!blockchains.containsKey(BlockUtil.cryptoId2Str(id)))
            blockchains.put(BlockUtil.cryptoId2Str(id), new BlockchainV1(this,  id));
    }


    /**
     * Check whether the given id is in the peer set. This call will delegate to a CRDT 2P set to
     * validate the given @id.
     * @param id the crypto id of peer node.
     * @return true if this is a valid peer, i.e. @id in the set of valid peers.
     */
    protected boolean validatePeer(com.isaacsheff.charlotte.proto.CryptoId id) {
        return true;
    }


    /**
     * A frontier set of each chain can be represented by a vector clock. Therefore, we just need
     * to return the vector clock of the last block of the blockchain for this node.
     * @return vector clock represented the frontier blocks of each blockchain.
     */
    @Override
    public VectorClock computeFrontierSet() {
        return blockchains.get(BlockUtil.cryptoId2Str(this.config.getCryptoId())).getLastVectorClock();
    }


    /**
     * Return a list of blocks with the correct dependencies order that should be sent to the remote
     * device.
     * @param remoteVC the vector clock from remote device.
     * @return a list of blocks to be sent.
     */
    public Iterable<Block> findMissedBlocksByVectorClock(VectorClock remoteVC) {
        /* finding the last common frontier set */
        Set<Reference> commonFrontierSet = new HashSet<>();
        VectorClock myClock = computeFrontierSet();
        for (Map.Entry<String, VectorClock.Value> entry: myClock.getValuesMap().entrySet()) {
            VectorClock.Value value = remoteVC.getValuesMap().get(entry.getKey());
            commonFrontierSet.add(
                    blockchains.get(entry.getKey())
                            .getBlockList()
                            .get(Math.min(value.getIndex(), entry.getValue().getIndex()))
            );
        }
        Blockchain thisChain = blockchains.get(BlockUtil.cryptoId2Str(this.config.getCryptoId()));
        Reference leadingBlock = thisChain.getBlockList().get(thisChain.getBlockList().size()-1);
        return _findMissedBlocks(commonFrontierSet, leadingBlock);
    }


    /**
     * A BFS implementation that finds all blocks appended after the @commonFrontierSet with @leader
     * as leading block. The dependencies partial order is preserved in the returned list of blocks.
     * The leading block is the first element in the return list. If a block A -> block B("<" means
     * happens before), then, indexOf(Block A) > indexOf(Block B).
     *
     * @param commonFrontierSet
     * @param leader
     * @return a list of blocks preserving the partial order of blocks based on their dependencies.
     */
    private List<Block> _findMissedBlocks(Set<Reference> commonFrontierSet, Reference leader) {
        Deque<Reference> references = new ArrayDeque<>();
        Set<Reference> dupRefs = new HashSet<>();
        List<Block> blocks = new ArrayList<>();
        references.add(leader);
        while (!references.isEmpty()) {
            Reference next = references.poll();
            if (!dupRefs.add(next) || commonFrontierSet.contains(next))
                continue;
            Block nextBlock = this.getBlock(next);
            references.addAll(nextBlock.getVegvisirBlock().getBlock().getParentsList());
            blocks.add(nextBlock);
        }
        Collections.reverse(blocks);
        return blocks;
    }


    @Override
    public void addLeadingBlock() {
        createBlock(BlockUtil.cryptoId2Str(config.getCryptoId()), Collections.emptyList(), Collections.emptyList());
    }

    protected Blockchain getMyChain() {
        return blockchains.get(BlockUtil.cryptoId2Str(config.getCryptoId()));
    }

    @Override
    public Set<Reference> getLeadingBlocks() {
        return leadingSet;
    }

    @Override
    public Reference createBlock(String cryptoID,
                                 Iterable<com.vegvisir.core.datatype.proto.Block.Transaction> transactions,
                                 Iterable<Reference> parents) {
        Reference ref;
        synchronized (leadingSet) {
            Set<Reference> _parents = new HashSet<>();
            parents.forEach(_parents::add);
            _parents.addAll(leadingSet);
            ref = blockchains.get(cryptoID).createBlock(transactions, _parents);
            leadingSet.clear();
            leadingSet.add(ref);
        }
        return  ref;
    }
}
