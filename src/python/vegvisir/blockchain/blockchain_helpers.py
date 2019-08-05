#!/usr/bin/python3


from struct import pack


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza, Kolbeinn Karlsson"]


def int_to_bytestring(num):
    """ Convert int to a big-endian bytestring. """

    return pack('!I', num)


def double_to_bytestring(num):
    """ Convert a double to a big-endian bytestring. """

    return pack('!d', num)


def str_to_bytestring(string):
    """ Turns a utf-8 encoded string into a bytes. """

    return bytes(str(string), encoding='utf-8')
