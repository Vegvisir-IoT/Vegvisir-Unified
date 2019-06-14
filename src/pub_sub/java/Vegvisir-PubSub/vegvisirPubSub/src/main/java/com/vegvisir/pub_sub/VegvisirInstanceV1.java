package com.vegvisir.pub_sub;

import com.vegvisir.VegvisirCore;
import com.vegvisir.gossip.adapter.NetworkAdapter;

import java.util.Set;



public class VegvisirInstanceV1 implements VegvisirInstance {

    private VegvisirCore core;

    public VegvisirInstanceV1(NetworkAdapter adapter) {
        core = new VegvisirCore(adapter);
    }

    @Override
    public boolean addTransaction(VegvisirApplicationContext context, Set<String> topics, byte[] payload, Set<TransactionID> dependencies) {
        return false;
    }

    @Override
    public boolean registerApplicationDelegator(VegvisirApplicationContext context, VegvisirApplicationDelegator delegator) {
        return false;
    }
}
