// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: charlotte.proto

package com.isaacsheff.charlotte.proto;

/**
 * Protobuf type {@code charlotte.SendBlocksInput}
 */
public  final class SendBlocksInput extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:charlotte.SendBlocksInput)
    SendBlocksInputOrBuilder {
private static final long serialVersionUID = 0L;
  // Use SendBlocksInput.newBuilder() to construct.
  private SendBlocksInput(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private SendBlocksInput() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private SendBlocksInput(
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
            com.isaacsheff.charlotte.proto.Block.Builder subBuilder = null;
            if (block_ != null) {
              subBuilder = block_.toBuilder();
            }
            block_ = input.readMessage(com.isaacsheff.charlotte.proto.Block.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(block_);
              block_ = subBuilder.buildPartial();
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
    return com.isaacsheff.charlotte.proto.CharlotteProto.internal_static_charlotte_SendBlocksInput_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.isaacsheff.charlotte.proto.CharlotteProto.internal_static_charlotte_SendBlocksInput_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.isaacsheff.charlotte.proto.SendBlocksInput.class, com.isaacsheff.charlotte.proto.SendBlocksInput.Builder.class);
  }

  public static final int BLOCK_FIELD_NUMBER = 1;
  private com.isaacsheff.charlotte.proto.Block block_;
  /**
   * <code>.charlotte.Block block = 1;</code>
   */
  public boolean hasBlock() {
    return block_ != null;
  }
  /**
   * <code>.charlotte.Block block = 1;</code>
   */
  public com.isaacsheff.charlotte.proto.Block getBlock() {
    return block_ == null ? com.isaacsheff.charlotte.proto.Block.getDefaultInstance() : block_;
  }
  /**
   * <code>.charlotte.Block block = 1;</code>
   */
  public com.isaacsheff.charlotte.proto.BlockOrBuilder getBlockOrBuilder() {
    return getBlock();
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
    if (block_ != null) {
      output.writeMessage(1, getBlock());
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (block_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getBlock());
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
    if (!(obj instanceof com.isaacsheff.charlotte.proto.SendBlocksInput)) {
      return super.equals(obj);
    }
    com.isaacsheff.charlotte.proto.SendBlocksInput other = (com.isaacsheff.charlotte.proto.SendBlocksInput) obj;

    boolean result = true;
    result = result && (hasBlock() == other.hasBlock());
    if (hasBlock()) {
      result = result && getBlock()
          .equals(other.getBlock());
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
    if (hasBlock()) {
      hash = (37 * hash) + BLOCK_FIELD_NUMBER;
      hash = (53 * hash) + getBlock().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.isaacsheff.charlotte.proto.SendBlocksInput parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.isaacsheff.charlotte.proto.SendBlocksInput parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.SendBlocksInput parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.isaacsheff.charlotte.proto.SendBlocksInput parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.SendBlocksInput parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.isaacsheff.charlotte.proto.SendBlocksInput parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.SendBlocksInput parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.isaacsheff.charlotte.proto.SendBlocksInput parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.SendBlocksInput parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.isaacsheff.charlotte.proto.SendBlocksInput parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.SendBlocksInput parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.isaacsheff.charlotte.proto.SendBlocksInput parseFrom(
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
  public static Builder newBuilder(com.isaacsheff.charlotte.proto.SendBlocksInput prototype) {
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
   * Protobuf type {@code charlotte.SendBlocksInput}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:charlotte.SendBlocksInput)
      com.isaacsheff.charlotte.proto.SendBlocksInputOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.isaacsheff.charlotte.proto.CharlotteProto.internal_static_charlotte_SendBlocksInput_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.isaacsheff.charlotte.proto.CharlotteProto.internal_static_charlotte_SendBlocksInput_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.isaacsheff.charlotte.proto.SendBlocksInput.class, com.isaacsheff.charlotte.proto.SendBlocksInput.Builder.class);
    }

    // Construct using com.isaacsheff.charlotte.proto.SendBlocksInput.newBuilder()
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
      if (blockBuilder_ == null) {
        block_ = null;
      } else {
        block_ = null;
        blockBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.isaacsheff.charlotte.proto.CharlotteProto.internal_static_charlotte_SendBlocksInput_descriptor;
    }

    @java.lang.Override
    public com.isaacsheff.charlotte.proto.SendBlocksInput getDefaultInstanceForType() {
      return com.isaacsheff.charlotte.proto.SendBlocksInput.getDefaultInstance();
    }

    @java.lang.Override
    public com.isaacsheff.charlotte.proto.SendBlocksInput build() {
      com.isaacsheff.charlotte.proto.SendBlocksInput result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.isaacsheff.charlotte.proto.SendBlocksInput buildPartial() {
      com.isaacsheff.charlotte.proto.SendBlocksInput result = new com.isaacsheff.charlotte.proto.SendBlocksInput(this);
      if (blockBuilder_ == null) {
        result.block_ = block_;
      } else {
        result.block_ = blockBuilder_.build();
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
      if (other instanceof com.isaacsheff.charlotte.proto.SendBlocksInput) {
        return mergeFrom((com.isaacsheff.charlotte.proto.SendBlocksInput)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.isaacsheff.charlotte.proto.SendBlocksInput other) {
      if (other == com.isaacsheff.charlotte.proto.SendBlocksInput.getDefaultInstance()) return this;
      if (other.hasBlock()) {
        mergeBlock(other.getBlock());
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
      com.isaacsheff.charlotte.proto.SendBlocksInput parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.isaacsheff.charlotte.proto.SendBlocksInput) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private com.isaacsheff.charlotte.proto.Block block_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.isaacsheff.charlotte.proto.Block, com.isaacsheff.charlotte.proto.Block.Builder, com.isaacsheff.charlotte.proto.BlockOrBuilder> blockBuilder_;
    /**
     * <code>.charlotte.Block block = 1;</code>
     */
    public boolean hasBlock() {
      return blockBuilder_ != null || block_ != null;
    }
    /**
     * <code>.charlotte.Block block = 1;</code>
     */
    public com.isaacsheff.charlotte.proto.Block getBlock() {
      if (blockBuilder_ == null) {
        return block_ == null ? com.isaacsheff.charlotte.proto.Block.getDefaultInstance() : block_;
      } else {
        return blockBuilder_.getMessage();
      }
    }
    /**
     * <code>.charlotte.Block block = 1;</code>
     */
    public Builder setBlock(com.isaacsheff.charlotte.proto.Block value) {
      if (blockBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        block_ = value;
        onChanged();
      } else {
        blockBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.charlotte.Block block = 1;</code>
     */
    public Builder setBlock(
        com.isaacsheff.charlotte.proto.Block.Builder builderForValue) {
      if (blockBuilder_ == null) {
        block_ = builderForValue.build();
        onChanged();
      } else {
        blockBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.charlotte.Block block = 1;</code>
     */
    public Builder mergeBlock(com.isaacsheff.charlotte.proto.Block value) {
      if (blockBuilder_ == null) {
        if (block_ != null) {
          block_ =
            com.isaacsheff.charlotte.proto.Block.newBuilder(block_).mergeFrom(value).buildPartial();
        } else {
          block_ = value;
        }
        onChanged();
      } else {
        blockBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.charlotte.Block block = 1;</code>
     */
    public Builder clearBlock() {
      if (blockBuilder_ == null) {
        block_ = null;
        onChanged();
      } else {
        block_ = null;
        blockBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.charlotte.Block block = 1;</code>
     */
    public com.isaacsheff.charlotte.proto.Block.Builder getBlockBuilder() {
      
      onChanged();
      return getBlockFieldBuilder().getBuilder();
    }
    /**
     * <code>.charlotte.Block block = 1;</code>
     */
    public com.isaacsheff.charlotte.proto.BlockOrBuilder getBlockOrBuilder() {
      if (blockBuilder_ != null) {
        return blockBuilder_.getMessageOrBuilder();
      } else {
        return block_ == null ?
            com.isaacsheff.charlotte.proto.Block.getDefaultInstance() : block_;
      }
    }
    /**
     * <code>.charlotte.Block block = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.isaacsheff.charlotte.proto.Block, com.isaacsheff.charlotte.proto.Block.Builder, com.isaacsheff.charlotte.proto.BlockOrBuilder> 
        getBlockFieldBuilder() {
      if (blockBuilder_ == null) {
        blockBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.isaacsheff.charlotte.proto.Block, com.isaacsheff.charlotte.proto.Block.Builder, com.isaacsheff.charlotte.proto.BlockOrBuilder>(
                getBlock(),
                getParentForChildren(),
                isClean());
        block_ = null;
      }
      return blockBuilder_;
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


    // @@protoc_insertion_point(builder_scope:charlotte.SendBlocksInput)
  }

  // @@protoc_insertion_point(class_scope:charlotte.SendBlocksInput)
  private static final com.isaacsheff.charlotte.proto.SendBlocksInput DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.isaacsheff.charlotte.proto.SendBlocksInput();
  }

  public static com.isaacsheff.charlotte.proto.SendBlocksInput getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<SendBlocksInput>
      PARSER = new com.google.protobuf.AbstractParser<SendBlocksInput>() {
    @java.lang.Override
    public SendBlocksInput parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new SendBlocksInput(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<SendBlocksInput> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<SendBlocksInput> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.isaacsheff.charlotte.proto.SendBlocksInput getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

