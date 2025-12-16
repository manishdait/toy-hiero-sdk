module org.example.hapi {
  requires com.google.protobuf;
  requires com.google.common;
  requires io.grpc.stub;
  requires io.grpc.protobuf;
  requires io.grpc;

  exports com.hedera.hashgraph.sdk.proto;
  exports com.hedera.hashgraph.sdk.proto.mirror;
}