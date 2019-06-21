// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: vegvisirNetwork.proto

package com.vegvisir.network.datatype.proto;

/**
 * Protobuf type {@code vegvisir.network.datatype.Peer}
 */
public  final class Peer extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:vegvisir.network.datatype.Peer)
    PeerOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Peer.newBuilder() to construct.
  private Peer(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Peer() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Peer(
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
            if (id_ != null) {
              subBuilder = id_.toBuilder();
            }
            id_ = input.readMessage(com.vegvisir.network.datatype.proto.Identifier.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(id_);
              id_ = subBuilder.buildPartial();
            }

            break;
          }
          case 18: {
            com.vegvisir.network.datatype.proto.Connection.Builder subBuilder = null;
            if (conn_ != null) {
              subBuilder = conn_.toBuilder();
            }
            conn_ = input.readMessage(com.vegvisir.network.datatype.proto.Connection.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(conn_);
              conn_ = subBuilder.buildPartial();
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
    return com.vegvisir.network.datatype.proto.VegvisirNetworkDatatypeProto.internal_static_vegvisir_network_datatype_Peer_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.vegvisir.network.datatype.proto.VegvisirNetworkDatatypeProto.internal_static_vegvisir_network_datatype_Peer_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.vegvisir.network.datatype.proto.Peer.class, com.vegvisir.network.datatype.proto.Peer.Builder.class);
  }

  public static final int ID_FIELD_NUMBER = 1;
  private com.vegvisir.network.datatype.proto.Identifier id_;
  /**
   * <code>.vegvisir.network.datatype.Identifier id = 1;</code>
   */
  public boolean hasId() {
    return id_ != null;
  }
  /**
   * <code>.vegvisir.network.datatype.Identifier id = 1;</code>
   */
  public com.vegvisir.network.datatype.proto.Identifier getId() {
    return id_ == null ? com.vegvisir.network.datatype.proto.Identifier.getDefaultInstance() : id_;
  }
  /**
   * <code>.vegvisir.network.datatype.Identifier id = 1;</code>
   */
  public com.vegvisir.network.datatype.proto.IdentifierOrBuilder getIdOrBuilder() {
    return getId();
  }

  public static final int CONN_FIELD_NUMBER = 2;
  private com.vegvisir.network.datatype.proto.Connection conn_;
  /**
   * <code>.vegvisir.network.datatype.Connection conn = 2;</code>
   */
  public boolean hasConn() {
    return conn_ != null;
  }
  /**
   * <code>.vegvisir.network.datatype.Connection conn = 2;</code>
   */
  public com.vegvisir.network.datatype.proto.Connection getConn() {
    return conn_ == null ? com.vegvisir.network.datatype.proto.Connection.getDefaultInstance() : conn_;
  }
  /**
   * <code>.vegvisir.network.datatype.Connection conn = 2;</code>
   */
  public com.vegvisir.network.datatype.proto.ConnectionOrBuilder getConnOrBuilder() {
    return getConn();
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
    if (id_ != null) {
      output.writeMessage(1, getId());
    }
    if (conn_ != null) {
      output.writeMessage(2, getConn());
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (id_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getId());
    }
    if (conn_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getConn());
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
    if (!(obj instanceof com.vegvisir.network.datatype.proto.Peer)) {
      return super.equals(obj);
    }
    com.vegvisir.network.datatype.proto.Peer other = (com.vegvisir.network.datatype.proto.Peer) obj;

    boolean result = true;
    result = result && (hasId() == other.hasId());
    if (hasId()) {
      result = result && getId()
          .equals(other.getId());
    }
    result = result && (hasConn() == other.hasConn());
    if (hasConn()) {
      result = result && getConn()
          .equals(other.getConn());
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
    if (hasId()) {
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + getId().hashCode();
    }
    if (hasConn()) {
      hash = (37 * hash) + CONN_FIELD_NUMBER;
      hash = (53 * hash) + getConn().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.vegvisir.network.datatype.proto.Peer parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.vegvisir.network.datatype.proto.Peer parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.vegvisir.network.datatype.proto.Peer parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.vegvisir.network.datatype.proto.Peer parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.vegvisir.network.datatype.proto.Peer parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.vegvisir.network.datatype.proto.Peer parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.vegvisir.network.datatype.proto.Peer parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.vegvisir.network.datatype.proto.Peer parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.vegvisir.network.datatype.proto.Peer parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.vegvisir.network.datatype.proto.Peer parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.vegvisir.network.datatype.proto.Peer parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.vegvisir.network.datatype.proto.Peer parseFrom(
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
  public static Builder newBuilder(com.vegvisir.network.datatype.proto.Peer prototype) {
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
   * Protobuf type {@code vegvisir.network.datatype.Peer}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:vegvisir.network.datatype.Peer)
      com.vegvisir.network.datatype.proto.PeerOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.vegvisir.network.datatype.proto.VegvisirNetworkDatatypeProto.internal_static_vegvisir_network_datatype_Peer_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.vegvisir.network.datatype.proto.VegvisirNetworkDatatypeProto.internal_static_vegvisir_network_datatype_Peer_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.vegvisir.network.datatype.proto.Peer.class, com.vegvisir.network.datatype.proto.Peer.Builder.class);
    }

    // Construct using com.vegvisir.network.datatype.proto.Peer.newBuilder()
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
      if (idBuilder_ == null) {
        id_ = null;
      } else {
        id_ = null;
        idBuilder_ = null;
      }
      if (connBuilder_ == null) {
        conn_ = null;
      } else {
        conn_ = null;
        connBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.vegvisir.network.datatype.proto.VegvisirNetworkDatatypeProto.internal_static_vegvisir_network_datatype_Peer_descriptor;
    }

    @java.lang.Override
    public com.vegvisir.network.datatype.proto.Peer getDefaultInstanceForType() {
      return com.vegvisir.network.datatype.proto.Peer.getDefaultInstance();
    }

    @java.lang.Override
    public com.vegvisir.network.datatype.proto.Peer build() {
      com.vegvisir.network.datatype.proto.Peer result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.vegvisir.network.datatype.proto.Peer buildPartial() {
      com.vegvisir.network.datatype.proto.Peer result = new com.vegvisir.network.datatype.proto.Peer(this);
      if (idBuilder_ == null) {
        result.id_ = id_;
      } else {
        result.id_ = idBuilder_.build();
      }
      if (connBuilder_ == null) {
        result.conn_ = conn_;
      } else {
        result.conn_ = connBuilder_.build();
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
      if (other instanceof com.vegvisir.network.datatype.proto.Peer) {
        return mergeFrom((com.vegvisir.network.datatype.proto.Peer)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.vegvisir.network.datatype.proto.Peer other) {
      if (other == com.vegvisir.network.datatype.proto.Peer.getDefaultInstance()) return this;
      if (other.hasId()) {
        mergeId(other.getId());
      }
      if (other.hasConn()) {
        mergeConn(other.getConn());
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
      com.vegvisir.network.datatype.proto.Peer parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.vegvisir.network.datatype.proto.Peer) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private com.vegvisir.network.datatype.proto.Identifier id_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.vegvisir.network.datatype.proto.Identifier, com.vegvisir.network.datatype.proto.Identifier.Builder, com.vegvisir.network.datatype.proto.IdentifierOrBuilder> idBuilder_;
    /**
     * <code>.vegvisir.network.datatype.Identifier id = 1;</code>
     */
    public boolean hasId() {
      return idBuilder_ != null || id_ != null;
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier id = 1;</code>
     */
    public com.vegvisir.network.datatype.proto.Identifier getId() {
      if (idBuilder_ == null) {
        return id_ == null ? com.vegvisir.network.datatype.proto.Identifier.getDefaultInstance() : id_;
      } else {
        return idBuilder_.getMessage();
      }
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier id = 1;</code>
     */
    public Builder setId(com.vegvisir.network.datatype.proto.Identifier value) {
      if (idBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        id_ = value;
        onChanged();
      } else {
        idBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier id = 1;</code>
     */
    public Builder setId(
        com.vegvisir.network.datatype.proto.Identifier.Builder builderForValue) {
      if (idBuilder_ == null) {
        id_ = builderForValue.build();
        onChanged();
      } else {
        idBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier id = 1;</code>
     */
    public Builder mergeId(com.vegvisir.network.datatype.proto.Identifier value) {
      if (idBuilder_ == null) {
        if (id_ != null) {
          id_ =
            com.vegvisir.network.datatype.proto.Identifier.newBuilder(id_).mergeFrom(value).buildPartial();
        } else {
          id_ = value;
        }
        onChanged();
      } else {
        idBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier id = 1;</code>
     */
    public Builder clearId() {
      if (idBuilder_ == null) {
        id_ = null;
        onChanged();
      } else {
        id_ = null;
        idBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier id = 1;</code>
     */
    public com.vegvisir.network.datatype.proto.Identifier.Builder getIdBuilder() {
      
      onChanged();
      return getIdFieldBuilder().getBuilder();
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier id = 1;</code>
     */
    public com.vegvisir.network.datatype.proto.IdentifierOrBuilder getIdOrBuilder() {
      if (idBuilder_ != null) {
        return idBuilder_.getMessageOrBuilder();
      } else {
        return id_ == null ?
            com.vegvisir.network.datatype.proto.Identifier.getDefaultInstance() : id_;
      }
    }
    /**
     * <code>.vegvisir.network.datatype.Identifier id = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.vegvisir.network.datatype.proto.Identifier, com.vegvisir.network.datatype.proto.Identifier.Builder, com.vegvisir.network.datatype.proto.IdentifierOrBuilder> 
        getIdFieldBuilder() {
      if (idBuilder_ == null) {
        idBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.vegvisir.network.datatype.proto.Identifier, com.vegvisir.network.datatype.proto.Identifier.Builder, com.vegvisir.network.datatype.proto.IdentifierOrBuilder>(
                getId(),
                getParentForChildren(),
                isClean());
        id_ = null;
      }
      return idBuilder_;
    }

    private com.vegvisir.network.datatype.proto.Connection conn_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.vegvisir.network.datatype.proto.Connection, com.vegvisir.network.datatype.proto.Connection.Builder, com.vegvisir.network.datatype.proto.ConnectionOrBuilder> connBuilder_;
    /**
     * <code>.vegvisir.network.datatype.Connection conn = 2;</code>
     */
    public boolean hasConn() {
      return connBuilder_ != null || conn_ != null;
    }
    /**
     * <code>.vegvisir.network.datatype.Connection conn = 2;</code>
     */
    public com.vegvisir.network.datatype.proto.Connection getConn() {
      if (connBuilder_ == null) {
        return conn_ == null ? com.vegvisir.network.datatype.proto.Connection.getDefaultInstance() : conn_;
      } else {
        return connBuilder_.getMessage();
      }
    }
    /**
     * <code>.vegvisir.network.datatype.Connection conn = 2;</code>
     */
    public Builder setConn(com.vegvisir.network.datatype.proto.Connection value) {
      if (connBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        conn_ = value;
        onChanged();
      } else {
        connBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.vegvisir.network.datatype.Connection conn = 2;</code>
     */
    public Builder setConn(
        com.vegvisir.network.datatype.proto.Connection.Builder builderForValue) {
      if (connBuilder_ == null) {
        conn_ = builderForValue.build();
        onChanged();
      } else {
        connBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.vegvisir.network.datatype.Connection conn = 2;</code>
     */
    public Builder mergeConn(com.vegvisir.network.datatype.proto.Connection value) {
      if (connBuilder_ == null) {
        if (conn_ != null) {
          conn_ =
            com.vegvisir.network.datatype.proto.Connection.newBuilder(conn_).mergeFrom(value).buildPartial();
        } else {
          conn_ = value;
        }
        onChanged();
      } else {
        connBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.vegvisir.network.datatype.Connection conn = 2;</code>
     */
    public Builder clearConn() {
      if (connBuilder_ == null) {
        conn_ = null;
        onChanged();
      } else {
        conn_ = null;
        connBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.vegvisir.network.datatype.Connection conn = 2;</code>
     */
    public com.vegvisir.network.datatype.proto.Connection.Builder getConnBuilder() {
      
      onChanged();
      return getConnFieldBuilder().getBuilder();
    }
    /**
     * <code>.vegvisir.network.datatype.Connection conn = 2;</code>
     */
    public com.vegvisir.network.datatype.proto.ConnectionOrBuilder getConnOrBuilder() {
      if (connBuilder_ != null) {
        return connBuilder_.getMessageOrBuilder();
      } else {
        return conn_ == null ?
            com.vegvisir.network.datatype.proto.Connection.getDefaultInstance() : conn_;
      }
    }
    /**
     * <code>.vegvisir.network.datatype.Connection conn = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.vegvisir.network.datatype.proto.Connection, com.vegvisir.network.datatype.proto.Connection.Builder, com.vegvisir.network.datatype.proto.ConnectionOrBuilder> 
        getConnFieldBuilder() {
      if (connBuilder_ == null) {
        connBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.vegvisir.network.datatype.proto.Connection, com.vegvisir.network.datatype.proto.Connection.Builder, com.vegvisir.network.datatype.proto.ConnectionOrBuilder>(
                getConn(),
                getParentForChildren(),
                isClean());
        conn_ = null;
      }
      return connBuilder_;
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


    // @@protoc_insertion_point(builder_scope:vegvisir.network.datatype.Peer)
  }

  // @@protoc_insertion_point(class_scope:vegvisir.network.datatype.Peer)
  private static final com.vegvisir.network.datatype.proto.Peer DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.vegvisir.network.datatype.proto.Peer();
  }

  public static com.vegvisir.network.datatype.proto.Peer getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Peer>
      PARSER = new com.google.protobuf.AbstractParser<Peer>() {
    @java.lang.Override
    public Peer parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Peer(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Peer> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Peer> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.vegvisir.network.datatype.proto.Peer getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

