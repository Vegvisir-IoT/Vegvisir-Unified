# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: sendall.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


import vegvisirCommon_pb2 as vegvisirCommon__pb2


DESCRIPTOR = _descriptor.FileDescriptor(
  name='sendall.proto',
  package='vegvisir.protocol.datatype',
  syntax='proto3',
  serialized_pb=_b('\n\rsendall.proto\x12\x1avegvisir.protocol.datatype\x1a\x14vegvisirCommon.proto\"B\n\x0eSendallMessage\x12\x30\n\x03\x61\x64\x64\x18\x02 \x01(\x0b\x32#.vegvisir.common.datatype.AddBlocksb\x06proto3')
  ,
  dependencies=[vegvisirCommon__pb2.DESCRIPTOR,])
_sym_db.RegisterFileDescriptor(DESCRIPTOR)




_SENDALLMESSAGE = _descriptor.Descriptor(
  name='SendallMessage',
  full_name='vegvisir.protocol.datatype.SendallMessage',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='add', full_name='vegvisir.protocol.datatype.SendallMessage.add', index=0,
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
  serialized_start=67,
  serialized_end=133,
)

_SENDALLMESSAGE.fields_by_name['add'].message_type = vegvisirCommon__pb2._ADDBLOCKS
DESCRIPTOR.message_types_by_name['SendallMessage'] = _SENDALLMESSAGE

SendallMessage = _reflection.GeneratedProtocolMessageType('SendallMessage', (_message.Message,), dict(
  DESCRIPTOR = _SENDALLMESSAGE,
  __module__ = 'sendall_pb2'
  # @@protoc_insertion_point(class_scope:vegvisir.protocol.datatype.SendallMessage)
  ))
_sym_db.RegisterMessage(SendallMessage)


# @@protoc_insertion_point(module_scope)
