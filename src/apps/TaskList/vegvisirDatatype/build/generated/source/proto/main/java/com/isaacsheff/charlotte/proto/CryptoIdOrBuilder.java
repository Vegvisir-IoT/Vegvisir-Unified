// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: common.proto

package com.isaacsheff.charlotte.proto;

public interface CryptoIdOrBuilder extends
    // @@protoc_insertion_point(interface_extends:charlotte.CryptoId)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.charlotte.PublicKey publicKey = 1;</code>
   */
  boolean hasPublicKey();
  /**
   * <code>.charlotte.PublicKey publicKey = 1;</code>
   */
  com.isaacsheff.charlotte.proto.PublicKey getPublicKey();
  /**
   * <code>.charlotte.PublicKey publicKey = 1;</code>
   */
  com.isaacsheff.charlotte.proto.PublicKeyOrBuilder getPublicKeyOrBuilder();

  /**
   * <code>.charlotte.Hash hash = 2;</code>
   */
  boolean hasHash();
  /**
   * <code>.charlotte.Hash hash = 2;</code>
   */
  com.isaacsheff.charlotte.proto.Hash getHash();
  /**
   * <code>.charlotte.Hash hash = 2;</code>
   */
  com.isaacsheff.charlotte.proto.HashOrBuilder getHashOrBuilder();

  public com.isaacsheff.charlotte.proto.CryptoId.IdtypeOneofCase getIdtypeOneofCase();
}
