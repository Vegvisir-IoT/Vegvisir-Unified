package com.vegvisir.app.annotativemap;

import android.os.CpuUsageInfo;

import java.util.HashSet;
import java.util.Set;

public class TwoPSet {
    private Set<Coordinates> addSet;
    private Set<Coordinates> removeSet;

    public TwoPSet() {
        addSet = new HashSet<>();
        removeSet = new HashSet<>();
    }

    public TwoPSet(Set<Coordinates> add, Set<Coordinates> remove) {
        addSet = add;
        removeSet = remove;
    }

    public Set<Coordinates> getAddSet(){
        return this.addSet;
    }

    public Set<Coordinates> getRemoveSet(){
        return this.removeSet;
    }
}
