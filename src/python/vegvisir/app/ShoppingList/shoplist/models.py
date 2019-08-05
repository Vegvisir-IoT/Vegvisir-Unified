from django.db import models
from django.contrib.postgres.fields import ArrayField

from vegvisir.blockchain.block import TransactionId
#from pubsub.Context import Context
from vegvisir.pub_sub.application_context import VegvisirAppContext
from vegvisir.pub_sub.vegvisir_instance import VirtualVegvisirInstance
from vegvisir.pub_sub.app_delegator import VirtualVegvisirAppDelegator
from vegvisir.emulator.emulate_vegvisir import Emulator
from vegvisir.pub_sub.watch_dog import WatchDog

from concurrent.futures import ThreadPoolExecutor
# Create your models here.
class App():
    '''
        contains info about state of the app and initializes the vegvisir instance, delegator, watchdog,
        and thread that polls new transactions to add to a block

        txn_history -> list of all valid transactions made by user
        last_tx_id -> tx id of the last transaction sent down to the blockchain

        context -> VegvisirAppContext for this app
        topics -> set of topics this app cares about
        emulator -> Emualar for this app
        vegInstance -> VirtualVegvisirInstance for this app
        delegator -> VirtualVegvisirAppDelegator for this app that is registered by vegInstance
        watchdog -> WatchDog for this app
        pool -> thread that keeps track of polled transactions 
    '''

    txn_history=[]
    last_tx_id = None

    context = VegvisirAppContext('shopping list', 'a shopping list', set(['costco']))
    topics = set(['costco']) #allow user to select/add new topics

    args = {'username': 'Alpha', 'chainfile' : 'gloirechain.txt', 'crash_prob' : 0.0, 'block_limit' : 1, 'protocol' : 'sendall', 'paramsfile' : 'gloireparams.txt'}
    emulator = Emulator(args)
    emulator.activate_gossip_layer()
    emulator.blockchain.synchronize_functions()

    vegInstance = VirtualVegvisirInstance(emulator, 1)
    delegator = VirtualVegvisirAppDelegator(vegInstance, context)
    vegInstance.register_application_delegator(context, delegator)

    watch_dog = WatchDog(vegInstance, context.channels)
    emulator.blockchain.add_observer(watch_dog)
    pool = ThreadPoolExecutor(2)
    pool.submit(vegInstance.poll_new_transactions)
