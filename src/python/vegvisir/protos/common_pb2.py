# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: common.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


from google.protobuf import timestamp_pb2 as google_dot_protobuf_dot_timestamp__pb2


DESCRIPTOR = _descriptor.FileDescriptor(
  name='common.proto',
  package='charlotte',
  syntax='proto3',
  serialized_pb=_b('\n\x0c\x63ommon.proto\x12\tcharlotte\x1a\x1fgoogle/protobuf/timestamp.proto\"?\n\x04Hash\x12\x0e\n\x04sha3\x18\x01 \x01(\x0cH\x00\x12\x10\n\x06sha256\x18\x02 \x01(\x0cH\x00\x42\x15\n\x13hashalgorithm_oneof\"\x8f\x01\n\tPublicKey\x12\x43\n\x11\x65llipticCurveP256\x18\x01 \x01(\x0b\x32&.charlotte.PublicKey.EllipticCurveP256H\x00\x1a\'\n\x11\x45llipticCurveP256\x12\x12\n\nbyteString\x18\x01 \x01(\x0c\x42\x14\n\x12keyalgorithm_oneof\"f\n\x08\x43ryptoId\x12)\n\tpublicKey\x18\x01 \x01(\x0b\x32\x14.charlotte.PublicKeyH\x00\x12\x1f\n\x04hash\x18\x02 \x01(\x0b\x32\x0f.charlotte.HashH\x00\x42\x0e\n\x0cidtype_oneof\"\xda\x01\n\tSignature\x12%\n\x08\x63ryptoId\x18\x01 \x01(\x0b\x32\x13.charlotte.CryptoId\x12Q\n\x0fsha256WithEcdsa\x18\x02 \x01(\x0b\x32\x36.charlotte.Signature.SignatureAlgorithmSHA256WithECDSAH\x00\x1a\x37\n!SignatureAlgorithmSHA256WithECDSA\x12\x12\n\nbyteString\x18\x01 \x01(\x0c\x42\x1a\n\x18signaturealgorithm_oneof\"\x92\x01\n\tReference\x12\x1d\n\x04hash\x18\x01 \x01(\x0b\x32\x0f.charlotte.Hash\x12\x31\n\x18\x61vailabilityAttestations\x18\x02 \x03(\x0b\x32\x0f.charlotte.Hash\x12\x33\n\x15integrityAttestations\x18\x03 \x03(\x0b\x32\x14.charlotte.Reference\"\xff\x07\n\x14IntegrityAttestation\x12P\n\x12hetconsAttestation\x18\x04 \x01(\x0b\x32\x32.charlotte.IntegrityAttestation.HetconsAttestationH\x00\x1a\x88\x01\n\tChainSlot\x12#\n\x05\x62lock\x18\x01 \x01(\x0b\x32\x14.charlotte.Reference\x12\"\n\x04root\x18\x02 \x01(\x0b\x32\x14.charlotte.Reference\x12\x0c\n\x04slot\x18\x03 \x01(\x04\x12$\n\x06parent\x18\x04 \x01(\x0b\x32\x14.charlotte.Reference\x1ax\n\x0fSignedChainSlot\x12<\n\tchainSlot\x18\x01 \x01(\x0b\x32).charlotte.IntegrityAttestation.ChainSlot\x12\'\n\tsignature\x18\x02 \x01(\x0b\x32\x14.charlotte.Signature\x1ak\n\x15TimestampedReferences\x12-\n\ttimestamp\x18\x01 \x01(\x0b\x32\x1a.google.protobuf.Timestamp\x12#\n\x05\x62lock\x18\x02 \x03(\x0b\x32\x14.charlotte.Reference\x1a\x9c\x01\n\x1bSignedTimestampedReferences\x12T\n\x15timestampedReferences\x18\x01 \x01(\x0b\x32\x35.charlotte.IntegrityAttestation.TimestampedReferences\x12\'\n\tsignature\x18\x02 \x01(\x0b\x32\x14.charlotte.Signature\x1aw\n\x0cGitSimBranch\x12-\n\ttimestamp\x18\x01 \x01(\x0b\x32\x1a.google.protobuf.Timestamp\x12\x12\n\nbranchName\x18\x02 \x01(\t\x12$\n\x06\x63ommit\x18\x03 \x01(\x0b\x32\x14.charlotte.Reference\x1a\x81\x01\n\x12SignedGitSimBranch\x12\x42\n\x0cgitSimBranch\x18\x01 \x01(\x0b\x32,.charlotte.IntegrityAttestation.GitSimBranch\x12\'\n\tsignature\x18\x02 \x01(\x0b\x32\x14.charlotte.Signature\x1a\x65\n\x12HetconsAttestation\x12&\n\tobservers\x18\x01 \x03(\x0b\x32\x13.charlotte.CryptoId\x12\'\n\tmessage2b\x18\x02 \x03(\x0b\x32\x14.charlotte.ReferenceB \n\x1eintegrityattestationtype_oneof\"\xcf\x02\n\x17\x41vailabilityAttestation\x12S\n\x12signedStoreForever\x18\x01 \x01(\x0b\x32\x35.charlotte.AvailabilityAttestation.SignedStoreForeverH\x00\x1a\x33\n\x0cStoreForever\x12#\n\x05\x62lock\x18\x01 \x03(\x0b\x32\x14.charlotte.Reference\x1a\x84\x01\n\x12SignedStoreForever\x12\x45\n\x0cstoreForever\x18\x01 \x01(\x0b\x32/.charlotte.AvailabilityAttestation.StoreForever\x12\'\n\tsignature\x18\x02 \x01(\x0b\x32\x14.charlotte.SignatureB#\n!availabilityattestationtype_oneofBK\n\x1e\x63om.isaacsheff.charlotte.protoB\x14\x43harlotteCommonProtoP\x01\xa2\x02\x10\x43HARLOTTE_COMMONb\x06proto3')
  ,
  dependencies=[google_dot_protobuf_dot_timestamp__pb2.DESCRIPTOR,])
