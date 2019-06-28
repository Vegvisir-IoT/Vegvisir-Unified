// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: charlotte.proto

package com.isaacsheff.charlotte.proto;

/**
 * Protobuf type {@code charlotte.WilburQueryInput}
 */
public  final class WilburQueryInput extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:charlotte.WilburQueryInput)
    WilburQueryInputOrBuilder {
private static final long serialVersionUID = 0L;
  // Use WilburQueryInput.newBuilder() to construct.
  private WilburQueryInput(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private WilburQueryInput() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private WilburQueryInput(
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
            com.isaacsheff.charlotte.proto.Reference.Builder subBuilder = null;
            if (wilburqueryOneofCase_ == 1) {
              subBuilder = ((com.isaacsheff.charlotte.proto.Reference) wilburqueryOneof_).toBuilder();
            }
            wilburqueryOneof_ =
                input.readMessage(com.isaacsheff.charlotte.proto.Reference.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom((com.isaacsheff.charlotte.proto.Reference) wilburqueryOneof_);
              wilburqueryOneof_ = subBuilder.buildPartial();
            }
            wilburqueryOneofCase_ = 1;
            break;
          }
          case 18: {
            com.isaacsheff.charlotte.proto.Block.Builder subBuilder = null;
            if (wilburqueryOneofCase_ == 2) {
              subBuilder = ((com.isaacsheff.charlotte.proto.Block) wilburqueryOneof_).toBuilder();
            }
            wilburqueryOneof_ =
                input.readMessage(com.isaacsheff.charlotte.proto.Block.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom((com.isaacsheff.charlotte.proto.Block) wilburqueryOneof_);
              wilburqueryOneof_ = subBuilder.buildPartial();
            }
            wilburqueryOneofCase_ = 2;
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
    return com.isaacsheff.charlotte.proto.CharlotteProto.internal_static_charlotte_WilburQueryInput_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.isaacsheff.charlotte.proto.CharlotteProto.internal_static_charlotte_WilburQueryInput_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.isaacsheff.charlotte.proto.WilburQueryInput.class, com.isaacsheff.charlotte.proto.WilburQueryInput.Builder.class);
  }

  private int wilburqueryOneofCase_ = 0;
  private java.lang.Object wilburqueryOneof_;
  public enum WilburqueryOneofCase
      implements com.google.protobuf.Internal.EnumLite {
    REFERENCE(1),
    FILLINTHEBLANK(2),
    WILBURQUERYONEOF_NOT_SET(0);
    private final int value;
    private WilburqueryOneofCase(int value) {
      this.value = value;
    }
    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static WilburqueryOneofCase valueOf(int value) {
      return forNumber(value);
    }

    public static WilburqueryOneofCase forNumber(int value) {
      switch (value) {
        case 1: return REFERENCE;
        case 2: return FILLINTHEBLANK;
        case 0: return WILBURQUERYONEOF_NOT_SET;
        default: return null;
      }
    }
    public int getNumber() {
      return this.value;
    }
  };

  public WilburqueryOneofCase
  getWilburqueryOneofCase() {
    return WilburqueryOneofCase.forNumber(
        wilburqueryOneofCase_);
  }

  public static final int REFERENCE_FIELD_NUMBER = 1;
  /**
   * <code>.charlotte.Reference reference = 1;</code>
   */
  public boolean hasReference() {
    return wilburqueryOneofCase_ == 1;
  }
  /**
   * <code>.charlotte.Reference reference = 1;</code>
   */
  public com.isaacsheff.charlotte.proto.Reference getReference() {
    if (wilburqueryOneofCase_ == 1) {
       return (com.isaacsheff.charlotte.proto.Reference) wilburqueryOneof_;
    }
    return com.isaacsheff.charlotte.proto.Reference.getDefaultInstance();
  }
  /**
   * <code>.charlotte.Reference reference = 1;</code>
   */
  public com.isaacsheff.charlotte.proto.ReferenceOrBuilder getReferenceOrBuilder() {
    if (wilburqueryOneofCase_ == 1) {
       return (com.isaacsheff.charlotte.proto.Reference) wilburqueryOneof_;
    }
    return com.isaacsheff.charlotte.proto.Reference.getDefaultInstance();
  }

  public static final int FILLINTHEBLANK_FIELD_NUMBER = 2;
  /**
   * <code>.charlotte.Block fillInTheBlank = 2;</code>
   */
  public boolean hasFillInTheBlank() {
    return wilburqueryOneofCase_ == 2;
  }
  /**
   * <code>.charlotte.Block fillInTheBlank = 2;</code>
   */
  public com.isaacsheff.charlotte.proto.Block getFillInTheBlank() {
    if (wilburqueryOneofCase_ == 2) {
       return (com.isaacsheff.charlotte.proto.Block) wilburqueryOneof_;
    }
    return com.isaacsheff.charlotte.proto.Block.getDefaultInstance();
  }
  /**
   * <code>.charlotte.Block fillInTheBlank = 2;</code>
   */
  public com.isaacsheff.charlotte.proto.BlockOrBuilder getFillInTheBlankOrBuilder() {
    if (wilburqueryOneofCase_ == 2) {
       return (com.isaacsheff.charlotte.proto.Block) wilburqueryOneof_;
    }
    return com.isaacsheff.charlotte.proto.Block.getDefaultInstance();
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
    if (wilburqueryOneofCase_ == 1) {
      output.writeMessage(1, (com.isaacsheff.charlotte.proto.Reference) wilburqueryOneof_);
    }
    if (wilburqueryOneofCase_ == 2) {
      output.writeMessage(2, (com.isaacsheff.charlotte.proto.Block) wilburqueryOneof_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (wilburqueryOneofCase_ == 1) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, (com.isaacsheff.charlotte.proto.Reference) wilburqueryOneof_);
    }
    if (wilburqueryOneofCase_ == 2) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, (com.isaacsheff.charlotte.proto.Block) wilburqueryOneof_);
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
    if (!(obj instanceof com.isaacsheff.charlotte.proto.WilburQueryInput)) {
      return super.equals(obj);
    }
    com.isaacsheff.charlotte.proto.WilburQueryInput other = (com.isaacsheff.charlotte.proto.WilburQueryInput) obj;

    boolean result = true;
    result = result && getWilburqueryOneofCase().equals(
        other.getWilburqueryOneofCase());
    if (!result) return false;
    switch (wilburqueryOneofCase_) {
      case 1:
        result = result && getReference()
            .equals(other.getReference());
        break;
      case 2:
        result = result && getFillInTheBlank()
            .equals(other.getFillInTheBlank());
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
    switch (wilburqueryOneofCase_) {
      case 1:
        hash = (37 * hash) + REFERENCE_FIELD_NUMBER;
        hash = (53 * hash) + getReference().hashCode();
        break;
      case 2:
        hash = (37 * hash) + FILLINTHEBLANK_FIELD_NUMBER;
        hash = (53 * hash) + getFillInTheBlank().hashCode();
        break;
      case 0:
      default:
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.isaacsheff.charlotte.proto.WilburQueryInput parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.isaacsheff.charlotte.proto.WilburQueryInput parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.WilburQueryInput parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.isaacsheff.charlotte.proto.WilburQueryInput parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.WilburQueryInput parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.isaacsheff.charlotte.proto.WilburQueryInput parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.WilburQueryInput parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.isaacsheff.charlotte.proto.WilburQueryInput parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.WilburQueryInput parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.isaacsheff.charlotte.proto.WilburQueryInput parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.WilburQueryInput parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.isaacsheff.charlotte.proto.WilburQueryInput parseFrom(
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
  public static Builder newBuilder(com.isaacsheff.charlotte.proto.WilburQueryInput prototype) {
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
   * Protobuf type {@code charlotte.WilburQueryInput}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:charlotte.WilburQueryInput)
      com.isaacsheff.charlotte.proto.WilburQueryInputOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.isaacsheff.charlotte.proto.CharlotteProto.internal_static_charlotte_WilburQueryInput_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.isaacsheff.charlotte.proto.CharlotteProto.internal_static_charlotte_WilburQueryInput_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.isaacsheff.charlotte.proto.WilburQueryInput.class, com.isaacsheff.charlotte.proto.WilburQueryInput.Builder.class);
    }

    // Construct using com.isaacsheff.charlotte.proto.WilburQueryInput.newBuilder()
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
      wilburqueryOneofCase_ = 0;
      wilburqueryOneof_ = null;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.isaacsheff.charlotte.proto.CharlotteProto.internal_static_charlotte_WilburQueryInput_descriptor;
    }

    @java.lang.Override
    public com.isaacsheff.charlotte.proto.WilburQueryInput getDefaultInstanceForType() {
      return com.isaacsheff.charlotte.proto.WilburQueryInput.getDefaultInstance();
    }

    @java.lang.Override
    public com.isaacsheff.charlotte.proto.WilburQueryInput build() {
      com.isaacsheff.charlotte.proto.WilburQueryInput result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.isaacsheff.charlotte.proto.WilburQueryInput buildPartial() {
      com.isaacsheff.charlotte.proto.WilburQueryInput result = new com.isaacsheff.charlotte.proto.WilburQueryInput(this);
      if (wilburqueryOneofCase_ == 1) {
        if (referenceBuilder_ == null) {
          result.wilburqueryOneof_ = wilburqueryOneof_;
        } else {
          result.wilburqueryOneof_ = referenceBuilder_.build();
        }
      }
      if (wilburqueryOneofCase_ == 2) {
        if (fillInTheBlankBuilder_ == null) {
          result.wilburqueryOneof_ = wilburqueryOneof_;
        } else {
          result.wilburqueryOneof_ = fillInTheBlankBuilder_.build();
        }
      }
      result.wilburqueryOneofCase_ = wilburqueryOneofCase_;
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
      if (other instanceof com.isaacsheff.charlotte.proto.WilburQueryInput) {
        return mergeFrom((com.isaacsheff.charlotte.proto.WilburQueryInput)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.isaacsheff.charlotte.proto.WilburQueryInput other) {
      if (other == com.isaacsheff.charlotte.proto.WilburQueryInput.getDefaultInstance()) return this;
      switch (other.getWilburqueryOneofCase()) {
        case REFERENCE: {
          mergeReference(other.getReference());
          break;
        }
        case FILLINTHEBLANK: {
          mergeFillInTheBlank(other.getFillInTheBlank());
          break;
        }
        case WILBURQUERYONEOF_NOT_SET: {
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
      com.isaacsheff.charlotte.proto.WilburQueryInput parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.isaacsheff.charlotte.proto.WilburQueryInput) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int wilburqueryOneofCase_ = 0;
    private java.lang.Object wilburqueryOneof_;
    public WilburqueryOneofCase
        getWilburqueryOneofCase() {
      return WilburqueryOneofCase.forNumber(
          wilburqueryOneofCase_);
    }

    public Builder clearWilburqueryOneof() {
      wilburqueryOneofCase_ = 0;
      wilburqueryOneof_ = null;
      onChanged();
      return this;
    }


    private com.google.protobuf.SingleFieldBuilderV3<
        com.isaacsheff.charlotte.proto.Reference, com.isaacsheff.charlotte.proto.Reference.Builder, com.isaacsheff.charlotte.proto.ReferenceOrBuilder> referenceBuilder_;
    /**
     * <code>.charlotte.Reference reference = 1;</code>
     */
    public boolean hasReference() {
      return wilburqueryOneofCase_ == 1;
    }
    /**
     * <code>.charlotte.Reference reference = 1;</code>
     */
    public com.isaacsheff.charlotte.proto.Reference getReference() {
      if (referenceBuilder_ == null) {
        if (wilburqueryOneofCase_ == 1) {
          return (com.isaacsheff.charlotte.proto.Reference) wilburqueryOneof_;
        }
        return com.isaacsheff.charlotte.proto.Reference.getDefaultInstance();
      } else {
        if (wilburqueryOneofCase_ == 1) {
          return referenceBuilder_.getMessage();
        }
        return com.isaacsheff.charlotte.proto.Reference.getDefaultInstance();
      }
    }
    /**
     * <code>.charlotte.Reference reference = 1;</code>
     */
    public Builder setReference(com.isaacsheff.charlotte.proto.Reference value) {
      if (referenceBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        wilburqueryOneof_ = value;
        onChanged();
      } else {
        referenceBuilder_.setMessage(value);
      }
      wilburqueryOneofCase_ = 1;
      return this;
    }
    /**
     * <code>.charlotte.Reference reference = 1;</code>
     */
    public Builder setReference(
        com.isaacsheff.charlotte.proto.Reference.Builder builderForValue) {
      if (referenceBuilder_ == null) {
        wilburqueryOneof_ = builderForValue.build();
        onChanged();
      } else {
        referenceBuilder_.setMessage(builderForValue.build());
      }
      wilburqueryOneofCase_ = 1;
      return this;
    }
    /**
     * <code>.charlotte.Reference reference = 1;</code>
     */
    public Builder mergeReference(com.isaacsheff.charlotte.proto.Reference value) {
      if (referenceBuilder_ == null) {
        if (wilburqueryOneofCase_ == 1 &&
            wilburqueryOneof_ != com.isaacsheff.charlotte.proto.Reference.getDefaultInstance()) {
          wilburqueryOneof_ = com.isaacsheff.charlotte.proto.Reference.newBuilder((com.isaacsheff.charlotte.proto.Reference) wilburqueryOneof_)
              .mergeFrom(value).buildPartial();
        } else {
          wilburqueryOneof_ = value;
        }
        onChanged();
      } else {
        if (wilburqueryOneofCase_ == 1) {
          referenceBuilder_.mergeFrom(value);
        }
        referenceBuilder_.setMessage(value);
      }
      wilburqueryOneofCase_ = 1;
      return this;
    }
    /**
     * <code>.charlotte.Reference reference = 1;</code>
     */
    public Builder clearReference() {
      if (referenceBuilder_ == null) {
        if (wilburqueryOneofCase_ == 1) {
          wilburqueryOneofCase_ = 0;
          wilburqueryOneof_ = null;
          onChanged();
        }
      } else {
        if (wilburqueryOneofCase_ == 1) {
          wilburqueryOneofCase_ = 0;
          wilburqueryOneof_ = null;
        }
        referenceBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.charlotte.Reference reference = 1;</code>
     */
    public com.isaacsheff.charlotte.proto.Reference.Builder getReferenceBuilder() {
      return getReferenceFieldBuilder().getBuilder();
    }
    /**
     * <code>.charlotte.Reference reference = 1;</code>
     */
    public com.isaacsheff.charlotte.proto.ReferenceOrBuilder getReferenceOrBuilder() {
      if ((wilburqueryOneofCase_ == 1) && (referenceBuilder_ != null)) {
        return referenceBuilder_.getMessageOrBuilder();
      } else {
        if (wilburqueryOneofCase_ == 1) {
          return (com.isaacsheff.charlotte.proto.Reference) wilburqueryOneof_;
        }
        return com.isaacsheff.charlotte.proto.Reference.getDefaultInstance();
      }
    }
    /**
     * <code>.charlotte.Reference reference = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.isaacsheff.charlotte.proto.Reference, com.isaacsheff.charlotte.proto.Reference.Builder, com.isaacsheff.charlotte.proto.ReferenceOrBuilder> 
        getReferenceFieldBuilder() {
      if (referenceBuilder_ == null) {
        if (!(wilburqueryOneofCase_ == 1)) {
          wilburqueryOneof_ = com.isaacsheff.charlotte.proto.Reference.getDefaultInstance();
        }
        referenceBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.isaacsheff.charlotte.proto.Reference, com.isaacsheff.charlotte.proto.Reference.Builder, com.isaacsheff.charlotte.proto.ReferenceOrBuilder>(
                (com.isaacsheff.charlotte.proto.Reference) wilburqueryOneof_,
                getParentForChildren(),
                isClean());
        wilburqueryOneof_ = null;
      }
      wilburqueryOneofCase_ = 1;
      onChanged();;
      return referenceBuilder_;
    }

    private com.google.protobuf.SingleFieldBuilderV3<
        com.isaacsheff.charlotte.proto.Block, com.isaacsheff.charlotte.proto.Block.Builder, com.isaacsheff.charlotte.proto.BlockOrBuilder> fillInTheBlankBuilder_;
    /**
     * <code>.charlotte.Block fillInTheBlank = 2;</code>
     */
    public boolean hasFillInTheBlank() {
      return wilburqueryOneofCase_ == 2;
    }
    /**
     * <code>.charlotte.Block fillInTheBlank = 2;</code>
     */
    public com.isaacsheff.charlotte.proto.Block getFillInTheBlank() {
      if (fillInTheBlankBuilder_ == null) {
        if (wilburqueryOneofCase_ == 2) {
          return (com.isaacsheff.charlotte.proto.Block) wilburqueryOneof_;
        }
        return com.isaacsheff.charlotte.proto.Block.getDefaultInstance();
      } else {
        if (wilburqueryOneofCase_ == 2) {
          return fillInTheBlankBuilder_.getMessage();
        }
        return com.isaacsheff.charlotte.proto.Block.getDefaultInstance();
      }
    }
    /**
     * <code>.charlotte.Block fillInTheBlank = 2;</code>
     */
    public Builder setFillInTheBlank(com.isaacsheff.charlotte.proto.Block value) {
      if (fillInTheBlankBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        wilburqueryOneof_ = value;
        onChanged();
      } else {
        fillInTheBlankBuilder_.setMessage(value);
      }
      wilburqueryOneofCase_ = 2;
      return this;
    }
    /**
     * <code>.charlotte.Block fillInTheBlank = 2;</code>
     */
    public Builder setFillInTheBlank(
        com.isaacsheff.charlotte.proto.Block.Builder builderForValue) {
      if (fillInTheBlankBuilder_ == null) {
        wilburqueryOneof_ = builderForValue.build();
        onChanged();
      } else {
        fillInTheBlankBuilder_.setMessage(builderForValue.build());
      }
      wilburqueryOneofCase_ = 2;
      return this;
    }
    /**
     * <code>.charlotte.Block fillInTheBlank = 2;</code>
     */
    public Builder mergeFillInTheBlank(com.isaacsheff.charlotte.proto.Block value) {
      if (fillInTheBlankBuilder_ == null) {
        if (wilburqueryOneofCase_ == 2 &&
            wilburqueryOneof_ != com.isaacsheff.charlotte.proto.Block.getDefaultInstance()) {
          wilburqueryOneof_ = com.isaacsheff.charlotte.proto.Block.newBuilder((com.isaacsheff.charlotte.proto.Block) wilburqueryOneof_)
              .mergeFrom(value).buildPartial();
        } else {
          wilburqueryOneof_ = value;
        }
        onChanged();
      } else {
        if (wilburqueryOneofCase_ == 2) {
          fillInTheBlankBuilder_.mergeFrom(value);
        }
        fillInTheBlankBuilder_.setMessage(value);
      }
      wilburqueryOneofCase_ = 2;
      return this;
    }
    /**
     * <code>.charlotte.Block fillInTheBlank = 2;</code>
     */
    public Builder clearFillInTheBlank() {
      if (fillInTheBlankBuilder_ == null) {
        if (wilburqueryOneofCase_ == 2) {
          wilburqueryOneofCase_ = 0;
          wilburqueryOneof_ = null;
          onChanged();
        }
      } else {
        if (wilburqueryOneofCase_ == 2) {
          wilburqueryOneofCase_ = 0;
          wilburqueryOneof_ = null;
        }
        fillInTheBlankBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.charlotte.Block fillInTheBlank = 2;</code>
     */
    public com.isaacsheff.charlotte.proto.Block.Builder getFillInTheBlankBuilder() {
      return getFillInTheBlankFieldBuilder().getBuilder();
    }
    /**
     * <code>.charlotte.Block fillInTheBlank = 2;</code>
     */
    public com.isaacsheff.charlotte.proto.BlockOrBuilder getFillInTheBlankOrBuilder() {
      if ((wilburqueryOneofCase_ == 2) && (fillInTheBlankBuilder_ != null)) {
        return fillInTheBlankBuilder_.getMessageOrBuilder();
      } else {
        if (wilburqueryOneofCase_ == 2) {
          return (com.isaacsheff.charlotte.proto.Block) wilburqueryOneof_;
        }
        return com.isaacsheff.charlotte.proto.Block.getDefaultInstance();
      }
    }
    /**
     * <code>.charlotte.Block fillInTheBlank = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.isaacsheff.charlotte.proto.Block, com.isaacsheff.charlotte.proto.Block.Builder, com.isaacsheff.charlotte.proto.BlockOrBuilder> 
        getFillInTheBlankFieldBuilder() {
      if (fillInTheBlankBuilder_ == null) {
        if (!(wilburqueryOneofCase_ == 2)) {
          wilburqueryOneof_ = com.isaacsheff.charlotte.proto.Block.getDefaultInstance();
        }
        fillInTheBlankBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.isaacsheff.charlotte.proto.Block, com.isaacsheff.charlotte.proto.Block.Builder, com.isaacsheff.charlotte.proto.BlockOrBuilder>(
                (com.isaacsheff.charlotte.proto.Block) wilburqueryOneof_,
                getParentForChildren(),
                isClean());
        wilburqueryOneof_ = null;
      }
      wilburqueryOneofCase_ = 2;
      onChanged();;
      return fillInTheBlankBuilder_;
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


    // @@protoc_insertion_point(builder_scope:charlotte.WilburQueryInput)
  }

  // @@protoc_insertion_point(class_scope:charlotte.WilburQueryInput)
  private static final com.isaacsheff.charlotte.proto.WilburQueryInput DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.isaacsheff.charlotte.proto.WilburQueryInput();
  }

  public static com.isaacsheff.charlotte.proto.WilburQueryInput getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<WilburQueryInput>
      PARSER = new com.google.protobuf.AbstractParser<WilburQueryInput>() {
    @java.lang.Override
    public WilburQueryInput parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new WilburQueryInput(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<WilburQueryInput> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<WilburQueryInput> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.isaacsheff.charlotte.proto.WilburQueryInput getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

