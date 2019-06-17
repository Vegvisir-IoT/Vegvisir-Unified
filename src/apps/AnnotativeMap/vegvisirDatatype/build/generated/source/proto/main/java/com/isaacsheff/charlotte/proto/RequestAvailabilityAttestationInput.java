// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: charlotte.proto

package com.isaacsheff.charlotte.proto;

/**
 * <pre>
 * just wrappers for the rpc input and output
 * </pre>
 *
 * Protobuf type {@code charlotte.RequestAvailabilityAttestationInput}
 */
public  final class RequestAvailabilityAttestationInput extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:charlotte.RequestAvailabilityAttestationInput)
    RequestAvailabilityAttestationInputOrBuilder {
private static final long serialVersionUID = 0L;
  // Use RequestAvailabilityAttestationInput.newBuilder() to construct.
  private RequestAvailabilityAttestationInput(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private RequestAvailabilityAttestationInput() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private RequestAvailabilityAttestationInput(
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
            com.isaacsheff.charlotte.proto.AvailabilityPolicy.Builder subBuilder = null;
            if (policy_ != null) {
              subBuilder = policy_.toBuilder();
            }
            policy_ = input.readMessage(com.isaacsheff.charlotte.proto.AvailabilityPolicy.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(policy_);
              policy_ = subBuilder.buildPartial();
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
    return com.isaacsheff.charlotte.proto.CharlotteProto.internal_static_charlotte_RequestAvailabilityAttestationInput_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.isaacsheff.charlotte.proto.CharlotteProto.internal_static_charlotte_RequestAvailabilityAttestationInput_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput.class, com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput.Builder.class);
  }

  public static final int POLICY_FIELD_NUMBER = 1;
  private com.isaacsheff.charlotte.proto.AvailabilityPolicy policy_;
  /**
   * <code>.charlotte.AvailabilityPolicy policy = 1;</code>
   */
  public boolean hasPolicy() {
    return policy_ != null;
  }
  /**
   * <code>.charlotte.AvailabilityPolicy policy = 1;</code>
   */
  public com.isaacsheff.charlotte.proto.AvailabilityPolicy getPolicy() {
    return policy_ == null ? com.isaacsheff.charlotte.proto.AvailabilityPolicy.getDefaultInstance() : policy_;
  }
  /**
   * <code>.charlotte.AvailabilityPolicy policy = 1;</code>
   */
  public com.isaacsheff.charlotte.proto.AvailabilityPolicyOrBuilder getPolicyOrBuilder() {
    return getPolicy();
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
    if (policy_ != null) {
      output.writeMessage(1, getPolicy());
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (policy_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getPolicy());
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
    if (!(obj instanceof com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput)) {
      return super.equals(obj);
    }
    com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput other = (com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput) obj;

    boolean result = true;
    result = result && (hasPolicy() == other.hasPolicy());
    if (hasPolicy()) {
      result = result && getPolicy()
          .equals(other.getPolicy());
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
    if (hasPolicy()) {
      hash = (37 * hash) + POLICY_FIELD_NUMBER;
      hash = (53 * hash) + getPolicy().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput parseFrom(
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
  public static Builder newBuilder(com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput prototype) {
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
   * just wrappers for the rpc input and output
   * </pre>
   *
   * Protobuf type {@code charlotte.RequestAvailabilityAttestationInput}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:charlotte.RequestAvailabilityAttestationInput)
      com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInputOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.isaacsheff.charlotte.proto.CharlotteProto.internal_static_charlotte_RequestAvailabilityAttestationInput_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.isaacsheff.charlotte.proto.CharlotteProto.internal_static_charlotte_RequestAvailabilityAttestationInput_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput.class, com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput.Builder.class);
    }

    // Construct using com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput.newBuilder()
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
      if (policyBuilder_ == null) {
        policy_ = null;
      } else {
        policy_ = null;
        policyBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.isaacsheff.charlotte.proto.CharlotteProto.internal_static_charlotte_RequestAvailabilityAttestationInput_descriptor;
    }

    @java.lang.Override
    public com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput getDefaultInstanceForType() {
      return com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput.getDefaultInstance();
    }

    @java.lang.Override
    public com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput build() {
      com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput buildPartial() {
      com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput result = new com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput(this);
      if (policyBuilder_ == null) {
        result.policy_ = policy_;
      } else {
        result.policy_ = policyBuilder_.build();
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
      if (other instanceof com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput) {
        return mergeFrom((com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput other) {
      if (other == com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput.getDefaultInstance()) return this;
      if (other.hasPolicy()) {
        mergePolicy(other.getPolicy());
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
      com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private com.isaacsheff.charlotte.proto.AvailabilityPolicy policy_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.isaacsheff.charlotte.proto.AvailabilityPolicy, com.isaacsheff.charlotte.proto.AvailabilityPolicy.Builder, com.isaacsheff.charlotte.proto.AvailabilityPolicyOrBuilder> policyBuilder_;
    /**
     * <code>.charlotte.AvailabilityPolicy policy = 1;</code>
     */
    public boolean hasPolicy() {
      return policyBuilder_ != null || policy_ != null;
    }
    /**
     * <code>.charlotte.AvailabilityPolicy policy = 1;</code>
     */
    public com.isaacsheff.charlotte.proto.AvailabilityPolicy getPolicy() {
      if (policyBuilder_ == null) {
        return policy_ == null ? com.isaacsheff.charlotte.proto.AvailabilityPolicy.getDefaultInstance() : policy_;
      } else {
        return policyBuilder_.getMessage();
      }
    }
    /**
     * <code>.charlotte.AvailabilityPolicy policy = 1;</code>
     */
    public Builder setPolicy(com.isaacsheff.charlotte.proto.AvailabilityPolicy value) {
      if (policyBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        policy_ = value;
        onChanged();
      } else {
        policyBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.charlotte.AvailabilityPolicy policy = 1;</code>
     */
    public Builder setPolicy(
        com.isaacsheff.charlotte.proto.AvailabilityPolicy.Builder builderForValue) {
      if (policyBuilder_ == null) {
        policy_ = builderForValue.build();
        onChanged();
      } else {
        policyBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.charlotte.AvailabilityPolicy policy = 1;</code>
     */
    public Builder mergePolicy(com.isaacsheff.charlotte.proto.AvailabilityPolicy value) {
      if (policyBuilder_ == null) {
        if (policy_ != null) {
          policy_ =
            com.isaacsheff.charlotte.proto.AvailabilityPolicy.newBuilder(policy_).mergeFrom(value).buildPartial();
        } else {
          policy_ = value;
        }
        onChanged();
      } else {
        policyBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.charlotte.AvailabilityPolicy policy = 1;</code>
     */
    public Builder clearPolicy() {
      if (policyBuilder_ == null) {
        policy_ = null;
        onChanged();
      } else {
        policy_ = null;
        policyBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.charlotte.AvailabilityPolicy policy = 1;</code>
     */
    public com.isaacsheff.charlotte.proto.AvailabilityPolicy.Builder getPolicyBuilder() {
      
      onChanged();
      return getPolicyFieldBuilder().getBuilder();
    }
    /**
     * <code>.charlotte.AvailabilityPolicy policy = 1;</code>
     */
    public com.isaacsheff.charlotte.proto.AvailabilityPolicyOrBuilder getPolicyOrBuilder() {
      if (policyBuilder_ != null) {
        return policyBuilder_.getMessageOrBuilder();
      } else {
        return policy_ == null ?
            com.isaacsheff.charlotte.proto.AvailabilityPolicy.getDefaultInstance() : policy_;
      }
    }
    /**
     * <code>.charlotte.AvailabilityPolicy policy = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.isaacsheff.charlotte.proto.AvailabilityPolicy, com.isaacsheff.charlotte.proto.AvailabilityPolicy.Builder, com.isaacsheff.charlotte.proto.AvailabilityPolicyOrBuilder> 
        getPolicyFieldBuilder() {
      if (policyBuilder_ == null) {
        policyBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.isaacsheff.charlotte.proto.AvailabilityPolicy, com.isaacsheff.charlotte.proto.AvailabilityPolicy.Builder, com.isaacsheff.charlotte.proto.AvailabilityPolicyOrBuilder>(
                getPolicy(),
                getParentForChildren(),
                isClean());
        policy_ = null;
      }
      return policyBuilder_;
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


    // @@protoc_insertion_point(builder_scope:charlotte.RequestAvailabilityAttestationInput)
  }

  // @@protoc_insertion_point(class_scope:charlotte.RequestAvailabilityAttestationInput)
  private static final com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput();
  }

  public static com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<RequestAvailabilityAttestationInput>
      PARSER = new com.google.protobuf.AbstractParser<RequestAvailabilityAttestationInput>() {
    @java.lang.Override
    public RequestAvailabilityAttestationInput parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new RequestAvailabilityAttestationInput(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<RequestAvailabilityAttestationInput> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<RequestAvailabilityAttestationInput> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.isaacsheff.charlotte.proto.RequestAvailabilityAttestationInput getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

