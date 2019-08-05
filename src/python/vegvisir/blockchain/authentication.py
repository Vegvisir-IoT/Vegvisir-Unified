#!/usr/bin/python3

from .crypto import (sign, serialize_public_key, serialize_private_key,
                     deserialize_public_key, verify_signature)
from .blockchain_helpers import str_to_bytestring

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza, Kolbeinn Karlsson, Pablo Fiori"]


# @brief A class representing a user's certificate.
class Certificate(object):

    """
        Data structure representing a user certificate.
        The private_key is only set and serialized when the user needs
        to accesss it remotely when starting its copy of the blockchain.
        :param userid: string.
        :param public_key: bytes. 
        :param private_key: bytes.
    """
    def __init__(self, userid, public_key, private_key=None):
        self.userid = userid
        self.public_key = serialize_public_key(public_key)
        if private_key:
            self.private_key = serialize_private_key(private_key)
        self.sig = None
        # Partial_data avoids multiple calls to partial serialization
        # when signing, hashing, and verifying the block data.
        self.partial_data = self.serialize_partial()

    def get_public_key(self):
        return deserialize_public_key(self.public_key)

    def serialize_partial(self):
        bytestring = str_to_bytestring(self.userid)
        bytestring += self.public_key
        return bytestring

    def sign(self, private_key):
        self.sig = sign(self.partial_data, private_key)

    def verify(self, public_key):
        return verify_signature(self.partial_data, self.sig, public_key)

    def serialize(self):
        return self.partial_data + self.sig


# @brief A class representing the storage/verification of certificates.
class Keystore(object):

    def __init__(self, ca_cert):
        self.ca_cert = ca_cert
        self.certs = {}
        self.certs[ca_cert.userid] = ca_cert

    def add_certificate(self, cert):
        if cert.verify(self.ca_cert.get_public_key()):
            self.certs[cert.userid] = cert
            return True
        else:
            return False

    def get_public_key(self, userid):
        return self.certs[userid].get_public_key()

    def has_user(self, userid):
        return userid in self.certs

    def get_users(self):
        return list(self.certs.keys())
