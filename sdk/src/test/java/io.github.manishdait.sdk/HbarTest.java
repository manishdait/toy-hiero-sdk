package io.github.manishdait.sdk;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public final class HbarTest {
  @Test
  void shouldCreateHbarFromLong() {
    var hbar = Hbar.of(1);
    assertThat(hbar).isNotNull();
    assertThat(hbar.getValueInTinybar()).isEqualTo(100_000_000L);
  }

  @Test
  void shouldCreateHbarFromBigDecimal() {
    var hbar = Hbar.of(new BigDecimal("0.5"));
    var expected = new BigDecimal("0.5").multiply(BigDecimal.valueOf(100_000_000));
    assertThat(hbar).isNotNull();
    assertThat(hbar.getValueInTinybar()).isEqualTo(expected.longValue());
  }

  @Test
  void shouldCreateHbarFromAmountAndUnit() {
    for (HbarUnit unit : HbarUnit.values()) {
      var hbar = Hbar.of(1, unit);
      assertThat(hbar.getValueInTinybar()).isEqualTo(unit.getTinybar());
    }
  }

  @Test
  void shouldCreateHbarFromTinybar() {
    var hbar = Hbar.fromTinybar(1);
    assertThat(hbar).isNotNull();
    assertThat(hbar.getValueInTinybar()).isEqualTo(1L);
  }

  @Test
  void shouldCreateHbarFromMicrobar() {
    var hbar = Hbar.fromMicrobar(1);
    assertThat(hbar).isNotNull();
    assertThat(hbar.getValueInTinybar()).isEqualTo(100L);
  }

  @Test
  void shouldCreateHbarFromMillibar() {
    var hbar = Hbar.fromMillibar(1);
    assertThat(hbar).isNotNull();
    assertThat(hbar.getValueInTinybar()).isEqualTo(100_000L);
  }

  @Test
  void shouldCreateHbarFromKilobar() {
    var hbar = Hbar.fromKilobar(1);
    assertThat(hbar).isNotNull();
    assertThat(hbar.getValueInTinybar()).isEqualTo(100_000_000_000L);
  }

  @Test
  void shouldCreateHbarFromMegabar() {
    var hbar = Hbar.fromMegabar(1);
    assertThat(hbar).isNotNull();
    assertThat(hbar.getValueInTinybar()).isEqualTo(100_000_000_000_000L);
  }

  @Test
  void shouldCreateHbarFromGigabar() {
    var hbar = Hbar.fromGigabar(1);
    assertThat(hbar).isNotNull();
    assertThat(hbar.getValueInTinybar()).isEqualTo(100_000_000_000_000_000L);
  }

  @Test
  void shouldReturnTrueAndSameHashForHbarWithSameValue() {
    var hbar1 = Hbar.of(1);
    var hbar2 = Hbar.of(1);
    assertThat(hbar1.equals(hbar2)).isTrue();
    assertThat(hbar1.hashCode()).isEqualTo(hbar2.hashCode());
  }

  @Test
  void shouldReturnFalseAndDiffHashForHbarWithDiffValue() {
    var hbar1 = Hbar.of(1);
    var hbar2 = Hbar.of(2);
    assertThat(hbar1.equals(hbar2)).isFalse();
    assertThat(hbar1.hashCode()).isNotEqualTo(hbar2.hashCode());
  }

}
