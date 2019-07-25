class Context():
    appID: str
    desc : str
    channels : set()


    '''
     * Constructor
     *
     * @param appId    : String representation that reflects the application name
     * @param desc     : String
     * @param channels : Set of Strings pertaining to topics the application
     *                 wants updates from the Vegvisir blockchain.
    '''
    def __init__(self, id : str, desc : str, ch : set()):
        self.appID = id
        self.desc = desc
        self.channels = ch

    '''
     * updateChannels
     * TODO: The function name does not reflect what it did inside. This should be 'addChannel' instead of 'updateChannel'. Furthermore, app developers can update topics/channels through getChannels(). Therefore, suggest removing this function. Marked deprecated.
     *
     * @param topic String representation
     * @return True iff string was added to channel set
     */
    '''
    def updateChannels(self, topic : str) -> bool:
        prevSize = len(self.channels)
        self.channels.add(topic)
        return prevSize != len(self.channels)
    