_sym_db.RegisterFileDescriptor(DESCRIPTOR)




_HASH = _descriptor.Descriptor(
  name='Hash',
  full_name='charlotte.Hash',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='sha3', full_name='charlotte.Hash.sha3', index=0,
      number=1, type=12, cpp_type=9, label=1,
      has_default_value=False, default_value=_b(""),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='sha256', full_name='charlotte.Hash.sha256', index=1,
      number=2, type=12, cpp_type=9, label=1,
      has_default_value=False, default_value=_b(""),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
    _descriptor.OneofDescriptor(
      name='hashalgorithm_oneof', full_name='charlotte.Hash.hashalgorithm_oneof',
      index=0, containing_type=None, fields=[]),
  ],
  serialized_start=60,
  serialized_end=123,
)


_PUBLICKEY_ELLIPTICCURVEP256 = _descriptor.Descriptor(
  name='EllipticCurveP256',
  full_name='charlotte.PublicKey.EllipticCurveP256',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='byteString', full_name='charlotte.PublicKey.EllipticCurveP256.byteString', index=0,
      number=1, type=12, cpp_type=9, label=1,
      has_default_value=False, default_value=_b(""),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=208,
  serialized_end=247,
)

_PUBLICKEY = _descriptor.Descriptor(
  name='PublicKey',
  full_name='charlotte.PublicKey',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='ellipticCurveP256', full_name='charlotte.PublicKey.ellipticCurveP256', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[_PUBLICKEY_ELLIPTICCURVEP256, ],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
    _descriptor.OneofDescriptor(
      name='keyalgorithm_oneof', full_name='charlotte.PublicKey.keyalgorithm_oneof',
      index=0, containing_type=None, fields=[]),
  ],
  serialized_start=126,
  serialized_end=269,
)


_CRYPTOID = _descriptor.Descriptor(
  name='CryptoId',
  full_name='charlotte.CryptoId',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='publicKey', full_name='charlotte.CryptoId.publicKey', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='hash', full_name='charlotte.CryptoId.hash', index=1,
      number=2, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
    _descriptor.OneofDescriptor(
      name='idtype_oneof', full_name='charlotte.CryptoId.idtype_oneof',
      index=0, containing_type=None, fields=[]),
  ],
  serialized_start=271,
  serialized_end=373,
)


