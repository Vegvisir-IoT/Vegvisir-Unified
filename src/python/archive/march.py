import random
import time
import crdt_blockchain as crdt


def heading():
    MOVE_BY = 4
    # move here
    x = random.randint(-1, 1)
    y = random.randint(-1, 1)
    return (x * MOVE_BY , y * MOVE_BY)

##  starting_points
#   @returns List of tuples type Integer
def starting_points():
    listing = [(250, 400), (100, 100), (600, 350),
            (90, 200), (480, 72), (123, 600), (90, 680)]
    return listing

def color_index():
    color_map = {0 : "deeppink1", 1 : "blue", 2 : "yellow",
            3 : "brown", 4 : "green", 5 : "carrot",
            6 : "cobalt", 7: "coral", 8 : "darkorchid",
            9 : "gold1"}
    return color_map

##  @class agent
#   @brief Class utilized to represent EFR in environment
class agent:
    def __init__(self, circle, index):
        self.neighbors          = []
        self.ixi                = circle
        self.message            = []
        self.id                 = index
        ## list of Point objects defined in Graphics Module
        self.neighbor_coords    = []
        self.is_table_created   = False
        self.current_pos        = None
        self.response_distance  = 250
        self.distance_registry  = []
        self.blockchain         = None
        self.inbox              = []

    def create_genesis(self):
        timestamp = time.time()
        gblock    = crdt.GenesisBlock(self.id, timestamp, "holder", [])
        return gblock
    #   gblock.sign(private_key)

    def set_message( self, msg ):
        self.message = msg
    def set_index(self, num):
        self.id = num
    def set_neighbors( self, new_list ):
        self.neighbors = new_list

    def get_close_list( self ):
        listing = [i for i, x in enumerate(self.distance_registry) if x]
        return listing

    ##  check_updates
    #   @param full_list 
    def check_updates( self, full_list ):
        if not self.message:
            return False
        elif self.message == "BLOCK":
            print("RCVD BLOCK")
        elif self.blockchain == None:
            print("MSG: {}".format(self.message))
            genesis  =  self.create_genesis()
            my_chain =  crdt.Blockchain(genesis)
            '''new_block = crdt.Block(self.id, time.time(),
                    list(my_chain.blocks.keys()),None, my_chain.k_requirement)
            # add block with transaction
            new_block.store_tx(
                    {'recordid': random.randint(0, 3600),
                        'comment' : self.message})
            my_chain.add(new_block)'''
            transaction = crdt.Transaction( self.id, time.time(),
                    {'recordid': random.randint( 0, 3600 ),
                        'comment' : self.message} )
            self.blockchain = my_chain
            self.blockchain.mem_cache.append( transaction )
            self.blockchain.print_chain()
            return True
        elif self.blockchain:
            response_list = self.blockchain.evaluate_mem_cache( self.id )
            if response_list:
                contact = self.get_neighbor_coords( full_list )
                for action, index  in response_list:
                    print(action)
                    print(index)
                    # if need_response
                    if action == "SIGN":
                        print("Sign it")
                    elif action == "PROCESS":
                        print("Process Block")
                    else:
                        # send
                        actor = full_list[contact]
                        actor.message.append("BLOCK")
                        actor.inbox.append(self.blockchain)
                        actor.message.append("TX")
                        actor.inbox.append(self.blockchain.mem_cache[index])
            self.blockchain.print_chain()
            return True

    ##  get_neighbor_coords
    #   @param full_listing : dict of other participating 
    def get_neighbor_coords(self, full_listing):
        # update coordinates
        self.current_pos = self.ixi.getCenter()
        self.distance_registry = []
        # import coordinates
        for idx, neighbor in enumerate(self.neighbors):
            temp = full_listing[neighbor]
            my_args = temp.ixi.getCenter()
            self.define_distance(my_args)
            if self.is_table_created:
                self.neighbor_coords[idx] = my_args
            else:
                self.neighbor_coords.append(my_args)
        if not self.is_table_created:
            self.is_table_created = True
        close_neighbors = self.get_close_list()
        if close_neighbors:
            print(close_neighbors)
            contact = random.choice(close_neighbors)
            print("Contacting: {}".format(contact))
            return contact
            '''for neighbor, indicator in zip(self.neighbors,
                    self.distance_registry):
                print("Neigh: {}, Close Enough: {}".format(neighbor,
                    indicator))'''

    ##  define_distance
    #   Utility method for finding the distance between
    #   agent and surrounding neighbors.
    #   @params n_pointer : Pointer of the associated neighbor
    #   @returns Boolean representing if close enough
    def define_distance( self, n_pointer ):
        my_x = self.current_pos.getX()
        my_y = self.current_pos.getY()
        neighbor_x = n_pointer.getX()
        neighbor_y = n_pointer.getY()
        distance = ((my_x - neighbor_x)**2 + (my_y - neighbor_y)**2)**(1/2.0)
        print("Current Position, X: {} Y:{}\nDistance: {}".format(my_x,
            my_y, distance))
        self.distance_registry.append( distance <= self.response_distance )

    ''' Abstract
    def handle_messages():
        if self.message == "":
            pass
        else:
            # Check if message has been seen before
            # --if not, handle block chain
            #       ** if no block chain --> absorb message and send out new block chain
            #       ** if block chain: 
            #           *** check to see if update to old block chain
            #           *** if block chain is longer, see if current messages
            #           *** are on block chain. If changes are:
            #                   **** absorb block chain and add signature 
            #           *** If changes are not:
            #               add current messages and distribute.
            print("Need to handle this!")
    '''
