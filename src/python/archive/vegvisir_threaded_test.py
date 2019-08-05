from crypto import *
from vegvisir import *
from time import time, sleep
import visualize
import visualizePablo
from copy import *
from collections import Counter, deque
from heapq import *
from random import randint

class Network(object):
    """ 
    The Network object consists of a couple things. It has an event queue
    corresponding to network requests and it has a list of devices that are 
    members of the network. This doesn't use a real network, but it acts 
    as a buffer between two devices and allows us to delay or delete
    communications between the two to simulate network failures. 
    """

    def __init__(self):
        self.priority_heap = []
        self.members = {}
        self.terminate_network = False

    def send(self, sender, peer, payload, time):
        """
        Returns to the sender the amount of time a message will take to send. 
        Otherwise pushes a request onto the network queue.
        Requires
          -  
        """
        tranist_time = randint(0,300)/100
        heappush(self.priority_heap, (time + tranist_time, [sender, peer]+payload))
        return tranist_time

    def main(self, console_log):
        while not self.terminate_network:
            if len(self.priority_heap) > 0:
                event = heappop(self.priority_heap)
                if time() > event[0]:
                    if console_log:
                        self.print_event(event)
                    self.process_event(event)
                else: 
                    heappush(self.priority_heap, event)

    def print_event(self, event):
        if event[1][2] == Event.PEER_REQUEST:
            print([event[0]]+event[1][:4])
        else:
            print([event[0]]+event[1][:3])

    def process_event(self, event):
        # @param event is a tuple (time+tranist_time, list of event info)
        peer = event[1][1]
        peer.process_event(event)
    
    def add_users_to_network(self, user_list):
        for user in user_list:
            user.network = self
            self.members[user.userid] = user
    
    def terminate(self):
        self.terminate_network = True

