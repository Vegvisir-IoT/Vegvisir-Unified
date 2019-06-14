package com.vegvisir.core.reconciliation;

import com.vegvisir.core.blockdag.BlockDAG;
import com.vegvisir.core.blockdag.BlockDAGv2;
import com.vegvisir.core.datatype.proto.Block;
import com.vegvisir.common.datatype.proto.ControlSignal;
import com.vegvisir.core.blockdag.BlockDAG;
import com.vegvisir.network.datatype.proto.Payload;
import com.vegvisir.network.datatype.proto.VegvisirProtocolMessage;

public class ReconciliationV2 extends ReconciliationV1 {

    protected Block.VectorClock remoteVector;

    boolean connEnded = false;

    /**
     * This is a pull based reconciliation algorithm.
     * @param myDAG
     * @param remoteConnectionID
     */
    @Override
    public void exchangeBlocks(BlockDAG myDAG, String remoteConnectionID) {
        this.dag = myDAG;
        this.remoteId = remoteConnectionID;
        dispatchThread = gossipLayer.setHandlerForPeerMessage(remoteId, this::dispatcherHandler);
        /*
         * Send protocol version to the remote side and figure out a version that both can understand.
         * The final version should be the highest one that both can understand.
         */
        sendVersion();

        if (this.runningVersion.compareTo(this.getVersion()) < 0) {
            /*
             * If current version is higher than running version, then we let parent class handle this.
             * This will eventually be handled because all nodes should be able to run version 1.
             */
            super.exchangeBlocks(myDAG, remoteConnectionID);
            return;
        }

        this.dag = myDAG;
        this.remoteId = remoteConnectionID;


        /*
         * Compute frontier set. Now this is a vector clock.
         */
        Block.VectorClock clock = myDAG.computeFrontierSet();

        exchangeVectorClock(clock);

        /* Wait for remote vector clock */
        try {
            lock.wait();
        } catch (InterruptedException ex) {
            return;
        }

        /* Figure out dependencies */
        if (remoteVector == null) {
            /*TODO: Set error message, remote vector unknown */
            return;
        }
        Iterable<com.isaacsheff.charlotte.proto.Block> blocks =
                dag.findMissedBlocksByVectorClock(remoteVector);

        /* Send blocks */
        blocks.forEach(this::sendBlock);
        synchronized (lock) {
            if (!connEnded) {
                try {
                    lock.wait();
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }

        if (connEnded) {
            /* If connection ended by remote peer */
            dispatchThread.interrupt();
            gossipLayer.disconnect(this.remoteId);
            dag.addLeadingBlock();
        }
    }


    /**
     * Send this device's vector clock to the remote peer device.
     * @param clock
     */
    protected void exchangeVectorClock(Block.VectorClock clock) {

        VegvisirProtocolMessage message = VegvisirProtocolMessage.newBuilder()
                .setCmd(ControlSignal.VECTOR_CLOCK)
                .addBlocks(com.isaacsheff.charlotte.proto.Block.newBuilder()
                .setVegvisirBlock(Block.newBuilder().setVectorClock(clock).build()).build())
                .build();
        Payload payload = Payload.newBuilder().setMessage(message).build();
        this.gossipLayer.sendToPeer(this.remoteId, payload);
    }


    /**
     * Send block @block to remote device.
     * @param block
     */
    protected void sendBlock(com.isaacsheff.charlotte.proto.Block block) {
        VegvisirProtocolMessage message = VegvisirProtocolMessage.newBuilder()
                .addBlocks(block)
                .setCmd(block == null ? ControlSignal.END : ControlSignal.ADD_BLOCKS)
                .setVersion(this.runningVersion.toProtoVersion())
                .build();
        Payload payload = Payload.newBuilder()
                .setMessage(message)
                .build();
        this.gossipLayer.sendToPeer(remoteId, payload);
    }


    @Override
    protected void dispatcherHandler(Payload payload) {
        /* Assume both ends using vector clock protocol */
        if (!payload.hasMessage()) {
            return;
        }

        com.vegvisir.common.datatype.proto.ProtocolVersion remoteV = payload.getMessage().getVersion();
        Version remoteVersion = new Version(remoteV.getMajor(), remoteV.getMinor(), remoteV.getPatch());

        switch (payload.getMessage().getCmd()) {
            case VERSION:
                synchronized (lock) {
                    this.runningVersion = checkVersion(remoteVersion);
                    lock.notifyAll();
                }
                break;

            case ADD_BLOCKS:
                synchronized (lock) {
                    if (this.runningVersion == null) {
                        try {
                            lock.wait();
                        } catch (InterruptedException ex) {

                        }
                    }
                }
                if(remoteVersion.compareTo(this.runningVersion) != 0)
                {
                    /* all operations other than sync version will be run with the same version between two nodes. This is because the first step for running reconciliation is syncing up versions */
                    return;
                }
                handleAddBlocks(payload.getMessage().getBlocksList());

            case VECTOR_CLOCK:
                remoteVector = payload.getMessage().getBlocks(0).getVegvisirBlock().getVectorClock();
                break;

            case END:
                synchronized (lock) {
                    if (!connEnded)
                        connEnded = true;
                    else
                        lock.notifyAll();
                }

            case UNRECOGNIZED:
        }
    }

    @Override
    protected void handleAddBlocks(Iterable<com.isaacsheff.charlotte.proto.Block> blocks) {
        dag.addAllBlocks(blocks);
    }


}
