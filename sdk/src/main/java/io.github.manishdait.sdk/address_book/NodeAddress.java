package io.github.manishdait.sdk.address_book;

import io.github.manishdait.sdk.account.AccountId;

import java.util.List;

public record NodeAddress (
  String rsaPublicKey,
  long nodeId,
  AccountId nodeAccountId,
  byte[] nodeCertHash,
  String description,
  long stake,
  List<ServiceEndpoint> serviceEndpoints
) {
  public static NodeAddress fromProto(final com.hedera.hashgraph.sdk.proto.NodeAddress proto) {
    return new NodeAddress(
      proto.getRSAPubKey(),
      proto.getNodeId(),
      AccountId.fromProto(proto.getNodeAccountId()),
      proto.getNodeCertHash().toByteArray(),
      proto.getDescription(),
      proto.getStake(),
      proto.getServiceEndpointList().stream().map(e -> ServiceEndpoint.fromProto(e)).toList()
    );
  }
}