class Simulator(object):
    def __init__(self):
        self.number_of_users = 2
        self.console_log = False
        self.randomize_merges = False
        self.merge_at_finish = False
        self.merge_partial = False

    def set_number_of_users(self, n):
        self.number_of_users = n

    def enable_event_log(self):
        self.console_log = True
    
    def set_randomize_merges(self):
        self.randomize_merges = True
    
    def set_merge_at_finish(self):
        self.merge_at_finish = True

    def set_merge_partially_at_finish(self):
        self.merge_at_finish = True
        self.merge_partial = True

    def simulate(self):

        ca_private_key = generate_private_key()                            # Generate public and private keys for the certificate authority
        ca_public_key = generate_public_key(ca_private_key)
        userid = 1                                                      # Create an (arbitrarily selected) user id

        ca_cert = Certificate(userid, ca_public_key)                       # Create a new certificate for the certificate authority
        ca_cert.sign(ca_private_key)                                       # Certificate authority signs off on their own new certificate.
        ks = Keystore(ca_cert)  

        users = self.chain_controller_factory(ca_private_key, ca_cert, 2, ks)

        # Initialize network
        network = Network()
        network.add_users_to_network(users)
        pool = ThreadPoolExecutor()
        fut = pool.submit(network.main, self.console_log)

        # do a breif simmulation
        self.do_some_merges(users, self.randomize_merges)
        if self.merge_at_finish:
            self.merge_all_users(users, not self.merge_partial)

        # output some results
        network.terminate()
        self.print_all_chains(users)
        self.compare_all_chains(users)
        self.visualize_all_users(users)
        return "\n–––––––– SIMULATION FINISHED ––––––––\n"

    def print_a_chain(self, chain):
        l = []
        q = deque()
        for h in chain.frontier_nodes:
            q.appendleft(chain.blocks[h])
            l.append(chain.blocks[h])
        print('\n')
        while(len(q) > 0):
            block = q.pop()
            if isinstance(block, GenesisBlock):
                print('GenisisBlock -> Certificate Authority Id: '+str(block.userid)+'; Timestamp: '+str(block.timestamp))
            else:
                print('Block -> Userid: '+str(block.userid)+'; Timestamp: '+str(block.timestamp)+'; Comment:'+str(block.tx['comment'])+'; RecordId: '+str(block.tx['recordid']))
                for parent_hash in block.parents:
                    parent = chain.blocks[parent_hash]
                    if not parent in l:
                        q.appendleft(parent)
                        l.append(parent)
        print('\n')
    
    def print_all_chains(self, user_list):
        for user in user_list:
            print('UserId of Controller With This Chain: ' + str(user.userid))
            self.print_a_chain(user.blockchain)

    def chain_controller_factory(self, ca_private_key, ca_cert, count, ks):
        cert_list = []
        key_list = []
        user_list = []
        for x in range(count):
            private_key, public_key = generate_keypair()         # Generate a public and private key for the user getting a certificate
            userid = len(cert_list) + 2                                                # Create an (arbitrarily selected) user id
            cert = Certificate(userid, public_key)         # Create a certificate for a user
            cert.sign(ca_private_key)                                  # Certificate authority has to sign off on new user certificate
            cert_list.append(cert)
            key_list.append(private_key)

        timestamp = time()                                                                                      # Add a timestamp for the genesis block
        gblock = GenesisBlock(1, timestamp, ca_cert, cert_list)   # Generate the genesis block with a list of all certifications
        gblock.sign(ca_private_key)                                                                                # Have the certificate authority sign off on the Genesis block
        chain = Blockchain(gblock, ks)

        for i in range(len(cert_list)):
            new_chain = deepcopy(chain)
            user = Controller(i+2, new_chain, key_list[i]) 
            user_list.append(user)

        return user_list

    def do_some_merges(self, user_list, randomize_merges):
        pool = ThreadPoolExecutor(1)
        i = 0
        i2 = 0
        if randomize_merges:
            last_index = len(user_list)-1
        else:
            last_index = 1
        while(i <= 40):
            # if (i % 200 == 0):
            if (i % 20 == 0):
                i1 = randint(0,last_index)
                while (i2 == i1):
                    i2 = randint(0,last_index)
                future = pool.submit(user_list[i1].process_event, (time(), [user_list[i2], user_list[i1],Event.CONNECTION_BUILT]))
                user_list[i1].process_event((time(), ['god', user_list[i1], Event.RECORD_REQUEST, [user_list[i1].new_trans()]]))
                
            sleep(1)
            if i%4 == 0:
                i3 = randint(0,last_index)
                user_list[i3].process_event((time(), ['god', user_list[i3], Event.RECORD_REQUEST, [user_list[i3].new_trans()]]))
            print(i)
            i = i + 1
        while not future.done():
            sleep(2)
        
        # future = user_list[0].process_event([Event.CONNECTION_BUILT, user_list[1]])
        # sleep(1)

    def make_some_requests(self, idnum):
        i=0
        while(i <= 5):
            sleep(0.25)
            users[idnum].process_event([Event.RECORD_REQUEST, [users[idnum].new_trans()]])
            i = i + 1
            print(i)

    def compare_chains(self, User1, User2):
        bc1 = User1.blockchain
        bc2 = User2.blockchain
        for bh in bc1.blocks.keys():
            block1 = bc1.blocks[bh]
            if not bh in bc2.blocks:
                return "There exists a hash in user " + str(User1.userid) + "'s blockchain that does not exist in user " + str(User2.userid) + "'s blockchain\n"
            if not isinstance(block1, GenesisBlock):
                block2 = bc2.blocks[bh]
                if Counter(block1.parents) != Counter(block2.parents):
                    return "Two blocks with the same hash have different parents"
                if block1.tx != block2.tx:
                    return "Two blocks with the same hash have different transactions"
        
        for bh in bc2.blocks.keys():
            block2 = bc2.blocks[bh]
            if not bh in bc1.blocks:
                return "There exists a hash in user " + str(User2.userid) + "'s blockchain that does not exist in user " + str(User1.userid) + "'s blockchain\n"
            
        return "User " + str(User1.userid) + " and user " + str(User2.userid) + " have equivilant blockDAG structures\n"

    def compare_all_chains(self, user_list):
        print('\n–––––––– RESULTS ––––––––\n')
        l = len(user_list)
        for i in range(len(user_list)):
            if (l - i >= 2):
                for user2 in user_list[i+1:]:
                    print(self.compare_chains(user_list[i], user2))
        print('\n')

    def merge_all_users(self, user_list, first_run):
        pool = ThreadPoolExecutor(1)
        l = len(user_list)
        for i in range(len(user_list)):
            if (l - i >= 2):
                for user2 in user_list[i+1:]:
                    future = pool.submit(user_list[i].process_event, (time(), [user2, user_list[i],Event.CONNECTION_BUILT]))
                    # user_list[i].process_event([Event.CONNECTION_BUILT, user2])
                    while not future.done():
                        sleep(1)
        
        if first_run == True:
            self.merge_all_users(user_list, False)

    def visualize_all_users(self, user_list):
        for user in user_list:
            visualizePablo.visualize_plotly(user.blockchain)
            sleep(1)


# def validate_all_requests(chain, pows_needed):
#     blocks = chain.blocks
#     l = {}
#     for block in blocks.values():                           # for every block
#         if not isinstance(block, GenesisBlock):             # not including the genesis block
#             if not block.tx['comment'] == b'Type: POW':     # if the block isnt a pow, add it to the list of all blocks
#                 l[block] = []                           # l is now a list of all requests
    
#     q = deque()
#     for h in chain.frontier_nodes:
#         q.appendleft(blocks[h])
#     while (len(q) > 0):
#         b = q.pop()
#         if b.tx['comment'] == b'Type: POW':
#             userid = b.userid
#             q2 = deque()
#             for h in b.parents:
#                 q2.appendleft(blocks[h])
#             while (len(q2) > 0):



simulation = Simulator()
simulation.set_number_of_users(2)
simulation.enable_event_log()
simulation.set_merge_at_finish()
simulation.simulate()
