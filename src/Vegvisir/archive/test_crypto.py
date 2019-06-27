import unittest
import inspect
from crypto import *


class Test_crypto(unittest.TestCase):
    def test_sign(self):
        print("Test started: %s" %inspect.stack()[0][3])
        # Sign a bytestring and verify the signature
        privkey = generate_private_key()
        right_pubkey = generate_public_key(privkey)
        privkey2 = generate_private_key()
        wrong_pubkey = generate_public_key(privkey2)
        data = b'4kd934h28d95k70dj2'
        wrong_data = b'4kd934h28d95k70dj3'
        signature = sign(data, privkey)
        assert verify_signature(data, signature, right_pubkey)
        assert verify_signature(data, signature, wrong_pubkey) == False
        assert verify_signature(wrong_data, signature, right_pubkey) == False
        print("Test passed: %s" %inspect.stack()[0][3])


    def test_multiple_signatures(self):
        print("Test started: %s" %inspect.stack()[0][3])
        # Sign two different objects with two different keys to test
        # if the two signatures interfere with one another
        privkey1 = generate_private_key()
        pubkey1 = generate_public_key(privkey1)
        data1 = b'4kd934h28d95k70dj2'
        signature1 = sign(data1, privkey1)

        privkey2 = generate_private_key()
        pubkey2 = generate_public_key(privkey2)
        data2 = b'a.ck-nmfmn4903ufn1'
        signature2 = sign(data2, privkey2)

        # Test all combinations
        assert verify_signature(data1, signature1, pubkey1)
        assert verify_signature(data1, signature1, pubkey2) == False
        assert verify_signature(data1, signature2, pubkey1) == False
        assert verify_signature(data1, signature2, pubkey2) == False
        assert verify_signature(data2, signature1, pubkey1) == False
        assert verify_signature(data2, signature1, pubkey2) == False
        assert verify_signature(data2, signature2, pubkey1) == False
        assert verify_signature(data2, signature2, pubkey2)
        print("Test passed: %s" %inspect.stack()[0][3])


    def test_signature_string_format(self):
        print("Test started: %s" %inspect.stack()[0][3])
        # Sign a bytestring and verify the signature
        privkey = generate_private_key()
        pubkey = generate_public_key(privkey)
        byte_data = b'4kd934h28d95k70dj2'
        unicode_data = u'4kd934h28d95k70dj3'
        signature_bytes = sign(byte_data, privkey)
        # signature_unicode = sign(unicode_data, privkey)
        assert verify_signature(byte_data, signature_bytes, pubkey)
        # assert verify_signature(unicode_data, signature_unicode, pubkey) == True
        print("Test passed: %s" %inspect.stack()[0][3])


    def test_serialization(self):
        print("Test started: %s" %inspect.stack()[0][3])
        # Generate keypair
        privkey = generate_private_key()
        pubkey = generate_public_key(privkey)
        # Serialize
        sprivkey = serialize_private_key(privkey)
        spubkey = serialize_public_key(pubkey)
        # Deserialize
        dsprivkey = deserialize_private_key(sprivkey)
        dspubkey = deserialize_public_key(spubkey)
        # Generate data
        data = b'20dm5jf73nw0c0d934ke8fg62h1-sdkgb67r9vc'
        # Sign and verify
        signature1 = sign(data, privkey)
        signature2 = sign(data, dsprivkey)
        assert verify_signature(data, signature1, pubkey)
        assert verify_signature(data, signature1, dspubkey)
        assert verify_signature(data, signature2, pubkey)
        assert verify_signature(data, signature2, dspubkey)
        print("Test passed: %s" %inspect.stack()[0][3])

if __name__ == '__main__':
    unittest.main()
