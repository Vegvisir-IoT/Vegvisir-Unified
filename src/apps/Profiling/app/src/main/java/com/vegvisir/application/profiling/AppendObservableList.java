package com.vegvisir.application.profiling;

import java.util.ArrayList;
import java.util.function.Consumer;

public class AppendObservableList<E> {

    private ArrayList<E> list;
    private Consumer<E> appendEventListener;

    public AppendObservableList(){
        list = new ArrayList<E>();
    }

    public void setAppendEventListener(Consumer<E> listener) {
        this.appendEventListener = listener;
    }

    public boolean append(E o) {
        boolean ret = list.add(o);
        if (ret) {
            if (appendEventListener != null)
                appendEventListener.accept(o);
        }
        return ret;
    }

    public ArrayList<E> getList() {
        return list;
    }

    public void clear() {
        list.clear();
    }
}