_SIGNATURE_SIGNATUREALGORITHMSHA256WITHECDSA = _descriptor.Descriptor(
  name='SignatureAlgorithmSHA256WithECDSA',
  full_name='charlotte.Signature.SignatureAlgorithmSHA256WithECDSA',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='byteString', full_name='charlotte.Signature.SignatureAlgorithmSHA256WithECDSA.byteString', index=0,
      number=1, type=12, cpp_type=9, label=1,
      has_default_value=False, default_value=_b(""),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=511,
  serialized_end=566,
)

_SIGNATURE = _descriptor.Descriptor(
  name='Signature',
  full_name='charlotte.Signature',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='cryptoId', full_name='charlotte.Signature.cryptoId', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='sha256WithEcdsa', full_name='charlotte.Signature.sha256WithEcdsa', index=1,
      number=2, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[_SIGNATURE_SIGNATUREALGORITHMSHA256WITHECDSA, ],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
    _descriptor.OneofDescriptor(
      name='signaturealgorithm_oneof', full_name='charlotte.Signature.signaturealgorithm_oneof',
      index=0, containing_type=None, fields=[]),
  ],
  serialized_start=376,
  serialized_end=594,
)


_REFERENCE = _descriptor.Descriptor(
  name='Reference',
  full_name='charlotte.Reference',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='hash', full_name='charlotte.Reference.hash', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='availabilityAttestations', full_name='charlotte.Reference.availabilityAttestations', index=1,
      number=2, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='integrityAttestations', full_name='charlotte.Reference.integrityAttestations', index=2,
      number=3, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=597,
  serialized_end=743,
)


_INTEGRITYATTESTATION_CHAINSLOT = _descriptor.Descriptor(
  name='ChainSlot',
  full_name='charlotte.IntegrityAttestation.ChainSlot',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='block', full_name='charlotte.IntegrityAttestation.ChainSlot.block', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='root', full_name='charlotte.IntegrityAttestation.ChainSlot.root', index=1,
      number=2, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='slot', full_name='charlotte.IntegrityAttestation.ChainSlot.slot', index=2,
      number=3, type=4, cpp_type=4, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='parent', full_name='charlotte.IntegrityAttestation.ChainSlot.parent', index=3,
      number=4, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=853,
  serialized_end=989,
)

_INTEGRITYATTESTATION_SIGNEDCHAINSLOT = _descriptor.Descriptor(
  name='SignedChainSlot',
  full_name='charlotte.IntegrityAttestation.SignedChainSlot',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='chainSlot', full_name='charlotte.IntegrityAttestation.SignedChainSlot.chainSlot', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='signature', full_name='charlotte.IntegrityAttestation.SignedChainSlot.signature', index=1,
      number=2, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=991,
  serialized_end=1111,
)

_INTEGRITYATTESTATION_TIMESTAMPEDREFERENCES = _descriptor.Descriptor(
  name='TimestampedReferences',
  full_name='charlotte.IntegrityAttestation.TimestampedReferences',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='timestamp', full_name='charlotte.IntegrityAttestation.TimestampedReferences.timestamp', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='block', full_name='charlotte.IntegrityAttestation.TimestampedReferences.block', index=1,
      number=2, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1113,
  serialized_end=1220,
)

_INTEGRITYATTESTATION_SIGNEDTIMESTAMPEDREFERENCES = _descriptor.Descriptor(
  name='SignedTimestampedReferences',
  full_name='charlotte.IntegrityAttestation.SignedTimestampedReferences',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='timestampedReferences', full_name='charlotte.IntegrityAttestation.SignedTimestampedReferences.timestampedReferences', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='signature', full_name='charlotte.IntegrityAttestation.SignedTimestampedReferences.signature', index=1,
      number=2, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1223,
  serialized_end=1379,
)

