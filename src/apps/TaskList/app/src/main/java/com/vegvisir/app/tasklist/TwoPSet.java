package com.vegvisir.app.tasklist;

import com.vegvisir.pub_sub.TransactionID;

import java.util.HashSet;
import java.util.Set;

public class TwoPSet {
    private Set<String> addSet;
    private Set<String> removeSet;

    public TwoPSet() {
        addSet = new HashSet<>();
        removeSet = new HashSet<>();
    }

    public TwoPSet(Set<String> add, Set<String> remove) {
       addSet = add;
       removeSet = remove;
    }

    public Set<String> getAddSet(){
        return this.addSet;
    }

    public Set<String> getRemoveSet(){
        return this.removeSet;
    }

    public String toString(){
        return "addset:" + addSet.toString() +", removeset:"+ removeSet.toString();
    }
}
