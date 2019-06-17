package com.vegvisir.application;

import android.app.Application;
import android.content.Context;
import android.support.v4.util.Pair;

import com.isaacsheff.charlotte.proto.Block;
import com.vegvisir.VegvisirCore;
import com.vegvisir.core.blockdag.NewBlockListener;
import com.vegvisir.core.config.Config;
import com.vegvisir.core.reconciliation.ReconciliationV1;
import com.vegvisir.pub_sub.TransactionID;
import com.vegvisir.pub_sub.VegvisirApplicationContext;
import com.vegvisir.pub_sub.VegvisirApplicationDelegator;
import com.vegvisir.pub_sub.VegvisirInstance;
import com.vegvisir.core.datatype.proto.Block.Transaction;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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


    private ConcurrentHashMap<String, Set<String>> topic2app;

    private ConcurrentHashMap<String, VegvisirApplicationDelegator> app2handler;

    /**
     * The singleton instance.
     */
    private static VegvisirInstanceV1 instance;



    public static synchronized VegvisirInstance getInstance(Context applicationContext) {
        if (instance == null) {
            instance = new VegvisirInstanceV1(applicationContext);
        }
        return instance;
    }

    private VegvisirInstanceV1(Context ctx) {
        KeyPair keyPair = Config.generateKeypair();
        String deviceName = Config.pk2str(keyPair.getPublic());
        core = new VegvisirCore(new AndroidAdapter(ctx, deviceName),
                ReconciliationV1.class,
                createGenesisBlock(keyPair),
                keyPair,
                deviceName
        );
        core.registerNewBlockListener(this);
        transactionQueue = new LinkedBlockingDeque<>();
        topic2app = new ConcurrentHashMap<>();
        app2handler = new ConcurrentHashMap<>();
        new Thread(this::pollTransactions).start();
    }


    /**
     * A thread runs forever for polling and applying new transactions.
     */
    private void pollTransactions() {
        while (true) {
            try{
                Transaction tx = transactionQueue.take();

                Set<TransactionID> deps = new HashSet<>();
                for (Transaction.TransactionId id : tx.getDependenciesList()) {
                    deps.add(new TransactionID(id.getDeviceId(), id.getTransactionHeight()));
                }
                List<String> topics = tx.getTopicsList();

                /* TODO: May want run this in separate threads to avoid being blocked by application code */
                getAppHandlers(new HashSet<>(topics)).forEach( (handler, _topics) -> {
                    handler.applyTransaction(
                            _topics,
                            tx.getPayload().toByteArray(),
                            new TransactionID(tx.getTransactionId().getDeviceId(), tx.getTransactionId().getTransactionHeight()),
                            deps);

                });
            } catch (InterruptedException ex) {
                System.err.println("Interrupted transaction polling thread! Will exit.");
                break;
            }
        }
    }


    /**
     * A helper method for gathering transaction handlers for a transaction with given topics.
     * The method returns a map mapping from handler to a set of topics that appear in both the
     * set of topics that the application is listening on and the set of transaction that
     * the transaction contains.
     * listening on.
     * @param topics a set of topics in the given transaction.
     * @return
     */
    private Map<VegvisirApplicationDelegator, Set<String>> getAppHandlers(Set<String> topics) {
        Map<VegvisirApplicationDelegator, Set<String>> delegatorTopics = new HashMap<>();
        for (String topic : topics) {
            topic2app.get(topic).forEach(app -> {
                VegvisirApplicationDelegator delegator = app2handler.get(app);
                if (!delegatorTopics.containsKey(delegator)) {
                    delegatorTopics.put(delegator, new HashSet<>());
                }
                delegatorTopics.get(delegator).add(topic);
            });
        }
        return delegatorTopics;
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
        if (context.getChannels() == null || context.getAppID() == null || context.getChannels().isEmpty())
            return false;
        context.getChannels().forEach(t -> {
            if (!topic2app.containsKey(t)) {
                topic2app.putIfAbsent(t, new HashSet<>());
            }
            topic2app.get(t).add(context.getAppID());
        });
        app2handler.put(context.getAppID(), delegator);
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
        List<com.vegvisir.core.datatype.proto.Block.Transaction.TransactionId> deps = new ArrayList<>();
        for (TransactionID id : dependencies) {
            deps.add(com.vegvisir.core.datatype.proto.Block.Transaction.TransactionId.newBuilder().setTransactionHeight(id.getTransactionHeight()).setDeviceId(id.getDeviceID()).build());
        }
        core.createTransaction(deps, topics, payload);
        return true;
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

    /**
     * Created a genesis block signed by given keypair.
     * @param keyPair a key pair used to sign the block.
     * @return a genesis block with empty content.
     */
    private Block createGenesisBlock(KeyPair keyPair) {
        com.vegvisir.core.datatype.proto.Block.GenesisBlock genesis =
                                com.vegvisir.core.datatype.proto.Block.GenesisBlock.newBuilder() .build();
        return Block.newBuilder().setVegvisirBlock(
                com.vegvisir.core.datatype.proto.Block.newBuilder()
                .setGenesisBlock(genesis)
                .setSignature(Config.signProtoObject(keyPair, genesis))
                .build()

        ).build();
    }

}
