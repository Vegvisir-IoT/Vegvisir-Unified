package com.vegvisir.core.reconciliation;

import com.isaacsheff.charlotte.proto.Block;
import com.vegvisir.common.datatype.proto.AddBlocks;
import com.vegvisir.core.blockdag.BlockDAGv2;
import com.vegvisir.network.datatype.proto.VegvisirProtocolMessage;
import com.vegvisir.util.profiling.VegvisirStatsCollector;

import java.util.List;

import vegvisir.proto.Handshake;
import vegvisir.proto.Vector;

public class VectorClockProtocol implements ReconciliationProtocol {


    private static final int TIMEOUT = 3000;

    private Vector.VectorClock remoteVector;

    private String remoteId;


    private ProtocolConfig config;

    BlockDAGv2 dag;

    private final Object lock = new Object();

    VectorClockProtocol(ProtocolConfig config) {
        this.config = config;
        dag = config.getDag();
    }

    @Override
    public void startReconciliation() throws InterruptedException {



        this.remoteId = config.getRemoteId();


        /*
         * Compute frontier set. Now this is a vector clock.
         */
        Vector.VectorClock clock = dag.computeFrontierSet();

        sendVectorClock(clock);

        synchronized (lock) {
            lock.wait(TIMEOUT);
        }

        /* Wait for remote vector clock */

//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//             currentThread.interrupt();
//            }
//        }, TIMEOUT);
//
//        try {
//            if (remoteVector == null) {
//                synchronized (lock) {
//                    if (remoteVector == null) {
//                        lock.wait();
//                        timer.cancel();
//                    }
//                }
//            }
//        } catch (InterruptedException ex) {
//            reconciliationEndCleaner();
//            return;
//        }
//
//        /* Figure out dependencies */
//        if (remoteVector == null) {
//            /*TODO: Set error message, remote vector unknown */
//            return;
//        }
//        dag.updateVCForDevice(remoteConnectionID, remoteVector);
//        Iterable<com.isaacsheff.charlotte.proto.Block> blocks =
//                dag.findMissedBlocksByVectorClock(remoteVector);
//
//        /* Send blocks */
//        blocks.forEach(this::sendBlock);
//
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                currentThread.interrupt();
//            }
//        }, TIMEOUT);
//
//        synchronized (lock) {
//            if (!connEnded) {
//                try {
//                    lock.wait();
//                } catch (InterruptedException ex) {
//                }
//            }
//        }
//        reconciliationEndCleaner();
    }
//
//    private void reconciliationEndCleaner() {
//        connEnded = true;
//        dispatchThread.interrupt();
//        gossipLayer.disconnect(this.remoteId);
////        if (connEnded) {
//            /* If connection ended by remote peer */
////            dag.addLeadingBlock();
////        }
//    }


    /**
     * Send this device's vector clock to the remote peer device.
     * @param clock
     */
    protected void sendVectorClock(Vector.VectorClock clock) {

        VegvisirProtocolMessage message = VegvisirProtocolMessage.newBuilder()
                .setMessageType(VegvisirProtocolMessage.MessageType.VECTOR_CLOCK)
                .setVector(Vector.VectorMessage.newBuilder()
                        .setType(Vector.VectorMessage.MessageType.LOCAL_VECTOR_CLOCK)
                        .setLocalView(clock)
                        .build())
                .build();
        System.err.println("MINE:\n" + clock.getBody().getClocksMap());
        config.send(message);
    }

    private void computeSendBlocks(Vector.VectorClock remoteVector) {
        System.err.println("REMOTE:\n" + remoteVector.getBody().getClocksMap());
        dag.updateVCForDevice(config.getRemoteId(), remoteVector);
        List<Block> blocks =
                dag.findMissedBlocksByVectorClock(remoteVector);

        /* Send blocks */

//        VegvisirProtocolMessage message = VegvisirProtocolMessage
//                .newBuilder().setMessageType(VegvisirProtocolMessage.MessageType.VECTOR_CLOCK)
//                .setVector(
//                        Vector.VectorMessage.newBuilder()
//                                .setAdd(
//                                        com.vegvisir.common.datatype.proto.AddBlocks.newBuilder()
//                                                .addAllBlocksToAdd(blocks)
//                                                .build()
//                                )
//                                .setType(Vector.VectorMessage.MessageType.BLOCKS)
//                                .build()
//                )
//                .build();
//        config.send(message);
        int rest2Sent = blocks.size();
        for (Block b : blocks) {
            VegvisirProtocolMessage message = VegvisirProtocolMessage
                    .newBuilder().setMessageType(VegvisirProtocolMessage.MessageType.VECTOR_CLOCK)
                    .setVector(
                            Vector.VectorMessage.newBuilder()
                                    .setAdd(
                                            AddBlocks.newBuilder()
                                                    .addBlocksToAdd(b)
                                                    .build()
                                    )
                                    .setSendLimit(--rest2Sent)
                                    .setType(Vector.VectorMessage.MessageType.BLOCKS)
                                    .build()
                    )
                    .build();
            config.send(message);
        }
    }


    @Override
    public void onNewMessage(VegvisirProtocolMessage message) {

    /* Assume both ends using vector clock protocol */


        switch (message.getVector().getType()) {

            case BLOCKS:
                handleAddBlocks(message.getVector().getAdd().getBlocksToAddList());
//                VegvisirStatsCollector.getInstance().logNewBlockCount(message.getVector().getAdd().getBlocksToAddList().size());
                if (message.getVector().getSendLimit() == 0) {
                    config.endProtocol();
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                }
                break;
            case LOCAL_VECTOR_CLOCK:
                computeSendBlocks(message.getVector().getLocalView());
                break;
            case ALL_VECTOR_CLOCKS:
                break;
            case UNRECOGNIZED:
        }
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public Handshake.ProtocolVersion getVersion() {
        return Handshake.ProtocolVersion.VECTOR;
    }

    protected void handleAddBlocks(Iterable<com.isaacsheff.charlotte.proto.Block> blocks) {
        dag.addAllBlocks(blocks);
    }
}
