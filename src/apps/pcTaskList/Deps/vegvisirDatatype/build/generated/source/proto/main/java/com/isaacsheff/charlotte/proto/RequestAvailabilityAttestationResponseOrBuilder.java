// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: charlotte.proto

package com.isaacsheff.charlotte.proto;

public interface RequestAvailabilityAttestationResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:charlotte.RequestAvailabilityAttestationResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string errorMessage = 1;</code>
   */
  java.lang.String getErrorMessage();
  /**
   * <code>string errorMessage = 1;</code>
   */
  com.google.protobuf.ByteString
      getErrorMessageBytes();

  /**
   * <code>.charlotte.Reference reference = 2;</code>
   */
  boolean hasReference();
  /**
   * <code>.charlotte.Reference reference = 2;</code>
   */
  com.isaacsheff.charlotte.proto.Reference getReference();
  /**
   * <code>.charlotte.Reference reference = 2;</code>
   */
  com.isaacsheff.charlotte.proto.ReferenceOrBuilder getReferenceOrBuilder();
}
