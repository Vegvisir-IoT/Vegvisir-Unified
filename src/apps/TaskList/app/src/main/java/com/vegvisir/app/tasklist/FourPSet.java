package com.vegvisir.app.tasklist;

import java.util.HashSet;
import java.util.Set;

public class FourPSet {
    private Set<String> lowSet;
    private Set<String> mediumSet;
    private Set<String> highSet;
    private Set<String> removeSet;

    public FourPSet() {
        lowSet = new HashSet<>();
        mediumSet = new HashSet<>();
        highSet = new HashSet<>();
        removeSet = new HashSet<>();
    }

    public FourPSet(Set<String> low, Set<String> medium, Set<String> high, Set<String> remove) {
        lowSet = low;
        mediumSet = medium;
        highSet = high;
        removeSet = remove;
    }

    public Set<String> getLowSet(){
        return this.lowSet;
    }

    public Set<String> getMediumSet(){
        return this.mediumSet;
    }

    public Set<String> getHighSet(){
        return this.highSet;
    }

    public Set<String> getRemoveSet(){
        return this.removeSet;
    }

    public String toString(){
        return "lowset:" + lowSet.toString() + ", mediumset:"+ mediumSet.toString() + ", highset:"+ highSet.toString() + ", removeset:"+ removeSet.toString();
    }
}
