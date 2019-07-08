from enum import Enum


# @brief: an enumeration of types of protocol failures
class ProtocolStatus(Enum):
    SUCCESS = 1
    ONGOING_VECTOR_PROTOCOL = 2
    FSET_REQUEST_FAILURE = 3
    BCHASHES_REQUEST_FAILURE = 4
    BLOCK_REQUEST_FAILURE = 5
    RECONCILIATION_FAILURE = 6
    CONNECTION_ATTEMPT_FAILURE = 7
    REQUEST_DELAY_FSET = 8
    REQUEST_DELAY_BLOCK = 9
    REQUEST_DELAY_DECISION = 10
    REQUEST_DELAY_PROTOCOL_LIST = 11
    PROTOCOL_DISAGREEMENT = 12
    CONNECTION_FAILURE = 13

# @brief: an enumeration of the vegvisir types
class MessageTypes(Enum):
    REQUEST = 0
    RESPONSE = 1
    UPDATE = 2

# @brief: an enumeration of receiving failures.
class CommunicationStatus(Enum):
    SOCKET_ERROR = 1
    SUCCESS = 2
    NO_DATA = 3

# @brief: an enumeration of states for all protocols.
class ProtocolState(Enum):
    HANDSHAKE = 1
    LOCAL_DOMINATES = 2
    REMOTE_DOMINATES = 3 
    EVEN = 4
    RECONCILIATION = 5 
    PROTOCOL_DISAGREEMENT = 6 
    CONNECTION_ATTEMPT_FAILURE = 7    
