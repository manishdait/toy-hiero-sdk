package io.github.manishdait.sdk;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Class representing the Duration.
 */
public final class Duration {
  private final long seconds;

  /**
   * Constructor.
   * @param seconds the value of duration seconds.
   */
  private Duration(final long seconds) {
    this.seconds = seconds;
  }

  /**
   * Get value of {@code Duration} in seconds.
   * @return duration seconds
   */
  public long getSeconds() {
    return seconds;
  }

  /**
   * Create {@code Duration} for given seconds.
   *
   * @param seconds the value of duration in seconds
   * @return the new instance of {@code Duration}
   */
  public static @NonNull Duration of(final long seconds) {
    return new Duration(seconds);
  }

  /**
   * Create {@code Duration} from protobuf message.
   *
   * @param proto the protobuf message of {@link com.hedera.hashgraph.sdk.proto.Duration}
   * @return the new instance of {@code Duration}
   */
  public static @NonNull Duration fromProto(final com.hedera.hashgraph.sdk.proto.Duration proto) {
    Objects.requireNonNull(proto, "proto must be not null");
    return new Duration(proto.getSeconds());
  }

  /**
   * Create protobuf message for {@link com.hedera.hashgraph.sdk.proto.Duration}.
   *
   * @return the protobuf message of {@code Duration}
   */
  public com.hedera.hashgraph.sdk.proto.Duration toProto() {
    return com.hedera.hashgraph.sdk.proto.Duration.newBuilder()
      .setSeconds(seconds)
      .build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || o.getClass() != this.getClass()) {
      return false;
    }
    Duration duration = (Duration) o;
    return seconds == duration.seconds;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(seconds);
  }

  @Override
  public String toString() {
    return  "Duration{seconds=" + seconds + "}";
  }
}
