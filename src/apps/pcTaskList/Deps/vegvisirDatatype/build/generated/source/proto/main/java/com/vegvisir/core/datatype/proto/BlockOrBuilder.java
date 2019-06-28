// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: vegvisir.proto

package com.vegvisir.core.datatype.proto;

public interface BlockOrBuilder extends
    // @@protoc_insertion_point(interface_extends:vegvisir.core.datatype.Block)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.charlotte.Signature signature = 1;</code>
   */
  boolean hasSignature();
  /**
   * <code>.charlotte.Signature signature = 1;</code>
   */
  com.isaacsheff.charlotte.proto.Signature getSignature();
  /**
   * <code>.charlotte.Signature signature = 1;</code>
   */
  com.isaacsheff.charlotte.proto.SignatureOrBuilder getSignatureOrBuilder();

  /**
   * <code>.vegvisir.core.datatype.Block.GenesisBlock genesis_block = 2;</code>
   */
  boolean hasGenesisBlock();
  /**
   * <code>.vegvisir.core.datatype.Block.GenesisBlock genesis_block = 2;</code>
   */
  com.vegvisir.core.datatype.proto.Block.GenesisBlock getGenesisBlock();
  /**
   * <code>.vegvisir.core.datatype.Block.GenesisBlock genesis_block = 2;</code>
   */
  com.vegvisir.core.datatype.proto.Block.GenesisBlockOrBuilder getGenesisBlockOrBuilder();

  /**
   * <code>.vegvisir.core.datatype.Block.UserBlock block = 3;</code>
   */
  boolean hasBlock();
  /**
   * <code>.vegvisir.core.datatype.Block.UserBlock block = 3;</code>
   */
  com.vegvisir.core.datatype.proto.Block.UserBlock getBlock();
  /**
   * <code>.vegvisir.core.datatype.Block.UserBlock block = 3;</code>
   */
  com.vegvisir.core.datatype.proto.Block.UserBlockOrBuilder getBlockOrBuilder();

  /**
   * <code>.vegvisir.core.datatype.Block.VectorClock vector_clock = 4;</code>
   */
  boolean hasVectorClock();
  /**
   * <code>.vegvisir.core.datatype.Block.VectorClock vector_clock = 4;</code>
   */
  com.vegvisir.core.datatype.proto.Block.VectorClock getVectorClock();
  /**
   * <code>.vegvisir.core.datatype.Block.VectorClock vector_clock = 4;</code>
   */
  com.vegvisir.core.datatype.proto.Block.VectorClockOrBuilder getVectorClockOrBuilder();

  public com.vegvisir.core.datatype.proto.Block.BlocktypeOneofCase getBlocktypeOneofCase();
}
