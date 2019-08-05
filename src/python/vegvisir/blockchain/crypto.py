"""
Wrappers around the cryptography API
"""

from cryptography.hazmat.primitives.asymmetric import ec
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives import serialization
from cryptography.exceptions import InvalidSignature

_string_encoding = 'UTF-8'
_elliptic_curve = ec.SECP384R1()
_backend = default_backend()
_hash_algorithm = hashes.SHA256()
_dsa_algorithm = ec.ECDSA(_hash_algorithm)


def deserialize_public_key(serialized_public_key):
    """ Deserialize a public key.

    Parameters
    ----------
    serialized_public_key : str

    Returns
    -------
    public_key : A public key object

    """
    public_key = serialization.load_pem_public_key(
        serialized_public_key,
        backend=_backend)
    return public_key


def deserialize_private_key(serialized_private_key):
    """ Deserialize a private key.

    Parameters
    ----------
    serialized_private_key : str

    Returns
    -------
    private_key : A public key object

    """
    private_key = serialization.load_pem_private_key(
        serialized_private_key,
        password=None,
        backend=_backend)
    return private_key


def serialize_private_key(private_key):
    """ Serialize a private key.

    Parameters
    ----------
    private_key : private_key object

    Returns
    -------
    serialized_private_key : str
    """
    serialized_private_key = private_key.private_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PrivateFormat.PKCS8,
        encryption_algorithm=serialization.NoEncryption())
    return serialized_private_key


def serialize_public_key(public_key):
    """ Serialize a public key.

    Parameters
    ----------
    public_key : public_key object

    Returns
    -------
    serialized_public_key : str
    """
    serialized_public_key = public_key.public_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PublicFormat.SubjectPublicKeyInfo)
    return serialized_public_key


def generate_private_key():
    """ Generate a new private key.

    Returns
    -------
    key : private_key object
    """
    return ec.generate_private_key(_elliptic_curve, _backend)


def generate_public_key(private_key):
    """ Generate a public key from a given private key.

    Parameters
    ----------
    private_key : private_key object

    Returns
    -------
    public_key : public_key object
       The public key corresponding to the supplied private key/

    """
    return private_key.public_key()


def generate_keypair():
    private_key = generate_private_key()
    public_key = generate_public_key(private_key)
    return private_key, public_key


def sign(data, private_key):
    """ Generate a digital signature for the supplied data.

    Parameters
    ----------
    data: byte string
       The data to be signed.

    private_key: private_key object
       The private key to use to sign data

    Returns
    -------
    signature : bytes
       The signature
    """
    return private_key.sign(data, _dsa_algorithm)


def verify_signature(data, signature, public_key):
    """ Verify a digital signature.

    Parameters
    ----------
    data: bytes
       The data that was signed.

    signature: bytes
       The signature.

    public_key: public_key
       The public key corresponding to the private key used to sign the data.

    Returns
    -------
    result: bool
       True is the verification succeeds, False otherwise.

    """
    result = False
    try:
        public_key.verify(signature, data, _dsa_algorithm)
    except InvalidSignature:
        result = False
    else:
        result = True
    return result


def hash_data(data):
    """ Generates a hash digest for the data.
        :param data: bytestring
        :rtype: bytes
    """
    digest = hashes.Hash(_hash_algorithm, backend=_backend)
    digest.update(data)
    return digest.finalize()
