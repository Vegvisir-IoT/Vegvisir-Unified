
__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


#@brief: A class that stores information about the application and its channels. 
class VegvisirAppContext:

    """
       The representation of the state of an application.
       :param app_id: A string.
       :param desc: A string
       :param channels: A set of strings.
    """
    def __init__(self, app_id, desc, channels):
        self.app_id = app_id
        self.desc = desc
        self.channels = channels


    def update_channels(self, topic):
        """
           :param topic: A string.
        """
        self.channels.add(topic)
