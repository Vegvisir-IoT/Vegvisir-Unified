package com.vegvisir.application;

import com.isaacsheff.charlotte.proto.Block;
import com.vegvisir.VegvisirCore;
import com.vegvisir.core.blockdag.NewBlockListener;
import com.vegvisir.pub_sub.TransactionID;
import com.vegvisir.pub_sub.VegvisirApplicationContext;
import com.vegvisir.pub_sub.VegvisirApplicationDelegator;
import com.vegvisir.pub_sub.VegvisirInstance;

import java.util.Deque;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The ADD ALL BLOCKS reconciliation protocol instance.
 */
public class VegvisirInstanceV1 implements VegvisirInstance, NewBlockListener {

    /**
     * The block DAG instance.
     */
    private VegvisirCore core;

    /**
     * storing all incoming transactions.
     */
    private LinkedBlockingDeque<com.vegvisir.core.datatype.proto.Block.Transaction> transactionQueue;


    private ConcurrentHashMap<String, String> topic2app;

    private ConcurrentHashMap<String, VegvisirApplicationDelegator> app2handler;

    /**
     * The singleton instance.
     */
    private static VegvisirInstanceV1 instance;



    public static synchronized VegvisirInstance getInstance() {
        if (instance == null) {
            instance = new VegvisirInstanceV1();
        }
        return instance;
    }

    private VegvisirInstanceV1() {
        core = new VegvisirCore(new AndroidAdapter());
        core.registerNewBlockListener(this);
        transactionQueue = new LinkedBlockingDeque<>();
        topic2app = new ConcurrentHashMap<>();
        app2handler = new ConcurrentHashMap<>();
    }

    /**
     * Register a delegator, which will handle new transactions for that application.
     * After the registration, new transactions will be forward to the delegator at most once.
     * If there already is delegator for that application, then this one replaces the old one.
     * However, transactions that already been sent to the old delegator will be processed by the
     * old one.
     *
     * @param context   a context object of the application.
     * @param delegator a delegator instance.
     * @return true if the @delegator is successfully registered.
     */
    @Override
    public boolean registerApplicationDelegator(final VegvisirApplicationContext context, VegvisirApplicationDelegator delegator) {
        if (context.getTopics() == null || context.getAppId() == null || context.getTopics().isEmpty())
            return false;
        context.getTopics().forEach(t -> topic2app.put(t, context.getAppId()));
        app2handler.put(context.getAppId(), delegator);
        return true;
    }

    /**
     * Add a new transaction to the DAG. If the transaction is valid, then it will be added to the
     * block, either current one or next one depends on the transaction queue size. If the transaction
     * is valid, then this transaction will be pass to applyTransaction immediately to let application
     * update its states.
     *
     * @param context      a context object of the application.
     * @param topics       a set of pub/sub topic that unique identify who are interested in this transaction.
     * @param payload      a application defined data payload in byte array format.
     * @param dependencies a set of transactionIds that this transaction depends on.
     * @return true, if the transaction is valid.
     */
    @Override
    public boolean addTransaction(VegvisirApplicationContext context, Set<String> topics, byte[] payload, Set<TransactionID> dependencies) {
        return false;
    }

    /**
     * Called when a new block arrived.
     * Add all transaction in the block to the transaction queue.
     *
     * @param block a charlotte block.
     */
    @Override
    public void onNewBlock(Block block) {
        transactionQueue.addAll(block.getVegvisirBlock().getBlock().getTransactionsList());
    }
}
