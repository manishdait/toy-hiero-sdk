package org.example.sdk;

import java.util.Objects;

/**
 * Represent the Duration of time in seconds.
 */
public class Duration {
  private final long seconds;

  /**
   * Create a new {@link Duration} of given seconds.
   *
   * @param seconds the number of seconds. must be non-negative
   */
  private Duration(long seconds) {
    this.seconds = seconds;
  }

  /**
   * Return duration in seconds.
   *
   * @return the number of seconds
   */
  public long getSeconds() {
    return this.seconds;
  }

  /**
   * Create a {@link Duration} for given seconds.
   *
   * @param seconds the number seconds, must be non-negative
   * @return new instance of {@link Duration}
   */
  public static Duration of(long seconds) {
    return new Duration(seconds);
  }

  /**
   * Create a {@link Duration} from it protobuf {@link com.hedera.hashgraph.sdk.proto.Duration} object.
   *
   * @param proto protobuf {@link com.hedera.hashgraph.sdk.proto.Duration} object
   * @return new instance of {@link Duration}
   */
  public static Duration fromProto(final com.hedera.hashgraph.sdk.proto.Duration proto) {
    Objects.requireNonNull(proto, "proto must be not null.");
    return new Duration(proto.getSeconds());
  }

  /**
   * Converts the {@link Duration} to protobuf {@link com.hedera.hashgraph.sdk.proto.Duration} object.
   *
   * @return the protobuf {@link com.hedera.hashgraph.sdk.proto.Duration} of the {@link Duration}
   */
  public com.hedera.hashgraph.sdk.proto.Duration toProto() {
    return com.hedera.hashgraph.sdk.proto.Duration.newBuilder()
      .setSeconds(this.seconds)
      .build();
  }

  @Override
  public String toString() {
    return  String.format("Duration[seconds=%d]", this.seconds);
  }
}
