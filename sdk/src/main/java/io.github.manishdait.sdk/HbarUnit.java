package io.github.manishdait.sdk;

import java.util.Objects;
import org.jspecify.annotations.NonNull;

/** Enum representing HbarUnit. */
public enum HbarUnit {
  /** The atomic (smallest) unit of hbar, used natively by the Hedera network */
  TINYBAR("tℏ", 1L),

  /** Equivalent to 100 tinybar or 1⁄1,000,000 hbar. */
  MICROBAR("μℏ", 100L),

  /** Equivalent to 100,000 tinybar or 1⁄1,000 hbar. */
  MILLIBAR("mℏ", 100_000L),

  /** The base unit of hbar, equivalent to 100 million tinybar. */
  HBAR("ℏ", 100_000_000L),

  /** Equivalent to 1 thousand hbar or 100 billion tinybar.HbarUnit.Megabar. */
  KILOBAR("Kℏ", 100_000_000_000L),

  /** Equivalent to 1 million hbar or 100 trillion tinybar. */
  MEGABAR("Mℏ", 100_000_000_000_000L),

  /** Equivalent to 1 billion hbar or 100 quadrillion tinybar. */
  GIGABAR("Gℏ", 100_000_000_000_000_000L);

  private final String symbol;
  private final long tinybar;

  HbarUnit(@NonNull final String symbol, final long tinybar) {
    Objects.requireNonNull(symbol, "symbol must not be null");
    this.symbol = symbol;
    this.tinybar = tinybar;
  }

  /**
   * Get symbol for {@code HbarUnit}.
   *
   * @return symbol for hbar unit
   */
  @NonNull public String getSymbol() {
    return this.symbol;
  }

  /**
   * Get the value for each unit in tinybars.
   *
   * @return the value of tinybar for each unit
   */
  public long getTinybar() {
    return this.tinybar;
  }
}
