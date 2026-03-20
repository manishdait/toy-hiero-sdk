package io.github.manishdait.sdk;

import java.util.Objects;

public class Duration {
  private final long seconds;

  private Duration(long seconds) {
    this.seconds = seconds;
  }

  public long getSeconds() {
    return this.seconds;
  }

  public static Duration of(long seconds) {
    return new Duration(seconds);
  }

  public static Duration fromProto(final com.hedera.hashgraph.sdk.proto.Duration proto) {
    Objects.requireNonNull(proto, "proto must be not null");
    return new Duration(proto.getSeconds());
  }

  public com.hedera.hashgraph.sdk.proto.Duration toProto() {
    return com.hedera.hashgraph.sdk.proto.Duration.newBuilder()
      .setSeconds(this.seconds)
      .build();
  }

  @Override
  public String toString() {
    return  String.format("Duration [seconds: %d]", this.seconds);
  }
}