_INTEGRITYATTESTATION_GITSIMBRANCH = _descriptor.Descriptor(
  name='GitSimBranch',
  full_name='charlotte.IntegrityAttestation.GitSimBranch',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='timestamp', full_name='charlotte.IntegrityAttestation.GitSimBranch.timestamp', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='branchName', full_name='charlotte.IntegrityAttestation.GitSimBranch.branchName', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='commit', full_name='charlotte.IntegrityAttestation.GitSimBranch.commit', index=2,
      number=3, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1381,
  serialized_end=1500,
)

_INTEGRITYATTESTATION_SIGNEDGITSIMBRANCH = _descriptor.Descriptor(
  name='SignedGitSimBranch',
  full_name='charlotte.IntegrityAttestation.SignedGitSimBranch',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='gitSimBranch', full_name='charlotte.IntegrityAttestation.SignedGitSimBranch.gitSimBranch', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='signature', full_name='charlotte.IntegrityAttestation.SignedGitSimBranch.signature', index=1,
      number=2, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1503,
  serialized_end=1632,
)

_INTEGRITYATTESTATION_HETCONSATTESTATION = _descriptor.Descriptor(
  name='HetconsAttestation',
  full_name='charlotte.IntegrityAttestation.HetconsAttestation',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='observers', full_name='charlotte.IntegrityAttestation.HetconsAttestation.observers', index=0,
      number=1, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='message2b', full_name='charlotte.IntegrityAttestation.HetconsAttestation.message2b', index=1,
      number=2, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1634,
  serialized_end=1735,
)

_INTEGRITYATTESTATION = _descriptor.Descriptor(
  name='IntegrityAttestation',
  full_name='charlotte.IntegrityAttestation',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='hetconsAttestation', full_name='charlotte.IntegrityAttestation.hetconsAttestation', index=0,
      number=4, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[_INTEGRITYATTESTATION_CHAINSLOT, _INTEGRITYATTESTATION_SIGNEDCHAINSLOT, _INTEGRITYATTESTATION_TIMESTAMPEDREFERENCES, _INTEGRITYATTESTATION_SIGNEDTIMESTAMPEDREFERENCES, _INTEGRITYATTESTATION_GITSIMBRANCH, _INTEGRITYATTESTATION_SIGNEDGITSIMBRANCH, _INTEGRITYATTESTATION_HETCONSATTESTATION, ],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
    _descriptor.OneofDescriptor(
      name='integrityattestationtype_oneof', full_name='charlotte.IntegrityAttestation.integrityattestationtype_oneof',
      index=0, containing_type=None, fields=[]),
  ],
  serialized_start=746,
  serialized_end=1769,
)


_AVAILABILITYATTESTATION_STOREFOREVER = _descriptor.Descriptor(
  name='StoreForever',
  full_name='charlotte.AvailabilityAttestation.StoreForever',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='block', full_name='charlotte.AvailabilityAttestation.StoreForever.block', index=0,
      number=1, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1884,
  serialized_end=1935,
)

_AVAILABILITYATTESTATION_SIGNEDSTOREFOREVER = _descriptor.Descriptor(
  name='SignedStoreForever',
  full_name='charlotte.AvailabilityAttestation.SignedStoreForever',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='storeForever', full_name='charlotte.AvailabilityAttestation.SignedStoreForever.storeForever', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='signature', full_name='charlotte.AvailabilityAttestation.SignedStoreForever.signature', index=1,
      number=2, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1938,
  serialized_end=2070,
)

_AVAILABILITYATTESTATION = _descriptor.Descriptor(
  name='AvailabilityAttestation',
  full_name='charlotte.AvailabilityAttestation',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='signedStoreForever', full_name='charlotte.AvailabilityAttestation.signedStoreForever', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[_AVAILABILITYATTESTATION_STOREFOREVER, _AVAILABILITYATTESTATION_SIGNEDSTOREFOREVER, ],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
    _descriptor.OneofDescriptor(
      name='availabilityattestationtype_oneof', full_name='charlotte.AvailabilityAttestation.availabilityattestationtype_oneof',
      index=0, containing_type=None, fields=[]),
  ],
  serialized_start=1772,
  serialized_end=2107,
)

_HASH.oneofs_by_name['hashalgorithm_oneof'].fields.append(
  _HASH.fields_by_name['sha3'])
