package io.github.manishdait.sdk;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

public record Hbar (long tinybars) {
  public static Hbar of(long amount) {
    return of(amount, HbarUnit.HBAR);
  }

  public static Hbar of(long amount, @NonNull HbarUnit unit) {
    Objects.requireNonNull(unit, "unit must not be null");
    return new Hbar(amount * unit.getUnit());
  }

  public Hbar negate() {
    return new Hbar(-this.tinybars);
  }
}