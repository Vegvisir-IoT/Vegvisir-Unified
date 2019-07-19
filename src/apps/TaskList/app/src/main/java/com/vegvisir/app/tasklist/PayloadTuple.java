package com.vegvisir.app.tasklist;


public class PayloadTuple {
    public String item;
    public int transactionType; //0 is remove, 1 is add

    public PayloadTuple(String itm, int tx_type) {
        item = itm;
        transactionType = tx_type;
    }
}
