// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: hetcons.proto

package com.isaacsheff.charlotte.proto;

/**
 * Protobuf type {@code charlotte.HetconsBallot}
 */
public  final class HetconsBallot extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:charlotte.HetconsBallot)
    HetconsBallotOrBuilder {
private static final long serialVersionUID = 0L;
  // Use HetconsBallot.newBuilder() to construct.
  private HetconsBallot(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private HetconsBallot() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private HetconsBallot(
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
          case 8: {
            ballotOnefoCase_ = 1;
            ballotOnefo_ = input.readInt64();
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();
            ballotOnefoCase_ = 2;
            ballotOnefo_ = s;
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
    return com.isaacsheff.charlotte.proto.HetconsProto.internal_static_charlotte_HetconsBallot_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.isaacsheff.charlotte.proto.HetconsProto.internal_static_charlotte_HetconsBallot_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.isaacsheff.charlotte.proto.HetconsBallot.class, com.isaacsheff.charlotte.proto.HetconsBallot.Builder.class);
  }

  private int ballotOnefoCase_ = 0;
  private java.lang.Object ballotOnefo_;
  public enum BallotOnefoCase
      implements com.google.protobuf.Internal.EnumLite {
    BALLOTNUMBER(1),
    BALLOTSEQUENCE(2),
    BALLOTONEFO_NOT_SET(0);
    private final int value;
    private BallotOnefoCase(int value) {
      this.value = value;
    }
    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static BallotOnefoCase valueOf(int value) {
      return forNumber(value);
    }

    public static BallotOnefoCase forNumber(int value) {
      switch (value) {
        case 1: return BALLOTNUMBER;
        case 2: return BALLOTSEQUENCE;
        case 0: return BALLOTONEFO_NOT_SET;
        default: return null;
      }
    }
    public int getNumber() {
      return this.value;
    }
  };

  public BallotOnefoCase
  getBallotOnefoCase() {
    return BallotOnefoCase.forNumber(
        ballotOnefoCase_);
  }

  public static final int BALLOTNUMBER_FIELD_NUMBER = 1;
  /**
   * <code>int64 ballotNumber = 1;</code>
   */
  public long getBallotNumber() {
    if (ballotOnefoCase_ == 1) {
      return (java.lang.Long) ballotOnefo_;
    }
    return 0L;
  }

  public static final int BALLOTSEQUENCE_FIELD_NUMBER = 2;
  /**
   * <code>string ballotSequence = 2;</code>
   */
  public java.lang.String getBallotSequence() {
    java.lang.Object ref = "";
    if (ballotOnefoCase_ == 2) {
      ref = ballotOnefo_;
    }
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      if (ballotOnefoCase_ == 2) {
        ballotOnefo_ = s;
      }
      return s;
    }
  }
  /**
   * <code>string ballotSequence = 2;</code>
   */
  public com.google.protobuf.ByteString
      getBallotSequenceBytes() {
    java.lang.Object ref = "";
    if (ballotOnefoCase_ == 2) {
      ref = ballotOnefo_;
    }
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      if (ballotOnefoCase_ == 2) {
        ballotOnefo_ = b;
      }
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
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
    if (ballotOnefoCase_ == 1) {
      output.writeInt64(
          1, (long)((java.lang.Long) ballotOnefo_));
    }
    if (ballotOnefoCase_ == 2) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, ballotOnefo_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (ballotOnefoCase_ == 1) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(
            1, (long)((java.lang.Long) ballotOnefo_));
    }
    if (ballotOnefoCase_ == 2) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, ballotOnefo_);
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
    if (!(obj instanceof com.isaacsheff.charlotte.proto.HetconsBallot)) {
      return super.equals(obj);
    }
    com.isaacsheff.charlotte.proto.HetconsBallot other = (com.isaacsheff.charlotte.proto.HetconsBallot) obj;

    boolean result = true;
    result = result && getBallotOnefoCase().equals(
        other.getBallotOnefoCase());
    if (!result) return false;
    switch (ballotOnefoCase_) {
      case 1:
        result = result && (getBallotNumber()
            == other.getBallotNumber());
        break;
      case 2:
        result = result && getBallotSequence()
            .equals(other.getBallotSequence());
        break;
      case 0:
      default:
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
    switch (ballotOnefoCase_) {
      case 1:
        hash = (37 * hash) + BALLOTNUMBER_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
            getBallotNumber());
        break;
      case 2:
        hash = (37 * hash) + BALLOTSEQUENCE_FIELD_NUMBER;
        hash = (53 * hash) + getBallotSequence().hashCode();
        break;
      case 0:
      default:
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.isaacsheff.charlotte.proto.HetconsBallot parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.isaacsheff.charlotte.proto.HetconsBallot parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.HetconsBallot parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.isaacsheff.charlotte.proto.HetconsBallot parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.HetconsBallot parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.isaacsheff.charlotte.proto.HetconsBallot parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.HetconsBallot parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.isaacsheff.charlotte.proto.HetconsBallot parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.HetconsBallot parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.isaacsheff.charlotte.proto.HetconsBallot parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.HetconsBallot parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.isaacsheff.charlotte.proto.HetconsBallot parseFrom(
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
  public static Builder newBuilder(com.isaacsheff.charlotte.proto.HetconsBallot prototype) {
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
   * Protobuf type {@code charlotte.HetconsBallot}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:charlotte.HetconsBallot)
      com.isaacsheff.charlotte.proto.HetconsBallotOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.isaacsheff.charlotte.proto.HetconsProto.internal_static_charlotte_HetconsBallot_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.isaacsheff.charlotte.proto.HetconsProto.internal_static_charlotte_HetconsBallot_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.isaacsheff.charlotte.proto.HetconsBallot.class, com.isaacsheff.charlotte.proto.HetconsBallot.Builder.class);
    }

    // Construct using com.isaacsheff.charlotte.proto.HetconsBallot.newBuilder()
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
      ballotOnefoCase_ = 0;
      ballotOnefo_ = null;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.isaacsheff.charlotte.proto.HetconsProto.internal_static_charlotte_HetconsBallot_descriptor;
    }

    @java.lang.Override
    public com.isaacsheff.charlotte.proto.HetconsBallot getDefaultInstanceForType() {
      return com.isaacsheff.charlotte.proto.HetconsBallot.getDefaultInstance();
    }

    @java.lang.Override
    public com.isaacsheff.charlotte.proto.HetconsBallot build() {
      com.isaacsheff.charlotte.proto.HetconsBallot result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.isaacsheff.charlotte.proto.HetconsBallot buildPartial() {
      com.isaacsheff.charlotte.proto.HetconsBallot result = new com.isaacsheff.charlotte.proto.HetconsBallot(this);
      if (ballotOnefoCase_ == 1) {
        result.ballotOnefo_ = ballotOnefo_;
      }
      if (ballotOnefoCase_ == 2) {
        result.ballotOnefo_ = ballotOnefo_;
      }
      result.ballotOnefoCase_ = ballotOnefoCase_;
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
      if (other instanceof com.isaacsheff.charlotte.proto.HetconsBallot) {
        return mergeFrom((com.isaacsheff.charlotte.proto.HetconsBallot)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.isaacsheff.charlotte.proto.HetconsBallot other) {
      if (other == com.isaacsheff.charlotte.proto.HetconsBallot.getDefaultInstance()) return this;
      switch (other.getBallotOnefoCase()) {
        case BALLOTNUMBER: {
          setBallotNumber(other.getBallotNumber());
          break;
        }
        case BALLOTSEQUENCE: {
          ballotOnefoCase_ = 2;
          ballotOnefo_ = other.ballotOnefo_;
          onChanged();
          break;
        }
        case BALLOTONEFO_NOT_SET: {
          break;
        }
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
      com.isaacsheff.charlotte.proto.HetconsBallot parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.isaacsheff.charlotte.proto.HetconsBallot) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int ballotOnefoCase_ = 0;
    private java.lang.Object ballotOnefo_;
    public BallotOnefoCase
        getBallotOnefoCase() {
      return BallotOnefoCase.forNumber(
          ballotOnefoCase_);
    }

    public Builder clearBallotOnefo() {
      ballotOnefoCase_ = 0;
      ballotOnefo_ = null;
      onChanged();
      return this;
    }


    /**
     * <code>int64 ballotNumber = 1;</code>
     */
    public long getBallotNumber() {
      if (ballotOnefoCase_ == 1) {
        return (java.lang.Long) ballotOnefo_;
      }
      return 0L;
    }
    /**
     * <code>int64 ballotNumber = 1;</code>
     */
    public Builder setBallotNumber(long value) {
      ballotOnefoCase_ = 1;
      ballotOnefo_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 ballotNumber = 1;</code>
     */
    public Builder clearBallotNumber() {
      if (ballotOnefoCase_ == 1) {
        ballotOnefoCase_ = 0;
        ballotOnefo_ = null;
        onChanged();
      }
      return this;
    }

    /**
     * <code>string ballotSequence = 2;</code>
     */
    public java.lang.String getBallotSequence() {
      java.lang.Object ref = "";
      if (ballotOnefoCase_ == 2) {
        ref = ballotOnefo_;
      }
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (ballotOnefoCase_ == 2) {
          ballotOnefo_ = s;
        }
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string ballotSequence = 2;</code>
     */
    public com.google.protobuf.ByteString
        getBallotSequenceBytes() {
      java.lang.Object ref = "";
      if (ballotOnefoCase_ == 2) {
        ref = ballotOnefo_;
      }
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        if (ballotOnefoCase_ == 2) {
          ballotOnefo_ = b;
        }
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string ballotSequence = 2;</code>
     */
    public Builder setBallotSequence(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ballotOnefoCase_ = 2;
      ballotOnefo_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string ballotSequence = 2;</code>
     */
    public Builder clearBallotSequence() {
      if (ballotOnefoCase_ == 2) {
        ballotOnefoCase_ = 0;
        ballotOnefo_ = null;
        onChanged();
      }
      return this;
    }
    /**
     * <code>string ballotSequence = 2;</code>
     */
    public Builder setBallotSequenceBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      ballotOnefoCase_ = 2;
      ballotOnefo_ = value;
      onChanged();
      return this;
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


    // @@protoc_insertion_point(builder_scope:charlotte.HetconsBallot)
  }

  // @@protoc_insertion_point(class_scope:charlotte.HetconsBallot)
  private static final com.isaacsheff.charlotte.proto.HetconsBallot DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.isaacsheff.charlotte.proto.HetconsBallot();
  }

  public static com.isaacsheff.charlotte.proto.HetconsBallot getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<HetconsBallot>
      PARSER = new com.google.protobuf.AbstractParser<HetconsBallot>() {
    @java.lang.Override
    public HetconsBallot parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new HetconsBallot(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<HetconsBallot> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<HetconsBallot> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.isaacsheff.charlotte.proto.HetconsBallot getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

