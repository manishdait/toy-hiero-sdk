package com.example.sdk;

import org.assertj.core.api.Assertions;
import org.example.sdk.Hbar;
import org.example.sdk.HbarUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HbarTest {
  @Test
  @DisplayName("Should create a Hbar")
  void shouldCreateHbar() {
    final Hbar hbar = Hbar.of(1);
    Assertions.assertThat(hbar).isNotNull();
    Assertions.assertThat(hbar.tinybars()).isEqualTo(100_000_000L);
  }

  @Test
  @DisplayName("Should create a Hbar using HbarUnit")
  void shouldCreateHbarUsingUnit() {
    final Hbar hbar = Hbar.of(1, HbarUnit.TINYBAR);
    Assertions.assertThat(hbar).isNotNull();
    Assertions.assertThat(hbar.tinybars()).isEqualTo(1L);
  }

  @Test
  @DisplayName("Should create a negated Hbar")
  void shouldCreateNegatedHbar() {
    final Hbar original = Hbar.of(1);
    final Hbar negated = original.negate();

    Assertions.assertThat(negated).isNotNull();
    Assertions.assertThat(negated.tinybars()).isEqualTo(-original.tinybars());
  }
}
