package org.example.sdk;

import java.time.Instant;

public record ExchangeRate(int hbarEquiv, int centEquiv, Instant expirationTime) {
  public static ExchangeRate fromProto(com.hedera.hashgraph.sdk.proto.ExchangeRate proto) {
    return new ExchangeRate(
      proto.getHbarEquiv(),
      proto.getCentEquiv(),
      Instant.ofEpochSecond(proto.getExpirationTime().getSeconds())
    );
  }
}