_HASH.fields_by_name['sha3'].containing_oneof = _HASH.oneofs_by_name['hashalgorithm_oneof']
_HASH.oneofs_by_name['hashalgorithm_oneof'].fields.append(
  _HASH.fields_by_name['sha256'])
_HASH.fields_by_name['sha256'].containing_oneof = _HASH.oneofs_by_name['hashalgorithm_oneof']
_PUBLICKEY_ELLIPTICCURVEP256.containing_type = _PUBLICKEY
_PUBLICKEY.fields_by_name['ellipticCurveP256'].message_type = _PUBLICKEY_ELLIPTICCURVEP256
_PUBLICKEY.oneofs_by_name['keyalgorithm_oneof'].fields.append(
  _PUBLICKEY.fields_by_name['ellipticCurveP256'])
_PUBLICKEY.fields_by_name['ellipticCurveP256'].containing_oneof = _PUBLICKEY.oneofs_by_name['keyalgorithm_oneof']
_CRYPTOID.fields_by_name['publicKey'].message_type = _PUBLICKEY
_CRYPTOID.fields_by_name['hash'].message_type = _HASH
_CRYPTOID.oneofs_by_name['idtype_oneof'].fields.append(
  _CRYPTOID.fields_by_name['publicKey'])
_CRYPTOID.fields_by_name['publicKey'].containing_oneof = _CRYPTOID.oneofs_by_name['idtype_oneof']
_CRYPTOID.oneofs_by_name['idtype_oneof'].fields.append(
  _CRYPTOID.fields_by_name['hash'])
_CRYPTOID.fields_by_name['hash'].containing_oneof = _CRYPTOID.oneofs_by_name['idtype_oneof']
_SIGNATURE_SIGNATUREALGORITHMSHA256WITHECDSA.containing_type = _SIGNATURE
_SIGNATURE.fields_by_name['cryptoId'].message_type = _CRYPTOID
_SIGNATURE.fields_by_name['sha256WithEcdsa'].message_type = _SIGNATURE_SIGNATUREALGORITHMSHA256WITHECDSA
_SIGNATURE.oneofs_by_name['signaturealgorithm_oneof'].fields.append(
  _SIGNATURE.fields_by_name['sha256WithEcdsa'])
