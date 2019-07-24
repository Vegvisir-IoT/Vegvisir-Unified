import threading
from types import MappingProxyType


__author__ = "Bruce Eckel & Friends"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Bruce Eckel & Friends"]

# @brief: Emulation of Java's 'synchronized' keyword
def synchronized(method):
    def f(*args):
        self = args[0]
        print("self is %s\n" % self)
        self.mutex.acquire()
        try:
            print(args)
            print("About to run %s\n" % method.__name__)
            print("Observers %s\n" % self.observers)
            return method(self, args[1:])
        finally:
            self.mutex.release()
            print("%s released" % method.__name__)
    return f


# @brief: Sychronizing an entire class or just some methods in it.
def synchronize(klass, names=None):
    if type(names) == str:
        names = names.split()

    klass_copy = MappingProxyType(klass.__dict__)
    for (name,val) in klass_copy.items():
        if callable(val) and name != '__init__' and \
            (names == None or name in names):
            print("Synchronizing %s\n" % name)
            synch_val = synchronized(val)
            klass.__setattr__(name, synch_val)

# @brief: The base class to inherit from and get a mutex
class Synchronization:
    def __init__(self):
        self.mutex = threading.RLock()
    
