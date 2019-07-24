
__author__ = "Bruce Eckel & Friends"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Bruce Eckel & Friends"]

from vegvisir.design_pattern.synchronization import Synchronization, synchronized 

# @brief: A module to support the "observer" pattern found in Java.

class Observer:

    def update(observable, arg):
        """
            Update observers the observable is modified.
            To be overridden in the subclasses
            :param observable: some object.
            :param arg: the object that triggers the nofify_observers.
        """
        pass


class Observable(Synchronization):
    """
        A representation of the observable in relation to its observers.
    """
    def __init__(self):
        self.observers = []
        self.changed = 0
        Synchronization.__init__(self)

    #def __setattr__(self, name, value):
    #    """
    #       Set the attribute for the instance.
    #       :param name: A string.
    #       :param value: An object.
    #    """
    #    super().__setattr__(name, value)


    def add_observer(self, observer):
        if observer not in self.observers:
            self.observers.append(observer)
        print("All attributes %s\n" % self.__dict__)
    add_observer = synchronized(add_observer)


    def delete_observer(self, observer):
        self.observers.remove(observer)
    delete_observer = synchronized(delete_observer)


    def notify_observers(self, arg=None):
        """
           Notify observers that the object has been changed.
           :param arg: The object that triggered the change in state.
        """
        self.mutex.acquire()
        try:
            if not self.changed:
                return
            else:
                # Make a copy in case synchronous addition of observers.
                local_copy = self.observers[:]
                self.clear_changed()
        finally:
            self.mutex.release()
        # Updating is not required to be synchronized.
        for observer in local_copy:
            print("Notifying %s\\n" % observer)
            observer.update(self, arg)

    def synchronize_functions(self):
        self.delete_observers = synchronized(self.delete_observers)
        self.set_changed = synchronized(self.set_changed)
        self.clear_changed = synchronized(self.clear_changed)
        self.has_changed = synchronized(self.has_changed)
        self.count_observers = synchronized(self.count_observers)
    


    def delete_observers(self): self.observers = []
    delete_observers = synchronized(delete_observers)
    def set_changed(self): self.changed = 1
    set_changed = synchronized(set_changed)
    def clear_changed(self): self.changed = 0
    clear_changed = synchronized(clear_changed)
    def has_changed(self): return self.changed
    has_changed = synchronized(has_changed)
    def count_observers(self): return len(self.observers)
    count_observers = synchronized(count_observers)

#synchronize(Observable, "add_observer delete_observer delete_observers" +
#                        "set_changed clear_changed has_changed" +
#                        "count_observers")