_SIGNATURE.fields_by_name['sha256WithEcdsa'].containing_oneof = _SIGNATURE.oneofs_by_name['signaturealgorithm_oneof']
_REFERENCE.fields_by_name['hash'].message_type = _HASH
_REFERENCE.fields_by_name['availabilityAttestations'].message_type = _HASH
_REFERENCE.fields_by_name['integrityAttestations'].message_type = _REFERENCE
_INTEGRITYATTESTATION_CHAINSLOT.fields_by_name['block'].message_type = _REFERENCE
_INTEGRITYATTESTATION_CHAINSLOT.fields_by_name['root'].message_type = _REFERENCE
_INTEGRITYATTESTATION_CHAINSLOT.fields_by_name['parent'].message_type = _REFERENCE
_INTEGRITYATTESTATION_CHAINSLOT.containing_type = _INTEGRITYATTESTATION
_INTEGRITYATTESTATION_SIGNEDCHAINSLOT.fields_by_name['chainSlot'].message_type = _INTEGRITYATTESTATION_CHAINSLOT
_INTEGRITYATTESTATION_SIGNEDCHAINSLOT.fields_by_name['signature'].message_type = _SIGNATURE
_INTEGRITYATTESTATION_SIGNEDCHAINSLOT.containing_type = _INTEGRITYATTESTATION
_INTEGRITYATTESTATION_TIMESTAMPEDREFERENCES.fields_by_name['timestamp'].message_type = google_dot_protobuf_dot_timestamp__pb2._TIMESTAMP
_INTEGRITYATTESTATION_TIMESTAMPEDREFERENCES.fields_by_name['block'].message_type = _REFERENCE
_INTEGRITYATTESTATION_TIMESTAMPEDREFERENCES.containing_type = _INTEGRITYATTESTATION
_INTEGRITYATTESTATION_SIGNEDTIMESTAMPEDREFERENCES.fields_by_name['timestampedReferences'].message_type = _INTEGRITYATTESTATION_TIMESTAMPEDREFERENCES
_INTEGRITYATTESTATION_SIGNEDTIMESTAMPEDREFERENCES.fields_by_name['signature'].message_type = _SIGNATURE
_INTEGRITYATTESTATION_SIGNEDTIMESTAMPEDREFERENCES.containing_type = _INTEGRITYATTESTATION
_INTEGRITYATTESTATION_GITSIMBRANCH.fields_by_name['timestamp'].message_type = google_dot_protobuf_dot_timestamp__pb2._TIMESTAMP
_INTEGRITYATTESTATION_GITSIMBRANCH.fields_by_name['commit'].message_type = _REFERENCE
_INTEGRITYATTESTATION_GITSIMBRANCH.containing_type = _INTEGRITYATTESTATION
_INTEGRITYATTESTATION_SIGNEDGITSIMBRANCH.fields_by_name['gitSimBranch'].message_type = _INTEGRITYATTESTATION_GITSIMBRANCH
_INTEGRITYATTESTATION_SIGNEDGITSIMBRANCH.fields_by_name['signature'].message_type = _SIGNATURE
_INTEGRITYATTESTATION_SIGNEDGITSIMBRANCH.containing_type = _INTEGRITYATTESTATION
_INTEGRITYATTESTATION_HETCONSATTESTATION.fields_by_name['observers'].message_type = _CRYPTOID
_INTEGRITYATTESTATION_HETCONSATTESTATION.fields_by_name['message2b'].message_type = _REFERENCE
_INTEGRITYATTESTATION_HETCONSATTESTATION.containing_type = _INTEGRITYATTESTATION
_INTEGRITYATTESTATION.fields_by_name['hetconsAttestation'].message_type = _INTEGRITYATTESTATION_HETCONSATTESTATION
_INTEGRITYATTESTATION.oneofs_by_name['integrityattestationtype_oneof'].fields.append(
  _INTEGRITYATTESTATION.fields_by_name['hetconsAttestation'])
_INTEGRITYATTESTATION.fields_by_name['hetconsAttestation'].containing_oneof = _INTEGRITYATTESTATION.oneofs_by_name['integrityattestationtype_oneof']
_AVAILABILITYATTESTATION_STOREFOREVER.fields_by_name['block'].message_type = _REFERENCE
_AVAILABILITYATTESTATION_STOREFOREVER.containing_type = _AVAILABILITYATTESTATION
_AVAILABILITYATTESTATION_SIGNEDSTOREFOREVER.fields_by_name['storeForever'].message_type = _AVAILABILITYATTESTATION_STOREFOREVER
_AVAILABILITYATTESTATION_SIGNEDSTOREFOREVER.fields_by_name['signature'].message_type = _SIGNATURE
_AVAILABILITYATTESTATION_SIGNEDSTOREFOREVER.containing_type = _AVAILABILITYATTESTATION
_AVAILABILITYATTESTATION.fields_by_name['signedStoreForever'].message_type = _AVAILABILITYATTESTATION_SIGNEDSTOREFOREVER
_AVAILABILITYATTESTATION.oneofs_by_name['availabilityattestationtype_oneof'].fields.append(
  _AVAILABILITYATTESTATION.fields_by_name['signedStoreForever'])
_AVAILABILITYATTESTATION.fields_by_name['signedStoreForever'].containing_oneof = _AVAILABILITYATTESTATION.oneofs_by_name['availabilityattestationtype_oneof']
DESCRIPTOR.message_types_by_name['Hash'] = _HASH
DESCRIPTOR.message_types_by_name['PublicKey'] = _PUBLICKEY
DESCRIPTOR.message_types_by_name['CryptoId'] = _CRYPTOID
DESCRIPTOR.message_types_by_name['Signature'] = _SIGNATURE
DESCRIPTOR.message_types_by_name['Reference'] = _REFERENCE
DESCRIPTOR.message_types_by_name['IntegrityAttestation'] = _INTEGRITYATTESTATION
DESCRIPTOR.message_types_by_name['AvailabilityAttestation'] = _AVAILABILITYATTESTATION

