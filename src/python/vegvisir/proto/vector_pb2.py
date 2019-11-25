# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: vector.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


from . import vegvisirCommon_pb2 as vegvisirCommon__pb2


DESCRIPTOR = _descriptor.FileDescriptor(
  name='vector.proto',
  package='vegvisir.proto',
  syntax='proto3',
  serialized_pb=_b('\n\x0cvector.proto\x12\x0evegvisir.proto\x1a\x14vegvisirCommon.proto\"\x98\x01\n\x0bVectorClock\x12\x37\n\x06\x63locks\x18\x01 \x03(\x0b\x32\'.vegvisir.proto.VectorClock.ClocksEntry\x12\x11\n\tsendLimit\x18\x02 \x01(\x05\x12\x0e\n\x06userid\x18\x04 \x01(\t\x1a-\n\x0b\x43locksEntry\x12\x0b\n\x03key\x18\x01 \x01(\t\x12\r\n\x05value\x18\x02 \x01(\x03:\x02\x38\x01\"\xcb\x01\n\rVectorMessage\x12\x30\n\tworldView\x18\x01 \x01(\x0b\x32\x1b.vegvisir.proto.VectorClockH\x00\x12(\n\x03\x61\x64\x64\x18\x02 \x01(\x0b\x32\x19.vegvisir.proto.AddBlocksH\x00\x12\x11\n\tsignature\x18\x03 \x01(\x0c\x12\x34\n\x0f\x61llVectorClocks\x18\x04 \x03(\x0b\x32\x1b.vegvisir.proto.VectorClockB\x15\n\x13vector_message_typeb\x06proto3')
  ,
  dependencies=[vegvisirCommon__pb2.DESCRIPTOR,])
_sym_db.RegisterFileDescriptor(DESCRIPTOR)




_VECTORCLOCK_CLOCKSENTRY = _descriptor.Descriptor(
  name='ClocksEntry',
  full_name='vegvisir.proto.VectorClock.ClocksEntry',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='key', full_name='vegvisir.proto.VectorClock.ClocksEntry.key', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='value', full_name='vegvisir.proto.VectorClock.ClocksEntry.value', index=1,
      number=2, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=_descriptor._ParseOptions(descriptor_pb2.MessageOptions(), _b('8\001')),
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=162,
  serialized_end=207,
)

_VECTORCLOCK = _descriptor.Descriptor(
  name='VectorClock',
  full_name='vegvisir.proto.VectorClock',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='clocks', full_name='vegvisir.proto.VectorClock.clocks', index=0,
      number=1, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='sendLimit', full_name='vegvisir.proto.VectorClock.sendLimit', index=1,
      number=2, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='userid', full_name='vegvisir.proto.VectorClock.userid', index=2,
      number=4, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[_VECTORCLOCK_CLOCKSENTRY, ],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=55,
  serialized_end=207,
)


_VECTORMESSAGE = _descriptor.Descriptor(
  name='VectorMessage',
  full_name='vegvisir.proto.VectorMessage',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='worldView', full_name='vegvisir.proto.VectorMessage.worldView', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='add', full_name='vegvisir.proto.VectorMessage.add', index=1,
      number=2, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='signature', full_name='vegvisir.proto.VectorMessage.signature', index=2,
      number=3, type=12, cpp_type=9, label=1,
      has_default_value=False, default_value=_b(""),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='allVectorClocks', full_name='vegvisir.proto.VectorMessage.allVectorClocks', index=3,
      number=4, type=11, cpp_type=10, label=3,
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
    _descriptor.OneofDescriptor(
      name='vector_message_type', full_name='vegvisir.proto.VectorMessage.vector_message_type',
      index=0, containing_type=None, fields=[]),
  ],
  serialized_start=210,
  serialized_end=413,
)

_VECTORCLOCK_CLOCKSENTRY.containing_type = _VECTORCLOCK
_VECTORCLOCK.fields_by_name['clocks'].message_type = _VECTORCLOCK_CLOCKSENTRY
_VECTORMESSAGE.fields_by_name['worldView'].message_type = _VECTORCLOCK
_VECTORMESSAGE.fields_by_name['add'].message_type = vegvisirCommon__pb2._ADDBLOCKS
_VECTORMESSAGE.fields_by_name['allVectorClocks'].message_type = _VECTORCLOCK
_VECTORMESSAGE.oneofs_by_name['vector_message_type'].fields.append(
  _VECTORMESSAGE.fields_by_name['worldView'])
_VECTORMESSAGE.fields_by_name['worldView'].containing_oneof = _VECTORMESSAGE.oneofs_by_name['vector_message_type']
_VECTORMESSAGE.oneofs_by_name['vector_message_type'].fields.append(
  _VECTORMESSAGE.fields_by_name['add'])
_VECTORMESSAGE.fields_by_name['add'].containing_oneof = _VECTORMESSAGE.oneofs_by_name['vector_message_type']
DESCRIPTOR.message_types_by_name['VectorClock'] = _VECTORCLOCK
DESCRIPTOR.message_types_by_name['VectorMessage'] = _VECTORMESSAGE

VectorClock = _reflection.GeneratedProtocolMessageType('VectorClock', (_message.Message,), dict(

  ClocksEntry = _reflection.GeneratedProtocolMessageType('ClocksEntry', (_message.Message,), dict(
    DESCRIPTOR = _VECTORCLOCK_CLOCKSENTRY,
    __module__ = 'vector_pb2'
    # @@protoc_insertion_point(class_scope:vegvisir.proto.VectorClock.ClocksEntry)
    ))
  ,
  DESCRIPTOR = _VECTORCLOCK,
  __module__ = 'vector_pb2'
  # @@protoc_insertion_point(class_scope:vegvisir.proto.VectorClock)
  ))
_sym_db.RegisterMessage(VectorClock)
_sym_db.RegisterMessage(VectorClock.ClocksEntry)

VectorMessage = _reflection.GeneratedProtocolMessageType('VectorMessage', (_message.Message,), dict(
  DESCRIPTOR = _VECTORMESSAGE,
  __module__ = 'vector_pb2'
  # @@protoc_insertion_point(class_scope:vegvisir.proto.VectorMessage)
  ))
_sym_db.RegisterMessage(VectorMessage)


_VECTORCLOCK_CLOCKSENTRY.has_options = True
_VECTORCLOCK_CLOCKSENTRY._options = _descriptor._ParseOptions(descriptor_pb2.MessageOptions(), _b('8\001'))
# @@protoc_insertion_point(module_scope)