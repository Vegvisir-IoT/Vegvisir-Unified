import threading
from types import MappingProxyType


__author__ = "Bruce Eckel & Friends"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Bruce Eckel & Friends"]
__adapted__ = ["https://python-3-patterns-idioms-test.readthedocs.io/\
                en/latest/Observer.html"]


# @brief: Emulation of Java's 'synchronized' keyword
def synchronized(method):
    def f(*args):
        self = args[0]
        self.mutex.acquire()
        try:
            if len(args) > 1:
                return method(self, args[1:])
            else:
                return method(self)
        finally:
            self.mutex.release()
            print("%s released" % method.__name__)
    return f


# @brief: The base class to inherit from and get a mutex
class Synchronization:
    def __init__(self):
        self.mutex = threading.RLock()
    
