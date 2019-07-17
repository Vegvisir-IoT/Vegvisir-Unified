from random import randint


from vegvisir.blockchain.crypto import verify_signature
from vegvisir.blockchain.blockchain_helpers import (int_to_bytestring,
                                                     str_to_bytestring)


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

#@brief: A class for handling all operations on vector clocks
class VectorClock(object):

    """
       A data structure representing a node's vector clock.

       :param userid: A string.
       :param peer_names: A list.
       :param genesis_hash: A bytestring.
    """
    def __init__(self, userid, peer_names, genesis_hash, blockchain):
        self.peer_names = peer_names

        # Maps userids to their leading blocks and block hashes.
        self.vector = {}

        # Maps block hashes to their parents and tags.
        self.block_map = {}

        # Add the block map for the genesis block.
        self.block_map[genesis_hash] = {'tag': "Commander0", 'parents' : []}
        # Maps block tags to their children and block hashes.
        self.tag_map = {}

        # Add the tag for the genesis block.
        self.tag_map["Commander0"] = {'bhash': genesis_hash,
                                      'children' : []}

        self.offline_activity = False
        self.userid = userid
        for peer in self.peer_names:
           self.vector[peer] = {'block': 0, 'bhash': genesis_hash}
        self.blockchain = blockchain

        print("Vector clock %s\n" % self.vector)

    def compare(self, other):
        """
           Compare a peer's vector clock to ours.
           Return which blocks to request.
           :param other: a vgp.VectorClock protobuf object.
        """ 
        differences = {}
        print("My view of the world\n")
        view = [str(key) + str(value['block']) + "\n" for key,value in self.vector.items()] 
        print(view)
        print("Peer's view")
        for userid, latest in other.clocks.items():
            print("Name: %s, Block: %s" % (userid, latest))
            diff = self.vector[userid]['block'] - latest 
            if diff > 0:
                if userid == self.userid and latest == 0:
                    starting_point = 1
                else:
                    starting_point = latest + 1
                differences[userid] = tuple([diff, starting_point])
        print("Differences %s\n" % differences)
        return differences 

    def create_tag(self, userid, number):
        """
           Concatenate a tag for a block.
           :param userid: A string.
           :param number: An int.
        """
        return str(userid) + str(number)


    def get_leading_block(self, userid):
        """"
            :param userid: A string.
        """
        return self.vector[userid]['block']


    def get_block_tag(self, bhash):
        """
           :param bhash: A bytestring.
        """
        return self.block_map[bhash]['tag']


    def add_block_mapping(self, userid, number, bhash, parent_userids,
                          parent_blocks):
        """
           Map a new block into the vector clock.
           :param userid: A string.
           :param number: An int.
           :param bhash: A bytestring.
           :param parent_userids: A list of strings.
           :param parent_blocks: A list of ints.
        """
        mapped_parents = list(zip(parent_userids, parent_blocks))
        tag = self.create_tag(userid, number)
        self.block_map[bhash] = {'tag': tag, 'parents': mapped_parents}
        # print("New block map %s\n" % self.block_map)


    def add_block_children(self, parent_tag, bhash, child_tag):
        """
           :param parent_tag: A string.
           :param bhash: A bytestring.
           :param child_tag: A string.
        """
        if parent_tag in self.tag_map:
            self.tag_map[parent_tag]['children'].append(child_tag)
        else:
            self.tag_map[parent_tag] = {'children': [], 'bhash': bhash}
            self.tag_map[parent_tag]['children'].append(child_tag)
        print("New children tag map %s\n" % self.tag_map[parent_tag])


    def update_vector(self, userid, number, bhash):
        """
           :param userid: A string.
           :param number: An int.
           :param bhash: A bytestring.
        """
        self.vector[userid]['block'] = number
        self.vector[userid]['bhash'] = bhash

    def update_offline_activity(self, update):
        """
           Update that we have created blocks since last gossip w/ any peer.
           :param update: A boolean.
        """
        self.offline_activity = update 


    def get_all_leading_blocks(self):
        """
           Get the userids and leading blocks for all peers in the network.
           :param userid: A string.
        """
        all_vectors = self.vector.items()
        userids = [key for key,value in all_vectors]
        block_nums = [value['block'] for _, value in all_vectors] 
        return userids, block_nums

    def verify_vector_clock(self, other):
        """
           Verify the vector clock data signed by remote peer.
           :param other: A VectorClock protobuf object.
        """
        bytestring = bytearray()
        #for userid, latest_block in other.clocks.items():
        #    print("Verifying %s, %s\n" % (userid, latest_block))
        #    bytestring += str_to_bytestring(userid)
        #    bytestring += int_to_bytestring(latest_block)
        bytestring += str_to_bytestring(other.userid)
        bytestring += int_to_bytestring(other.sendLimit)
        public_key = self.blockchain.keystore.get_public_key(other.userid)
        return verify_signature(bytestring, other.signature, public_key)

    def compute_dependencies(self, other):
        """
           Find the correct dependencies among blocks for ease of sending.
           :param other: A VectorClock protobuf object.
        """
        # Verify the vector clock signature
        is_signature_valid = self.verify_vector_clock(other)
        if not is_signature_valid:
            return "Invalid vector clock"

        print("\n\nCOMPUTING DEPENDENCIES\n\n")
        differences = self.compare(other)
        if len(differences) == 0:
            return None
        hashes = []
        if self.offline_activity == True:
            print("THERE WAS OFFLINE ACTIVITY\n")
            leading_bhash = self.vector[self.userid]['bhash']
            for parent in self.block_map[leading_bhash]['parents']:
                userid = parent[0]
                if userid in differences:
                    start_block_tag = self.create_tag(userid,
                                                  differences[userid][1])

                    # Append the hash of where we are starting
                    hashes.append(self.tag_map[start_block_tag]['bhash'])

                    # Loop through the children
                    children = self.tag_map[start_block_tag]['children']
                    total_children = len(children)
                    child = children[0]
                    local_leading_tag = self.block_map[leading_bhash]['tag']
                    while total_children == 1 and child != local_leading_tag:
                        child_hash = self.tag_map[child]['bhash']
                        hashes.append(child_hash)
                        if len(hashes) == other.sendLimit:
                            return hashes    
                        children = self.tag_map[child]['children']
                        total_children = len(children)
                        child = children[0]
            hashes.append(leading_bhash)
        else:
            print("THERE WAS NO OFFLINE ACTIVITY\n")
            for leading_block_details in self.vector.values():
                leading_bhash = leading_block_details['bhash']
                for parent in self.block_map[leading_bhash]['parents']:
                    userid = parent[0]
                    if userid in differences:
                        start_block_tag = self.create_tag(userid,
                                                  differences[userid][1])
                        # Append the hash of where we are starting
                        hashes.append(self.tag_map[start_block_tag]['bhash'])

                        # Loop through the children
                        children = self.tag_map[start_block_tag]['children']
                        total_children = len(children)
                        child = children[0]
                        local_leading_tag = self.block_map[leading_bhash]['tag']
                        while total_children == 1 and child != local_leading_tag:
                            hashes.append(self.tag_map[child]['bhash'])
                            if len(hashes) == other.send_limit:
                                return hashes    
                            children = self.tag_map[child]['children']
                            total_children = len(children)
                            child = children[0]
        return hashes
