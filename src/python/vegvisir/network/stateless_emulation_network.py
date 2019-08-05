import vegvisir.protos.vegvisirprotocol_pb2 as vgp
from vegvisir.simulator.opcodes import Operation
from vegvisir.blockchain.block import Block, Transaction
from vegvisir.emulator.socket_opcodes import (MessageTypes as msgtype,
                                             ProtocolStatus as ps,
                                             CommunicationStatus as comstatus)
from vegvisir.network.abstract_network import Network

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

# @brief: A class that handles emulation network operations.
class EmulationNetworkOperator(Network):
    
    """
       :param server: A VegvisirServer object.
       :param client: A VegvisirClient object.
    """
    def __init__(self, server, client):
        Network.__init__(self, userid=server.userid)
        self.server = server
        self.client = client


    def receive(self, sender=None):
        """
           Receive and deserialize a payload from the wire.
           :param sender: A Socket object.
           :param userid: A string.
        """
        payload = vgp.VegvisirMessage()
        if sender:
            message_len, start = self.server.read_from_buffer(sender)
            if message_len == None: # Zero bytes received for some of the msg
                print("Receipt on %s Server failed!, 0 bytes received\n" % self.userid)
                return None, None
            elif type(message_len) != int: # Socket error
                print("%s Server, Receipt failed with socket error -> %s\n" % 
                      (self.userid, message_len.__str__()))
                return None, None
            payload.ParseFromString(self.server.buffer[start:start + message_len])
            print("SUCCESSFUL PARSING ON %s SERVER\n" % self.userid)
            self.server.buffer = bytearray()
            print("For Server: MESSAGE SIZE %s\n" % message_len)
            payload, payload_type = self.sort_message(payload)
            if payload_type == msgtype.RESPONSE:
                return self.sort_response(payload)
            return payload, payload_type
        else:
            message_len, start = self.client.read_from_buffer()
            if message_len == None: # Zero bytes received for some of the msg
                print("Receipt on %s Client failed!, 0 bytes received\n" % self.userid)
                return None, None
            elif type(message_len) != int: # Socket error
                print("%s Client, Receipt failed with socket error -> %s\n" % 
                      (self.userid, message_len.__str__()))
                return None, None
            payload.ParseFromString(self.client.buffer[start:start + message_len])
            print("SUCCESSFUL PARSING ON CLIENT\n")
            self.client.buffer = bytearray()
            print("For Client: MESSAGE SIZE %s\n" % message_len)
            payload, payload_type = self.sort_message(payload)
            if payload_type == msgtype.RESPONSE:
                return self.sort_response(payload)
            return payload, payload_type
    
    def sort_message(self, message):
        """
            Sort the message received from remote peer.
            :param message: A vgp.VegvisirMessage.
        """
        message_type = message.WhichOneof("message_types")    
    
        if message_type == "request":
            payload = vgp.PeerRequest()
            payload.CopyFrom(message.request)
            return payload, msgtype.REQUEST
        elif message_type == "response":
            payload = vgp.PeerResponse()
            payload.CopyFrom(message.response)
            return payload, msgtype.RESPONSE
        elif message_type == "update":
            update = vgp.Update()
            update.CopyFrom(message.update)
            return update, msgtype.UPDATE
    

    def sort_response(self, response):
        """
             Sort specific response from peer based on stage of protocol.
             :param response: A vegvisir.protocol.datatype.Response message.
         """
         # Check what is set inside the response
        response_type = response.WhichOneof("response_types")
        if response_type == "hashResponse":
            print("SORTING FSET RESPONSE\n")
            fset = vgp.HashSet()
            fset.CopyFrom(response.hashResponse)
            frontier_set = []
            frontier_set.extend(list(fset.hashes))
            return frontier_set
        elif response_type == "blockResponse":
            print("SORTING SEND BLOCK RESPONSE\n")
            block_message = vegvisir.common.datatype.AddBlocks()
            block_message.CopyFrom(response.blockResponse)
            for pointer in block_message.blockToAdd:
                vegblock = vegvisir.core.datatype.Block()
                vegblock.ParseFromString(pointer.block)
                userid = vegblock.user_block.userid
                timestamp = vegblock.user_block.timestamp.utc_time
         
                # Worry about location later
    
                # Add parent list.
                parent_list = []
                for parent_hash in vegblock.user_block.parents:
                    parent_list.append(parent_hash.hash.sha256)
    
                # Add transaction list.
                tx_list = []
                for transaction in vegblock.user_block.transactions:
                    tx_userid = transaction.userid
                    tx_timestamp = transaction.timestamp
                    tx_recordid = transaction.recordid
                    tx_comment = transaction.comment
                    tx_dict = {'recordid': tx_recordid, 'comment': tx_comment}
                    incoming_tx = Transaction(tx_userid, tx_timestamp, tx_dict)
                    tx_list.append(incoming_tx)
    
                # Add the signature
                sig = vegblock.signature.sha256WithEcdsa.byteString
    
                # Reconstruct the block
                incoming_block = Block(userid, timestamp, parent_list, tx_list)
                incoming_block.sig = sig
                # incoming_block.print_block()
                return incoming_block


    def send(self, payload, sender=None, destination=None, request_type=None):
        """
           :param payload: A serialized bytestring.
           :param sender: A VegvisirClient or VegvisirServer object.
           :param destination: A Socket object.
           :param request_type: A string.
        """
        if destination:
          status, error = self.server.send(destination, payload)
          if status == comstatus.SOCKET_ERROR:
                if request_type:
                    print("%s Server: Sending %s request failed with error %s\n" % 
                          (self.userid, request_type, error.__str__()))
                else:
                    print("%s Server: Sending response failed with error %s\n" % 
                          (self.userid, error.__str__()))
                return comstatus.SOCKET_ERROR 
          else:
               return comstatus.SUCCESS
        else:
            status, error = self.client.send(payload)
            if status == comstatus.SOCKET_ERROR:
                if request_type:
                    print("%s Client : Sending %s request failed with error %s\n" % 
                          (self.userid, request_type, error.__str__()))
                else:
                    print("%s Client : Sending response failed with error %s\n" % 
                          (self.userid, error.__str__()))
                return comstatus.SOCKET_ERROR 
            else:
                return comstatus.SUCCESS
