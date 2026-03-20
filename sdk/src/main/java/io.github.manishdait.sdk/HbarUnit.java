package io.github.manishdait.sdk;

import org.jspecify.annotations.NonNull;

public enum HbarUnit {
  TINYBAR("tℏ", 1L),
  HBAR("ℏ", 100_000_000L);

  private final String symbol;
  private final long unit;

  @NonNull
  public String getSymbol() {
    return this.symbol;
  }

  public long getUnit() {
    return this.unit;
  }

  HbarUnit(@NonNull String symbol, long unit) {
    this.symbol = symbol;
    this.unit = unit;
  }
}
