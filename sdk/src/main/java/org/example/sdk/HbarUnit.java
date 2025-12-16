package org.example.sdk;

import org.jspecify.annotations.NonNull;

/**
 * Enumeration of units for {@link Hbar} values.
 */
public enum HbarUnit {
  TINYBAR("tℏ", 1L),
  HBAR("ℏ", 100_000_000L);

  private final long multiplier;
  private final String symbol;

  /**
   * Returns the symbol of this unit.
   *
   * @return the unit symbol
   */
  @NonNull
  public String getSymbol() {
    return this.symbol;
  }


  /**
   * Returns the multiplier to convert this unit to {@link #TINYBAR}.
   *
   * @return the multiplier
   */
  public long getMultiplier() {
    return this.multiplier;
  }

  /**
   * Construct a new HbarUnit.
   *
   * @param symbol the symbol representing the unit
   * @param multiplier the multiplier relative to {@link #TINYBAR}
   */
  HbarUnit(@NonNull String symbol, long multiplier) {
    this.multiplier = multiplier;
    this.symbol = symbol;
  }
}
