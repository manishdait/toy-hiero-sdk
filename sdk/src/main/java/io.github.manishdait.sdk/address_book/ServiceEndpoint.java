package io.github.manishdait.sdk.address_book;

public record ServiceEndpoint(
  byte[] ipAddressV4,
  String domainName,
  int port
) {
  public static ServiceEndpoint fromProto(final com.hedera.hashgraph.sdk.proto.ServiceEndpoint proto) {
    return new ServiceEndpoint(
      proto.getIpAddressV4().toByteArray(),
      proto.getDomainName(),
      proto.getPort()
    );
  }
}
