package io.github.manishdait.sdk;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

public class DurationTest {
  @Test
  void shouldCreateDuration() {
    var duration = Duration.of(120);
    assertThat(duration).isNotNull();
    assertThat(duration.getSeconds()).isEqualTo(120);
  }

  @Test
  void shouldCreateDurationFromProto() {
    var proto = com.hedera.hashgraph.sdk.proto.Duration.newBuilder().setSeconds(120).build();
    var duration = Duration.fromProto(proto);

    assertThat(duration).isNotNull();
    assertThat(duration.getSeconds()).isEqualTo(120);
  }

  @Test
  void shouldConvertDurationToProto() {
    var duration = Duration.of(120);
    var proto = duration.toProto();

    assertThat(proto).isNotNull();
    assertThat(proto.getSeconds()).isEqualTo(120);
  }

  @Test
  void shouldReturnTrueAndSameHashCodeWhenTwoDurationAreSame() {
    var duration1 = Duration.of(1);
    var duration2 = Duration.of(1);

    assertThat(duration1.equals(duration2)).isTrue();
    assertThat(duration1.hashCode()).isEqualTo(duration2.hashCode());
  }

  @Test
  void shouldReturnFalseAndDiffHashCodeWhenTwoDurationAreNotSame() {
    var duration1 = Duration.of(1);
    var duration2 = Duration.of(2);

    assertThat(duration1.equals(duration2)).isFalse();
    assertThat(duration1.hashCode()).isNotEqualTo(duration2.hashCode());
  }
}
