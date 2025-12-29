module org.example.sdk {
  requires transitive org.example.hapi;
  requires io.grpc;
  requires org.bouncycastle.provider;
  requires org.jspecify;
  requires com.google.protobuf;
  requires io.grpc.stub;

  exports org.example.sdk;
  exports org.example.sdk.key;
  exports org.example.sdk.network;
  exports org.example.sdk.account;
  exports org.example.sdk.transaction;
  exports org.example.sdk.query;
}