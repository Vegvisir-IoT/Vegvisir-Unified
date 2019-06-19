package com.vegvisir.core.blockdag;

import com.isaacsheff.charlotte.proto.Block;
import com.isaacsheff.charlotte.proto.Reference;
import com.vegvisir.core.config.Config;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BlockDAGv1 extends BlockDAG {

    private Set<Reference> frontierReference;

    public BlockDAGv1(Block genesisBlock) {
        super(genesisBlock, null);
    }

    public BlockDAGv1(Block genesisBlock, Config config) {
        super(genesisBlock, config);
        frontierReference = new HashSet<>();
    }

    public BlockDAGv1() {
        this(null);
    }

    /**
     * Verify all transactions and signature for the block. If all checks are passed, then append this block to the block dag.
     * TODO: check block transactions
     * @param block
     * @return
     */
    public boolean verifyBlock(Block block) {
        if (block.hasVegvisirBlock()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Get a particular block by its reference.
     * @param ref
     * @return
     */
    public Block getBlock(Reference ref) {
        return blockStorage.get(ref);
    }


    /**
     * Helper method for version 0.1
     * @param blocks a collection of blocks to be append to the block dag.
     */
    public synchronized void addAllBlocks(Iterable<Block> blocks) {
        blocks.forEach(b -> {
            putBlock(b);
            frontierReference.removeAll(b.getVegvisirBlock().getBlock().getParentsList());
            frontierReference.add(BlockUtil.byRef(b));
        });
    }


    /**
     * For version 0.1, we want this method help us to get all blocks.
     * @return all blocks on this node.
     */
    public Collection<Block> getAllBlocks() {
        return blockStorage.values();
    }

    @Override
    public void createBlock(Iterable<com.vegvisir.core.datatype.proto.Block.Transaction> transactions,
                            Iterable<Reference> parents) {
        com.vegvisir.core.datatype.proto.Block.UserBlock content = com.vegvisir.core.datatype.proto.Block.UserBlock.newBuilder().addAllParents(parents)
                .setUserid(getConfig().getNodeId())
                .setCryptoID(getConfig().getCryptoId())
                .setTimestamp(com.vegvisir.common.datatype.proto.Timestamp.newBuilder().setUtcTime(new Date().getTime()).build())
                .addAllTransactions(transactions)
                .build();
        com.isaacsheff.charlotte.proto.Block block = com.isaacsheff.charlotte.proto.Block.newBuilder()
                .setVegvisirBlock(
                        com.vegvisir.core.datatype.proto.Block.newBuilder().setBlock(content)
                                .setSignature(getConfig().signProtoObject(content))
                                .build()
                ).build();
        addAllBlocks(Collections.singleton(block));
    }

    @Override
    public Set<Reference> getFrontierBlocks() {
        return frontierReference;
    }
}
