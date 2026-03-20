package io.github.manishdait.sdk;

import org.assertj.core.api.Assertions;
import io.github.manishdait.sdk.ExchangeRate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class ExchangeRateTest {
  @Test
  @DisplayName("Should create an Exchange Rate object from protobuf object")
  void shouldCreateExchangeRateFromProto() {
    final var timestamp = Instant.now();
    final var proto = com.hedera.hashgraph.sdk.proto.ExchangeRate.newBuilder()
      .setHbarEquiv(1)
      .setCentEquiv(1)
      .setExpirationTime(
        com.hedera.hashgraph.sdk.proto.TimestampSeconds.newBuilder()
          .setSeconds(timestamp.getEpochSecond())
          .build()
      )
      .build();

    var result = ExchangeRate.fromProto(proto);

    Assertions.assertThat(result).isNotNull();
    Assertions.assertThat(result.centEquiv()).isEqualTo(1);
    Assertions.assertThat(result.hbarEquiv()).isEqualTo(1);
    Assertions.assertThat(result.expirationTime().getEpochSecond())
      .isEqualTo(timestamp.getEpochSecond());
  }
}
