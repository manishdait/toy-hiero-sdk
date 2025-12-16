package org.example.sdk;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Represents a Hbar in the Hedera network, denominated in {@link HbarUnit}.
 */
public record Hbar (long tinybars) {
  /**
   * Creates an {@link Hbar} instance from the given amount in HBAR.
   *
   * @param amount the amount in HBAR
   * @return a new {@link Hbar} instance representing the given amount
   */
  public static Hbar of(long amount) {
    return of(amount, HbarUnit.HBAR);
  }

  /**
   * Creates an {@link Hbar} instance from the given amount and unit.
   *
   * @param amount the numerical amount
   * @param unit the {@link HbarUnit} to interpret the amount
   * @return a new {@link Hbar} instance representing the given amount in tinybars
   * @throws NullPointerException if {@code unit} is {@code null}
   */
  public static Hbar of(long amount, @NonNull HbarUnit unit) {
    Objects.requireNonNull(unit, "unit must not be null");
    return new Hbar(amount * unit.getMultiplier());
  }

  /**
   * Returns a new {@link Hbar} instance with the value negated.
   *
   * @return a new {@link Hbar} instance representing {@code -this.tinybars}
   */
  public Hbar negate() {
    return new Hbar(-this.tinybars);
  }
}