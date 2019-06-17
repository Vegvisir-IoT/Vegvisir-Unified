// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: vegvisirNetwork.proto

package com.vegvisir.network.datatype.proto;

/**
 * <pre>
 * EndPointConnection
 * </pre>
 *
 * Protobuf type {@code vegvisir.network.datatype.Connection}
 */
public  final class Connection extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:vegvisir.network.datatype.Connection)
    ConnectionOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Connection.newBuilder() to construct.
  private Connection(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Connection() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Connection(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            com.vegvisir.network.datatype.proto.Identifier.Builder subBuilder = null;
            if (remoteId_ != null) {
              subBuilder = remoteId_.toBuilder();
            }
            remoteId_ = input.readMessage(com.vegvisir.network.datatype.proto.Identifier.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(remoteId_);
              remoteId_ = subBuilder.buildPartial();
            }

            break;
          }
          case 18: {
            com.vegvisir.common.datatype.proto.Timestamp.Builder subBuilder = null;
            if (connectedTime_ != null) {
              subBuilder = connectedTime_.toBuilder();
            }
            connectedTime_ = input.readMessage(com.vegvisir.common.datatype.proto.Timestamp.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(connectedTime_);
              connectedTime_ = subBuilder.buildPartial();
            }

            break;
          }
          case 26: {
            com.vegvisir.common.datatype.proto.Timestamp.Builder subBuilder = null;
            if (wakeupTime_ != null) {
              subBuilder = wakeupTime_.toBuilder();
            }
            wakeupTime_ = input.readMessage(com.vegvisir.common.datatype.proto.Timestamp.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(wakeupTime_);
              wakeupTime_ = subBuilder.buildPartial();
            }

            break;
          }
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.vegvisir.network.datatype.proto.VegvisirNetworkDatatypeProto.internal_static_vegvisir_network_datatype_Connection_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.vegvisir.network.datatype.proto.VegvisirNetworkDatatypeProto.internal_static_vegvisir_network_datatype_Connection_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.vegvisir.network.datatype.proto.Connection.class, com.vegvisir.network.datatype.proto.Connection.Builder.class);
  }

  public static final int REMOTE_ID_FIELD_NUMBER = 1;
  private com.vegvisir.network.datatype.proto.Identifier remoteId_;
  /**
   * <code>.vegvisir.network.datatype.Identifier remote_id = 1;</code>
   */
  public boolean hasRemoteId() {
    return remoteId_ != null;
  }
  /**
   * <code>.vegvisir.network.datatype.Identifier remote_id = 1;</code>
   */
  public com.vegvisir.network.datatype.proto.Identifier getRemoteId() {
    return remoteId_ == null ? com.vegvisir.network.datatype.proto.Identifier.getDefaultInstance() : remoteId_;
  }
  /**
   * <code>.vegvisir.network.datatype.Identifier remote_id = 1;</code>
   */
  public com.vegvisir.network.datatype.proto.IdentifierOrBuilder getRemoteIdOrBuilder() {
    return getRemoteId();
  }

  public static final int CONNECTED_TIME_FIELD_NUMBER = 2;
  private com.vegvisir.common.datatype.proto.Timestamp connectedTime_;
  /**
   * <code>.vegvisir.common.datatype.Timestamp connected_time = 2;</code>
   */
  public boolean hasConnectedTime() {
    return connectedTime_ != null;
  }
  /**
   * <code>.vegvisir.common.datatype.Timestamp connected_time = 2;</code>
   */
  public com.vegvisir.common.datatype.proto.Timestamp getConnectedTime() {
    return connectedTime_ == null ? com.vegvisir.common.datatype.proto.Timestamp.getDefaultInstance() : connectedTime_;
  }
  /**
   * <code>.vegvisir.common.datatype.Timestamp connected_time = 2;</code>
   */
  public com.vegvisir.common.datatype.proto.TimestampOrBuilder getConnectedTimeOrBuilder() {
    return getConnectedTime();
  }

  public static final int WAKEUP_TIME_FIELD_NUMBER = 3;
  private com.vegvisir.common.datatype.proto.Timestamp wakeupTime_;
  /**
   * <code>.vegvisir.common.datatype.Timestamp wakeup_time = 3;</code>
   */
  public boolean hasWakeupTime() {
    return wakeupTime_ != null;
  }
  /**
   * <code>.vegvisir.common.datatype.Timestamp wakeup_time = 3;</code>
   */
  public com.vegvisir.common.datatype.proto.Timestamp getWakeupTime() {
    return wakeupTime_ == null ? com.vegvisir.common.datatype.proto.Timestamp.getDefaultInstance() : wakeupTime_;
  }
  /**
   * <code>.vegvisir.common.datatype.Timestamp wakeup_time = 3;</code>
   */
  public com.vegvisir.common.datatype.proto.TimestampOrBuilder getWakeupTimeOrBuilder() {
    return getWakeupTime();
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (remoteId_ != null) {
      output.writeMessage(1, getRemoteId());
    }
    if (connectedTime_ != null) {
      output.writeMessage(2, getConnectedTime());
    }
    if (wakeupTime_ != null) {
      output.writeMessage(3, getWakeupTime());
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (remoteId_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getRemoteId());
    }
    if (connectedTime_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getConnectedTime());
    }
    if (wakeupTime_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, getWakeupTime());
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.vegvisir.network.datatype.proto.Connection)) {
      return super.equals(obj);
    }
    com.vegvisir.network.datatype.proto.Connection other = (com.vegvisir.network.datatype.proto.Connection) obj;

    boolean result = true;
    result = result && (hasRemoteId() == other.hasRemoteId());
    if (hasRemoteId()) {
      result = result && getRemoteId()
          .equals(other.getRemoteId());
    }
    result = result && (hasConnectedTime() == other.hasConnectedTime());
    if (hasConnectedTime()) {
      result = result && getConnectedTime()
          .equals(other.getConnectedTime());
    }
    result = result && (hasWakeupTime() == other.hasWakeupTime());
    if (hasWakeupTime()) {
      result = result && getWakeupTime()
          .equals(other.getWakeupTime());
    }
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasRemoteId()) {
      hash = (37 * hash) + REMOTE_ID_FIELD_NUMBER;
      hash = (53 * hash) + getRemoteId().hashCode();
    }
    if (hasConnectedTime()) {
      hash = (37 * hash) + CONNECTED_TIME_FIELD_NUMBER;
      hash = (53 * hash) + getConnectedTime().hashCode();
    }
    if (hasWakeupTime()) {
      hash = (37 * hash) + WAKEUP_TIME_FIELD_NUMBER;
      hash = (53 * hash) + getWakeupTime().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.vegvisir.network.datatype.proto.Connection parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.vegvisir.network.datatype.proto.Connection parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.vegvisir.network.datatype.proto.Connection parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.vegvisir.network.datatype.proto.Connection parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.vegvisir.network.datatype.proto.Connection parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.vegvisir.network.datatype.proto.Connection parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.vegvisir.network.datatype.proto.Connection parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.vegvisir.network.datatype.proto.Connection parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.vegvisir.network.datatype.proto.Connection parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.vegvisir.network.datatype.proto.Connection parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.vegvisir.network.datatype.proto.Connection parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.vegvisir.network.datatype.proto.Connection parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.vegvisir.network.datatype.proto.Connection prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * EndPointConnection
   * </pre>
   *
   * Protobuf type {@code vegvisir.network.datatype.Connection}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:vegvisir.network.datatype.Connection)
      com.vegvisir.network.datatype.proto.ConnectionOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.vegvisir.network.datatype.proto.VegvisirNetworkDatatypeProto.internal_static_vegvisir_network_datatype_Connection_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.vegvisir.network.datatype.proto.VegvisirNetworkDatatypeProto.internal_static_vegvisir_network_datatype_Connection_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.vegvisir.network.datatype.proto.Connection.class, com.vegvisir.network.datatype.proto.Connection.Builder.class);
    }

    // Construct using com.vegvisir.network.datatype.proto.Connection.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (remoteIdBuilder_ == null) {
        remoteId_ = null;
      } else {
        remoteId_ = null;
        remoteIdBuilder_ = null;
      }
      if (connectedTimeBuilder_ == null) {
        connectedTime_ = null;
      } else {
        connectedTime_ = null;
        connectedTimeBuilder_ = null;
      }
      if (wakeupTimeBuilder_ == null) {
        wakeupTime_ = null;
      } else {
        wakeupTime_ = null;
        wakeupTimeBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.vegvisir.network.datatype.proto.VegvisirNetworkDatatypeProto.internal_static_vegvisir_network_datatype_Connection_descriptor;
    }

    @java.lang.Override
    public com.vegvisir.network.datatype.proto.Connection getDefaultInstanceForType() {
      return com.vegvisir.network.datatype.proto.Connection.getDefaultInstance();
    }

    @java.lang.Override
    public com.vegvisir.network.datatype.proto.Connection build() {
      com.vegvisir.network.datatype.proto.Connection result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.vegvisir.network.datatype.proto.Connection buildPartial() {
      com.vegvisir.network.datatype.proto.Connection result = new com.vegvisir.network.datatype.proto.Connection(this);
      if (remoteIdBuilder_ == null) {
        result.remoteId_ = remoteId_;
      } else {
        result.remoteId_ = remoteIdBuilder_.build();
      }
      if (connectedTimeBuilder_ == null) {
        result.connectedTime_ = connectedTime_;
      } else {
        result.connectedTime_ = connectedTimeBuilder_.build();
      }
      if (wakeupTimeBuilder_ == null) {
        result.wakeupTime_ = wakeupTime_;
      } else {
        result.wakeupTime_ = wakeupTimeBuilder_.build();
      }
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return (Builder) super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.vegvisir.network.datatype.proto.Connection) {
        return mergeFrom((com.vegvisir.network.datatype.proto.Connection)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.vegvisir.network.datatype.proto.Connection other) {
      if (other == com.vegvisir.network.datatype.proto.Connection.getDefaultInstance()) return this;
      if (other.hasRemoteId()) {
        mergeRemoteId(other.getRemoteId());
      }
      if (other.hasConnectedTime()) {
        mergeConnectedTime(other.getConnectedTime());
      }
      if (other.hasWakeupTime()) {
        mergeWakeupTime(other.getWakeupTime());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.vegvisir.network.datatype.proto.Connection parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.vegvisir.network.datatype.proto.Connection) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private com.vegvisir.network.datatype.proto.Identifier remoteId_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.vegvisir.network.datatype.proto.Identifier, com.vegvisir.network.datatype.proto.Identifier.Builder, com.vegvisir.network.datatype.proto.IdentifierOrBuilder> remoteIdBuilder_;
    /**
     * <code>.vegvisir.network.datatype.Identifier remote_id = 1;</code>
     */
    public boolean hasRemoteId() {
      return remoteIdBuilder_ != null || remoteId_ != null;
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier remote_id = 1;</code>
     */
    public com.vegvisir.network.datatype.proto.Identifier getRemoteId() {
      if (remoteIdBuilder_ == null) {
        return remoteId_ == null ? com.vegvisir.network.datatype.proto.Identifier.getDefaultInstance() : remoteId_;
      } else {
        return remoteIdBuilder_.getMessage();
      }
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier remote_id = 1;</code>
     */
    public Builder setRemoteId(com.vegvisir.network.datatype.proto.Identifier value) {
      if (remoteIdBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        remoteId_ = value;
        onChanged();
      } else {
        remoteIdBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier remote_id = 1;</code>
     */
    public Builder setRemoteId(
        com.vegvisir.network.datatype.proto.Identifier.Builder builderForValue) {
      if (remoteIdBuilder_ == null) {
        remoteId_ = builderForValue.build();
        onChanged();
      } else {
        remoteIdBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier remote_id = 1;</code>
     */
    public Builder mergeRemoteId(com.vegvisir.network.datatype.proto.Identifier value) {
      if (remoteIdBuilder_ == null) {
        if (remoteId_ != null) {
          remoteId_ =
            com.vegvisir.network.datatype.proto.Identifier.newBuilder(remoteId_).mergeFrom(value).buildPartial();
        } else {
          remoteId_ = value;
        }
        onChanged();
      } else {
        remoteIdBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier remote_id = 1;</code>
     */
    public Builder clearRemoteId() {
      if (remoteIdBuilder_ == null) {
        remoteId_ = null;
        onChanged();
      } else {
        remoteId_ = null;
        remoteIdBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier remote_id = 1;</code>
     */
    public com.vegvisir.network.datatype.proto.Identifier.Builder getRemoteIdBuilder() {
      
      onChanged();
      return getRemoteIdFieldBuilder().getBuilder();
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier remote_id = 1;</code>
     */
    public com.vegvisir.network.datatype.proto.IdentifierOrBuilder getRemoteIdOrBuilder() {
      if (remoteIdBuilder_ != null) {
        return remoteIdBuilder_.getMessageOrBuilder();
      } else {
        return remoteId_ == null ?
            com.vegvisir.network.datatype.proto.Identifier.getDefaultInstance() : remoteId_;
      }
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier remote_id = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.vegvisir.network.datatype.proto.Identifier, com.vegvisir.network.datatype.proto.Identifier.Builder, com.vegvisir.network.datatype.proto.IdentifierOrBuilder> 
        getRemoteIdFieldBuilder() {
      if (remoteIdBuilder_ == null) {
        remoteIdBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.vegvisir.network.datatype.proto.Identifier, com.vegvisir.network.datatype.proto.Identifier.Builder, com.vegvisir.network.datatype.proto.IdentifierOrBuilder>(
                getRemoteId(),
                getParentForChildren(),
                isClean());
        remoteId_ = null;
      }
      return remoteIdBuilder_;
    }

    private com.vegvisir.common.datatype.proto.Timestamp connectedTime_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.vegvisir.common.datatype.proto.Timestamp, com.vegvisir.common.datatype.proto.Timestamp.Builder, com.vegvisir.common.datatype.proto.TimestampOrBuilder> connectedTimeBuilder_;
    /**
     * <code>.vegvisir.common.datatype.Timestamp connected_time = 2;</code>
     */
    public boolean hasConnectedTime() {
      return connectedTimeBuilder_ != null || connectedTime_ != null;
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp connected_time = 2;</code>
     */
    public com.vegvisir.common.datatype.proto.Timestamp getConnectedTime() {
      if (connectedTimeBuilder_ == null) {
        return connectedTime_ == null ? com.vegvisir.common.datatype.proto.Timestamp.getDefaultInstance() : connectedTime_;
      } else {
        return connectedTimeBuilder_.getMessage();
      }
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp connected_time = 2;</code>
     */
    public Builder setConnectedTime(com.vegvisir.common.datatype.proto.Timestamp value) {
      if (connectedTimeBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        connectedTime_ = value;
        onChanged();
      } else {
        connectedTimeBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp connected_time = 2;</code>
     */
    public Builder setConnectedTime(
        com.vegvisir.common.datatype.proto.Timestamp.Builder builderForValue) {
      if (connectedTimeBuilder_ == null) {
        connectedTime_ = builderForValue.build();
        onChanged();
      } else {
        connectedTimeBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp connected_time = 2;</code>
     */
    public Builder mergeConnectedTime(com.vegvisir.common.datatype.proto.Timestamp value) {
      if (connectedTimeBuilder_ == null) {
        if (connectedTime_ != null) {
          connectedTime_ =
            com.vegvisir.common.datatype.proto.Timestamp.newBuilder(connectedTime_).mergeFrom(value).buildPartial();
        } else {
          connectedTime_ = value;
        }
        onChanged();
      } else {
        connectedTimeBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp connected_time = 2;</code>
     */
    public Builder clearConnectedTime() {
      if (connectedTimeBuilder_ == null) {
        connectedTime_ = null;
        onChanged();
      } else {
        connectedTime_ = null;
        connectedTimeBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp connected_time = 2;</code>
     */
    public com.vegvisir.common.datatype.proto.Timestamp.Builder getConnectedTimeBuilder() {
      
      onChanged();
      return getConnectedTimeFieldBuilder().getBuilder();
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp connected_time = 2;</code>
     */
    public com.vegvisir.common.datatype.proto.TimestampOrBuilder getConnectedTimeOrBuilder() {
      if (connectedTimeBuilder_ != null) {
        return connectedTimeBuilder_.getMessageOrBuilder();
      } else {
        return connectedTime_ == null ?
            com.vegvisir.common.datatype.proto.Timestamp.getDefaultInstance() : connectedTime_;
      }
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp connected_time = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.vegvisir.common.datatype.proto.Timestamp, com.vegvisir.common.datatype.proto.Timestamp.Builder, com.vegvisir.common.datatype.proto.TimestampOrBuilder> 
        getConnectedTimeFieldBuilder() {
      if (connectedTimeBuilder_ == null) {
        connectedTimeBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.vegvisir.common.datatype.proto.Timestamp, com.vegvisir.common.datatype.proto.Timestamp.Builder, com.vegvisir.common.datatype.proto.TimestampOrBuilder>(
                getConnectedTime(),
                getParentForChildren(),
                isClean());
        connectedTime_ = null;
      }
      return connectedTimeBuilder_;
    }

    private com.vegvisir.common.datatype.proto.Timestamp wakeupTime_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.vegvisir.common.datatype.proto.Timestamp, com.vegvisir.common.datatype.proto.Timestamp.Builder, com.vegvisir.common.datatype.proto.TimestampOrBuilder> wakeupTimeBuilder_;
    /**
     * <code>.vegvisir.common.datatype.Timestamp wakeup_time = 3;</code>
     */
    public boolean hasWakeupTime() {
      return wakeupTimeBuilder_ != null || wakeupTime_ != null;
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp wakeup_time = 3;</code>
     */
    public com.vegvisir.common.datatype.proto.Timestamp getWakeupTime() {
      if (wakeupTimeBuilder_ == null) {
        return wakeupTime_ == null ? com.vegvisir.common.datatype.proto.Timestamp.getDefaultInstance() : wakeupTime_;
      } else {
        return wakeupTimeBuilder_.getMessage();
      }
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp wakeup_time = 3;</code>
     */
    public Builder setWakeupTime(com.vegvisir.common.datatype.proto.Timestamp value) {
      if (wakeupTimeBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        wakeupTime_ = value;
        onChanged();
      } else {
        wakeupTimeBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp wakeup_time = 3;</code>
     */
    public Builder setWakeupTime(
        com.vegvisir.common.datatype.proto.Timestamp.Builder builderForValue) {
      if (wakeupTimeBuilder_ == null) {
        wakeupTime_ = builderForValue.build();
        onChanged();
      } else {
        wakeupTimeBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp wakeup_time = 3;</code>
     */
    public Builder mergeWakeupTime(com.vegvisir.common.datatype.proto.Timestamp value) {
      if (wakeupTimeBuilder_ == null) {
        if (wakeupTime_ != null) {
          wakeupTime_ =
            com.vegvisir.common.datatype.proto.Timestamp.newBuilder(wakeupTime_).mergeFrom(value).buildPartial();
        } else {
          wakeupTime_ = value;
        }
        onChanged();
      } else {
        wakeupTimeBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp wakeup_time = 3;</code>
     */
    public Builder clearWakeupTime() {
      if (wakeupTimeBuilder_ == null) {
        wakeupTime_ = null;
        onChanged();
      } else {
        wakeupTime_ = null;
        wakeupTimeBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp wakeup_time = 3;</code>
     */
    public com.vegvisir.common.datatype.proto.Timestamp.Builder getWakeupTimeBuilder() {
      
      onChanged();
      return getWakeupTimeFieldBuilder().getBuilder();
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp wakeup_time = 3;</code>
     */
    public com.vegvisir.common.datatype.proto.TimestampOrBuilder getWakeupTimeOrBuilder() {
      if (wakeupTimeBuilder_ != null) {
        return wakeupTimeBuilder_.getMessageOrBuilder();
      } else {
        return wakeupTime_ == null ?
            com.vegvisir.common.datatype.proto.Timestamp.getDefaultInstance() : wakeupTime_;
      }
    }
    /**
     * <code>.vegvisir.common.datatype.Timestamp wakeup_time = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.vegvisir.common.datatype.proto.Timestamp, com.vegvisir.common.datatype.proto.Timestamp.Builder, com.vegvisir.common.datatype.proto.TimestampOrBuilder> 
        getWakeupTimeFieldBuilder() {
      if (wakeupTimeBuilder_ == null) {
        wakeupTimeBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.vegvisir.common.datatype.proto.Timestamp, com.vegvisir.common.datatype.proto.Timestamp.Builder, com.vegvisir.common.datatype.proto.TimestampOrBuilder>(
                getWakeupTime(),
                getParentForChildren(),
                isClean());
        wakeupTime_ = null;
      }
      return wakeupTimeBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:vegvisir.network.datatype.Connection)
  }

  // @@protoc_insertion_point(class_scope:vegvisir.network.datatype.Connection)
  private static final com.vegvisir.network.datatype.proto.Connection DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.vegvisir.network.datatype.proto.Connection();
  }

  public static com.vegvisir.network.datatype.proto.Connection getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Connection>
      PARSER = new com.google.protobuf.AbstractParser<Connection>() {
    @java.lang.Override
    public Connection parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Connection(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Connection> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Connection> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.vegvisir.network.datatype.proto.Connection getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

