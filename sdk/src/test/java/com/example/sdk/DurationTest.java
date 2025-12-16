package com.example.sdk;

import org.assertj.core.api.Assertions;
import org.example.sdk.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DurationTest {
  @Test
  @DisplayName("Should create duration for given seconds")
  void shouldCreateDuration() {
    final Duration duration = Duration.of(120);
    Assertions.assertThat(duration).isNotNull();
    Assertions.assertThat(duration.getSeconds()).isEqualTo(120);
  }

  @Test
  @DisplayName("Should create duration from proto")
  void shouldCreateDurationFromProto() {
    final com.hedera.hashgraph.sdk.proto.Duration proto = com.hedera.hashgraph.sdk.proto.Duration.newBuilder()
      .setSeconds(120)
      .build();
    final Duration duration = Duration.fromProto(proto);

    Assertions.assertThat(duration).isNotNull();
    Assertions.assertThat(duration.getSeconds()).isEqualTo(120);
  }

  @Test
  @DisplayName("Should Convert duration to proto")
  void shouldConvertDurationToProto() {
    final Duration duration = Duration.of(120);
    final com.hedera.hashgraph.sdk.proto.Duration proto = duration.toProto();

    Assertions.assertThat(proto).isNotNull();
    Assertions.assertThat(proto.getSeconds()).isEqualTo(120);
  }
}
