module io.github.manishdait.sdk {
  requires transitive io.github.manishdait.hapi;
  requires io.grpc;
  requires org.bouncycastle.provider;
  requires org.jspecify;
  requires com.google.protobuf;
  requires io.grpc.stub;
  requires java.net.http;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;

  exports io.github.manishdait.sdk;
  exports io.github.manishdait.sdk.key;
  exports io.github.manishdait.sdk.network;
  exports io.github.manishdait.sdk.account;
  exports io.github.manishdait.sdk.transaction;
  exports io.github.manishdait.sdk.query;
  exports io.github.manishdait.sdk.address_book;
}