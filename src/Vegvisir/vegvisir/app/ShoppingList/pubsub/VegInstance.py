from .Context import Context
from vegvisir.blockchain.block import TransactionId, Transaction
from datetime import datetime
from queue import Queue

class VegInstance():

    deviceID = 'mac' #The id of current device 
    device2txnhgt = dict()
    height = 1 #The height of current device 
    txQueue =  Queue(10)

    def addTransaction(self, context : Context, topics : set, payload : bytes, dependencies : set, userid : str) -> bool:
        
        if not device2txnhgt.contains(self.deviceID):
            device2txnhgt.put(self.deviceID, height)

        return _addTransaction(self.deviceID, device2txnhgt.get(self.deviceID), topics, payload, dependencies, userid)

    '''
     * Append a transaction to the transaction queue with given device id and height, then increase
     * the transaction height of @deviceId.
     * @param deviceId      a identifier for a device.
     * @param height        a natural number identifying the number of transaction has been created by that
     *                      device.
     * @param topics         a pub/sub topic that unique identify who are interested in this transaction.
     * @param payload       a application defined data payload in byte array format.
     * @param dependencies  a list of transactionIds that this transaction depends on.
     * @return true, if the transaction is valid.
     '''
    def _addTransaction(self, deviceID : str, height : int, topics : set, payload : bytes, dependencies : set, userid : str):
        
        deps = list()
        for tid in dependencies:
            deps += [ TransactionId(tid.tx_height, tid.device_id) ]
        
        txdict = {'comment' : payload, 'recordid' : None}
        txnID = TransactionId(self.deviceID, self.height)
        txn = Transaction(userid, datetime.now(), txdict, txnID, deps)

        nxtheight = self.height + 1
        self.device2txnhgt.put(self.deviceID, nxtheight)
        txQueue.add(txn)

        return True