Hash = _reflection.GeneratedProtocolMessageType('Hash', (_message.Message,), dict(
  DESCRIPTOR = _HASH,
  __module__ = 'common_pb2'
  # @@protoc_insertion_point(class_scope:charlotte.Hash)
  ))
_sym_db.RegisterMessage(Hash)

PublicKey = _reflection.GeneratedProtocolMessageType('PublicKey', (_message.Message,), dict(

  EllipticCurveP256 = _reflection.GeneratedProtocolMessageType('EllipticCurveP256', (_message.Message,), dict(
    DESCRIPTOR = _PUBLICKEY_ELLIPTICCURVEP256,
    __module__ = 'common_pb2'
    # @@protoc_insertion_point(class_scope:charlotte.PublicKey.EllipticCurveP256)
    ))
  ,
  DESCRIPTOR = _PUBLICKEY,
  __module__ = 'common_pb2'
  # @@protoc_insertion_point(class_scope:charlotte.PublicKey)
  ))
_sym_db.RegisterMessage(PublicKey)
_sym_db.RegisterMessage(PublicKey.EllipticCurveP256)

CryptoId = _reflection.GeneratedProtocolMessageType('CryptoId', (_message.Message,), dict(
  DESCRIPTOR = _CRYPTOID,
  __module__ = 'common_pb2'
  # @@protoc_insertion_point(class_scope:charlotte.CryptoId)
  ))
_sym_db.RegisterMessage(CryptoId)

Signature = _reflection.GeneratedProtocolMessageType('Signature', (_message.Message,), dict(

  SignatureAlgorithmSHA256WithECDSA = _reflection.GeneratedProtocolMessageType('SignatureAlgorithmSHA256WithECDSA', (_message.Message,), dict(
    DESCRIPTOR = _SIGNATURE_SIGNATUREALGORITHMSHA256WITHECDSA,
    __module__ = 'common_pb2'
    # @@protoc_insertion_point(class_scope:charlotte.Signature.SignatureAlgorithmSHA256WithECDSA)
    ))
  ,
  DESCRIPTOR = _SIGNATURE,
  __module__ = 'common_pb2'
  # @@protoc_insertion_point(class_scope:charlotte.Signature)
  ))
_sym_db.RegisterMessage(Signature)
_sym_db.RegisterMessage(Signature.SignatureAlgorithmSHA256WithECDSA)

Reference = _reflection.GeneratedProtocolMessageType('Reference', (_message.Message,), dict(
  DESCRIPTOR = _REFERENCE,
  __module__ = 'common_pb2'
  # @@protoc_insertion_point(class_scope:charlotte.Reference)
  ))
_sym_db.RegisterMessage(Reference)

