#!/usr/bin/python3


from enum import Enum


# @brief: an enumeration of events to be handled.
class Event(Enum):
    RECORD_REQUEST = 0
    CONNECTION_BUILT = 1
    PEER_REQUEST = 2
    PEER_SEND = 3


# @brief an enumeration of requests sent by peer device.
class Request(Enum):
    SEND_FRONTIER_SET = 0
    SEND_BCHASHES = 1
    SEND_BLOCK = 2
    ADD_BLOCK = 3
    CREATE_POW = 4
    R_SET_SIZE = 5


# @brief an enumeration of possible states based on latest operation
class Operation(Enum):
    ADDED_REQUEST = 0
    GOT_POW = 1
    PROVIDED_POW = 2
    RECONCILED_PEER = 3
    RECEIVED_REGULAR_BLOCK = 4
