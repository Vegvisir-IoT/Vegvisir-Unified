// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: charlotte.proto

package com.isaacsheff.charlotte.proto;

public final class CharlotteProto {
  private CharlotteProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_SignedGitSimCommit_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_SignedGitSimCommit_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_SignedGitSimCommit_GitSimCommit_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_SignedGitSimCommit_GitSimCommit_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_SignedGitSimCommit_GitSimCommit_GitSimParents_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_SignedGitSimCommit_GitSimCommit_GitSimParents_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_SignedGitSimCommit_GitSimCommit_GitSimParents_GitSimParent_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_SignedGitSimCommit_GitSimCommit_GitSimParents_GitSimParent_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_Block_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_Block_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_SendBlocksInput_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_SendBlocksInput_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_SendBlocksResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_SendBlocksResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_AvailabilityPolicy_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_AvailabilityPolicy_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_RequestAvailabilityAttestationInput_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_RequestAvailabilityAttestationInput_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_RequestAvailabilityAttestationResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_RequestAvailabilityAttestationResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_WilburQueryInput_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_WilburQueryInput_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_WilburQueryResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_WilburQueryResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_IntegrityPolicy_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_IntegrityPolicy_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_IntegrityPolicy_HetconsPolicy_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_IntegrityPolicy_HetconsPolicy_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_RequestIntegrityAttestationInput_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_RequestIntegrityAttestationInput_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_charlotte_RequestIntegrityAttestationResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_charlotte_RequestIntegrityAttestationResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017charlotte.proto\022\tcharlotte\032\014common.pro" +
      "to\032\rhetcons.proto\032\016vegvisir.proto\"\343\003\n\022Si" +
      "gnedGitSimCommit\022:\n\006commit\030\001 \001(\0132*.charl" +
      "otte.SignedGitSimCommit.GitSimCommit\022\'\n\t" +
      "signature\030\002 \001(\0132\024.charlotte.Signature\032\347\002" +
      "\n\014GitSimCommit\022\017\n\007comment\030\001 \001(\t\022\035\n\004hash\030" +
      "\002 \001(\0132\017.charlotte.Hash\022\027\n\rinitialCommit\030" +
      "\003 \001(\014H\000\022K\n\007parents\030\004 \001(\01328.charlotte.Sig" +
      "nedGitSimCommit.GitSimCommit.GitSimParen" +
      "tsH\000\032\260\001\n\rGitSimParents\022U\n\006parent\030\001 \003(\0132E" +
      ".charlotte.SignedGitSimCommit.GitSimComm" +
      "it.GitSimParents.GitSimParent\032H\n\014GitSimP" +
      "arent\022*\n\014parentCommit\030\001 \001(\0132\024.charlotte." +
      "Reference\022\014\n\004diff\030\002 \001(\014B\016\n\014commit_oneof\"" +
      "\327\002\n\005Block\022E\n\027availabilityAttestation\030\001 \001" +
      "(\0132\".charlotte.AvailabilityAttestationH\000" +
      "\022?\n\024integrityAttestation\030\002 \001(\0132\037.charlot" +
      "te.IntegrityAttestationH\000\022\r\n\003str\030\003 \001(\tH\000" +
      "\022/\n\014hetconsBlock\030\004 \001(\0132\027.charlotte.Hetco" +
      "nsBlockH\000\022;\n\022signedGitSimCommit\030\005 \001(\0132\035." +
      "charlotte.SignedGitSimCommitH\000\0226\n\rvegvis" +
      "irBlock\030\006 \001(\0132\035.vegvisir.core.datatype.B" +
      "lockH\000B\021\n\017blocktype_oneof\"2\n\017SendBlocksI" +
      "nput\022\037\n\005block\030\001 \001(\0132\020.charlotte.Block\"*\n" +
      "\022SendBlocksResponse\022\024\n\014errorMessage\030\001 \001(" +
      "\t\"r\n\022AvailabilityPolicy\022<\n\016fillInTheBlan" +
      "k\030\001 \001(\0132\".charlotte.AvailabilityAttestat" +
      "ionH\000B\036\n\034availabilitypolicytype_oneof\"T\n" +
      "#RequestAvailabilityAttestationInput\022-\n\006" +
      "policy\030\001 \001(\0132\035.charlotte.AvailabilityPol" +
      "icy\"g\n&RequestAvailabilityAttestationRes" +
      "ponse\022\024\n\014errorMessage\030\001 \001(\t\022\'\n\treference" +
      "\030\002 \001(\0132\024.charlotte.Reference\"~\n\020WilburQu" +
      "eryInput\022)\n\treference\030\001 \001(\0132\024.charlotte." +
      "ReferenceH\000\022*\n\016fillInTheBlank\030\002 \001(\0132\020.ch" +
      "arlotte.BlockH\000B\023\n\021wilburquery_oneof\"L\n\023" +
      "WilburQueryResponse\022\024\n\014errorMessage\030\001 \001(" +
      "\t\022\037\n\005block\030\002 \003(\0132\020.charlotte.Block\"\221\002\n\017I" +
      "ntegrityPolicy\0229\n\016fillInTheBlank\030\001 \001(\0132\037" +
      ".charlotte.IntegrityAttestationH\000\022A\n\rhet" +
      "consPolicy\030\002 \001(\0132(.charlotte.IntegrityPo" +
      "licy.HetconsPolicyH\000\032c\n\rHetconsPolicy\022+\n" +
      "\010proposal\030\001 \001(\0132\031.charlotte.HetconsMessa" +
      "ge\022%\n\010observer\030\002 \001(\0132\023.charlotte.CryptoI" +
      "dB\033\n\031integritypolicytype_oneof\"N\n Reques" +
      "tIntegrityAttestationInput\022*\n\006policy\030\001 \001" +
      "(\0132\032.charlotte.IntegrityPolicy\"d\n#Reques" +
      "tIntegrityAttestationResponse\022\024\n\014errorMe" +
      "ssage\030\001 \001(\t\022\'\n\treference\030\002 \001(\0132\024.charlot" +
      "te.Reference2^\n\rCharlotteNode\022M\n\nSendBlo" +
      "cks\022\032.charlotte.SendBlocksInput\032\035.charlo" +
      "tte.SendBlocksResponse\"\000(\0010\0012\220\001\n\006Wilbur\022" +
      "\205\001\n\036RequestAvailabilityAttestation\022..cha" +
      "rlotte.RequestAvailabilityAttestationInp" +
      "ut\0321.charlotte.RequestAvailabilityAttest" +
      "ationResponse\"\0002[\n\013WilburQuery\022L\n\013Wilbur" +
      "Query\022\033.charlotte.WilburQueryInput\032\036.cha" +
      "rlotte.WilburQueryResponse\"\0002\204\001\n\004Fern\022|\n" +
      "\033RequestIntegrityAttestation\022+.charlotte" +
      ".RequestIntegrityAttestationInput\032..char" +
      "lotte.RequestIntegrityAttestationRespons" +
      "e\"\000B>\n\036com.isaacsheff.charlotte.protoB\016C" +
      "harlotteProtoP\001\242\002\tCHARLOTTEb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.isaacsheff.charlotte.proto.CharlotteCommonProto.getDescriptor(),
          com.isaacsheff.charlotte.proto.HetconsProto.getDescriptor(),
          com.vegvisir.core.datatype.proto.VegvisirCoreDatatypeProto.getDescriptor(),
        }, assigner);
    internal_static_charlotte_SignedGitSimCommit_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_charlotte_SignedGitSimCommit_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_SignedGitSimCommit_descriptor,
        new java.lang.String[] { "Commit", "Signature", });
    internal_static_charlotte_SignedGitSimCommit_GitSimCommit_descriptor =
      internal_static_charlotte_SignedGitSimCommit_descriptor.getNestedTypes().get(0);
    internal_static_charlotte_SignedGitSimCommit_GitSimCommit_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_SignedGitSimCommit_GitSimCommit_descriptor,
        new java.lang.String[] { "Comment", "Hash", "InitialCommit", "Parents", "CommitOneof", });
    internal_static_charlotte_SignedGitSimCommit_GitSimCommit_GitSimParents_descriptor =
      internal_static_charlotte_SignedGitSimCommit_GitSimCommit_descriptor.getNestedTypes().get(0);
    internal_static_charlotte_SignedGitSimCommit_GitSimCommit_GitSimParents_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_SignedGitSimCommit_GitSimCommit_GitSimParents_descriptor,
        new java.lang.String[] { "Parent", });
    internal_static_charlotte_SignedGitSimCommit_GitSimCommit_GitSimParents_GitSimParent_descriptor =
      internal_static_charlotte_SignedGitSimCommit_GitSimCommit_GitSimParents_descriptor.getNestedTypes().get(0);
    internal_static_charlotte_SignedGitSimCommit_GitSimCommit_GitSimParents_GitSimParent_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_SignedGitSimCommit_GitSimCommit_GitSimParents_GitSimParent_descriptor,
        new java.lang.String[] { "ParentCommit", "Diff", });
    internal_static_charlotte_Block_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_charlotte_Block_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_Block_descriptor,
        new java.lang.String[] { "AvailabilityAttestation", "IntegrityAttestation", "Str", "HetconsBlock", "SignedGitSimCommit", "VegvisirBlock", "BlocktypeOneof", });
    internal_static_charlotte_SendBlocksInput_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_charlotte_SendBlocksInput_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_SendBlocksInput_descriptor,
        new java.lang.String[] { "Block", });
    internal_static_charlotte_SendBlocksResponse_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_charlotte_SendBlocksResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_SendBlocksResponse_descriptor,
        new java.lang.String[] { "ErrorMessage", });
    internal_static_charlotte_AvailabilityPolicy_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_charlotte_AvailabilityPolicy_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_AvailabilityPolicy_descriptor,
        new java.lang.String[] { "FillInTheBlank", "AvailabilitypolicytypeOneof", });
    internal_static_charlotte_RequestAvailabilityAttestationInput_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_charlotte_RequestAvailabilityAttestationInput_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_RequestAvailabilityAttestationInput_descriptor,
        new java.lang.String[] { "Policy", });
    internal_static_charlotte_RequestAvailabilityAttestationResponse_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_charlotte_RequestAvailabilityAttestationResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_RequestAvailabilityAttestationResponse_descriptor,
        new java.lang.String[] { "ErrorMessage", "Reference", });
    internal_static_charlotte_WilburQueryInput_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_charlotte_WilburQueryInput_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_WilburQueryInput_descriptor,
        new java.lang.String[] { "Reference", "FillInTheBlank", "WilburqueryOneof", });
    internal_static_charlotte_WilburQueryResponse_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_charlotte_WilburQueryResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_WilburQueryResponse_descriptor,
        new java.lang.String[] { "ErrorMessage", "Block", });
    internal_static_charlotte_IntegrityPolicy_descriptor =
      getDescriptor().getMessageTypes().get(9);
    internal_static_charlotte_IntegrityPolicy_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_IntegrityPolicy_descriptor,
        new java.lang.String[] { "FillInTheBlank", "HetconsPolicy", "IntegritypolicytypeOneof", });
    internal_static_charlotte_IntegrityPolicy_HetconsPolicy_descriptor =
      internal_static_charlotte_IntegrityPolicy_descriptor.getNestedTypes().get(0);
    internal_static_charlotte_IntegrityPolicy_HetconsPolicy_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_IntegrityPolicy_HetconsPolicy_descriptor,
        new java.lang.String[] { "Proposal", "Observer", });
    internal_static_charlotte_RequestIntegrityAttestationInput_descriptor =
      getDescriptor().getMessageTypes().get(10);
    internal_static_charlotte_RequestIntegrityAttestationInput_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_RequestIntegrityAttestationInput_descriptor,
        new java.lang.String[] { "Policy", });
    internal_static_charlotte_RequestIntegrityAttestationResponse_descriptor =
      getDescriptor().getMessageTypes().get(11);
    internal_static_charlotte_RequestIntegrityAttestationResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_charlotte_RequestIntegrityAttestationResponse_descriptor,
        new java.lang.String[] { "ErrorMessage", "Reference", });
    com.isaacsheff.charlotte.proto.CharlotteCommonProto.getDescriptor();
    com.isaacsheff.charlotte.proto.HetconsProto.getDescriptor();
    com.vegvisir.core.datatype.proto.VegvisirCoreDatatypeProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
