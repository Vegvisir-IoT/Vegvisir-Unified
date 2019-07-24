#!/usr/bin/python3


from time import sleep
from multiprocessing.pool import Pool


from vegvisir.emulator.chain_creator import BlockchainGenerator
from vegvisir.emulator.emulation_helpers import parse_main_args
from vegvisir.emulator.emulate_vegvisir import Emulator 
from vegvisir.pub_sub.vegvisir_instance import VirtualVegvisirInstance
from vegvisir.pub_sub.watch_dog import WatchDog

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


def process_result(result):
    """
        Process the result of a protocol call on a node.
        :param result: An ApplyResult object.
    """
    print("Result from node -> %s\n" % result)


def process_error(exception):
    """
        Process the exception that a node ran into.
        :param exception: An ApplyResult object.
    """
    print("A node ran into -> %s error\n" % exception)


def run_emulation(args):
    """
        Launch an emulation either locally or remotely.
        :param args: A list of arguments including the run type,
                     the username, the blockchain file,
                     the parameter file (for peer locations and such),
                     the probability of a crash, the block limit,
                     and the list of protocols.
    """
    emulator = Emulator(args)
    emulator.activate_gossip_layer()
    #emulator.blockchain.synchronize_functions()
    instance = VirtualVegvisirInstance(emulator, 1, topics=["apple"])
    watch_dog = WatchDog(instance.incoming_tx_queue)
    emulator.blockchain.add_observer(watch_dog)
    if args['run'] == "local":
        # Generate the users
        chainfile = args['chainfile']
        paramsfile = args['paramsfile']
        generator = BlockchainGenerator(chainfile, paramsfile)
        generator.create_components()
        user_list = generator.user_names
        print(user_list)


        # Create execution pool
        pool = Pool(len(user_list))

        # Start pool and wait for handlers
        for username in user_list:
            args['username'] = username
            pool.apply_async(emulator.run_core, 
                             callback=process_result,
                             error_callback=process_error)
            sleep(1)
        # Wait for all the nodes to finish
        pool.close()
        pool.join()
    else:
        emulator.run_core()


if __name__ == '__main__':
    args = parse_main_args()
    run_emulation(args)