IntegrityAttestation = _reflection.GeneratedProtocolMessageType('IntegrityAttestation', (_message.Message,), dict(

  ChainSlot = _reflection.GeneratedProtocolMessageType('ChainSlot', (_message.Message,), dict(
    DESCRIPTOR = _INTEGRITYATTESTATION_CHAINSLOT,
    __module__ = 'common_pb2'
    # @@protoc_insertion_point(class_scope:charlotte.IntegrityAttestation.ChainSlot)
    ))
  ,

  SignedChainSlot = _reflection.GeneratedProtocolMessageType('SignedChainSlot', (_message.Message,), dict(
    DESCRIPTOR = _INTEGRITYATTESTATION_SIGNEDCHAINSLOT,
    __module__ = 'common_pb2'
    # @@protoc_insertion_point(class_scope:charlotte.IntegrityAttestation.SignedChainSlot)
    ))
  ,

  TimestampedReferences = _reflection.GeneratedProtocolMessageType('TimestampedReferences', (_message.Message,), dict(
    DESCRIPTOR = _INTEGRITYATTESTATION_TIMESTAMPEDREFERENCES,
    __module__ = 'common_pb2'
    # @@protoc_insertion_point(class_scope:charlotte.IntegrityAttestation.TimestampedReferences)
    ))
  ,

  SignedTimestampedReferences = _reflection.GeneratedProtocolMessageType('SignedTimestampedReferences', (_message.Message,), dict(
    DESCRIPTOR = _INTEGRITYATTESTATION_SIGNEDTIMESTAMPEDREFERENCES,
    __module__ = 'common_pb2'
    # @@protoc_insertion_point(class_scope:charlotte.IntegrityAttestation.SignedTimestampedReferences)
    ))
  ,

  GitSimBranch = _reflection.GeneratedProtocolMessageType('GitSimBranch', (_message.Message,), dict(
    DESCRIPTOR = _INTEGRITYATTESTATION_GITSIMBRANCH,
    __module__ = 'common_pb2'
    # @@protoc_insertion_point(class_scope:charlotte.IntegrityAttestation.GitSimBranch)
    ))
  ,

  SignedGitSimBranch = _reflection.GeneratedProtocolMessageType('SignedGitSimBranch', (_message.Message,), dict(
    DESCRIPTOR = _INTEGRITYATTESTATION_SIGNEDGITSIMBRANCH,
    __module__ = 'common_pb2'
    # @@protoc_insertion_point(class_scope:charlotte.IntegrityAttestation.SignedGitSimBranch)
    ))
  ,

  HetconsAttestation = _reflection.GeneratedProtocolMessageType('HetconsAttestation', (_message.Message,), dict(
    DESCRIPTOR = _INTEGRITYATTESTATION_HETCONSATTESTATION,
    __module__ = 'common_pb2'
    # @@protoc_insertion_point(class_scope:charlotte.IntegrityAttestation.HetconsAttestation)
    ))
  ,
  DESCRIPTOR = _INTEGRITYATTESTATION,
  __module__ = 'common_pb2'
  # @@protoc_insertion_point(class_scope:charlotte.IntegrityAttestation)
  ))
_sym_db.RegisterMessage(IntegrityAttestation)
_sym_db.RegisterMessage(IntegrityAttestation.ChainSlot)
_sym_db.RegisterMessage(IntegrityAttestation.SignedChainSlot)
_sym_db.RegisterMessage(IntegrityAttestation.TimestampedReferences)
_sym_db.RegisterMessage(IntegrityAttestation.SignedTimestampedReferences)
_sym_db.RegisterMessage(IntegrityAttestation.GitSimBranch)
_sym_db.RegisterMessage(IntegrityAttestation.SignedGitSimBranch)
_sym_db.RegisterMessage(IntegrityAttestation.HetconsAttestation)

AvailabilityAttestation = _reflection.GeneratedProtocolMessageType('AvailabilityAttestation', (_message.Message,), dict(

  StoreForever = _reflection.GeneratedProtocolMessageType('StoreForever', (_message.Message,), dict(
    DESCRIPTOR = _AVAILABILITYATTESTATION_STOREFOREVER,
    __module__ = 'common_pb2'
    # @@protoc_insertion_point(class_scope:charlotte.AvailabilityAttestation.StoreForever)
    ))
  ,

  SignedStoreForever = _reflection.GeneratedProtocolMessageType('SignedStoreForever', (_message.Message,), dict(
    DESCRIPTOR = _AVAILABILITYATTESTATION_SIGNEDSTOREFOREVER,
    __module__ = 'common_pb2'
    # @@protoc_insertion_point(class_scope:charlotte.AvailabilityAttestation.SignedStoreForever)
    ))
  ,
  DESCRIPTOR = _AVAILABILITYATTESTATION,
  __module__ = 'common_pb2'
  # @@protoc_insertion_point(class_scope:charlotte.AvailabilityAttestation)
  ))
_sym_db.RegisterMessage(AvailabilityAttestation)
_sym_db.RegisterMessage(AvailabilityAttestation.StoreForever)
_sym_db.RegisterMessage(AvailabilityAttestation.SignedStoreForever)


DESCRIPTOR.has_options = True
DESCRIPTOR._options = _descriptor._ParseOptions(descriptor_pb2.FileOptions(), _b('\n\036com.isaacsheff.charlotte.protoB\024CharlotteCommonProtoP\001\242\002\020CHARLOTTE_COMMON'))
# @@protoc_insertion_point(module_scope)