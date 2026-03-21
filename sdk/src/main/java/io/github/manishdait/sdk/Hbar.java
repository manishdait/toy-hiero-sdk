package io.github.manishdait.sdk;

import java.math.BigDecimal;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

/** Class Representing Hbar. */
public class Hbar implements Comparable<Hbar> {
  private final long valueInTinybar;

  /**
   * Constructor.
   *
   * @param valueInTinybar the amount in tinybar
   */
  private Hbar(final long valueInTinybar) {
    this.valueInTinybar = valueInTinybar;
  }

  /**
   * Get the value in tinybar.
   *
   * @return the value of Hbar in tinybar
   */
  public long getValueInTinybar() {
    return valueInTinybar;
  }

  /**
   * Create a Hbar with given value.
   *
   * @param hbar the amount in hbar
   * @return {@code Hbar}
   */
  public static @NonNull Hbar of(final long hbar) {
    return of(hbar, HbarUnit.HBAR);
  }

  /**
   * Create a Hbar with given value.
   *
   * @param hbar the amount in hbar
   * @return {@code Hbar}
   */
  public static @NonNull Hbar of(@NonNull final BigDecimal hbar) {
    Objects.requireNonNull(hbar, "hbar must not be null");
    return of(hbar, HbarUnit.HBAR);
  }

  /**
   * Create a Hbar with given value.
   *
   * @param amount the amount
   * @param unit the {@link HbarUnit} of amount
   * @return {@code Hbar}
   */
  public static @NonNull Hbar of(final long amount, @NonNull final HbarUnit unit) {
    Objects.requireNonNull(unit, "unit must not be null");
    return of(new BigDecimal(amount), unit);
  }

  /**
   * Create a Hbar with given value.
   *
   * @param amount the amount
   * @param unit the {@link HbarUnit} of amount
   * @return {@code Hbar}
   */
  public static @NonNull Hbar of(@NonNull final BigDecimal amount, @NonNull final HbarUnit unit) {
    Objects.requireNonNull(amount, "amount must not be null");
    Objects.requireNonNull(unit, "unit must not be null");

    final BigDecimal tinybars = amount.multiply(BigDecimal.valueOf(unit.getTinybar()));

    if (tinybars.doubleValue() % 1 != 0) {
      throw new IllegalArgumentException(
          "Amount and Unit combination results in a fractional value for tinybar.");
    }

    return new Hbar(tinybars.longValue());
  }

  /**
   * Create a Hbar with given tinybars.
   *
   * @param tinybar the amount in tinybars
   * @return {@code Hbar}
   */
  public static @NonNull Hbar fromTinybar(final long tinybar) {
    return of(tinybar, HbarUnit.TINYBAR);
  }

  /**
   * Create a Hbar with given microbar.
   *
   * @param microbar the amount in microbars
   * @return {@code Hbar}
   */
  public static @NonNull Hbar fromMicrobar(final long microbar) {
    return of(microbar, HbarUnit.MICROBAR);
  }

  /**
   * Create a Hbar with given microbar.
   *
   * @param microbar the amount in microbars
   * @return {@code Hbar}
   */
  public static @NonNull Hbar fromMicrobar(@NonNull final BigDecimal microbar) {
    Objects.requireNonNull(microbar, "microbar must not be null");
    return of(microbar, HbarUnit.MICROBAR);
  }

  /**
   * Create a Hbar with given millibar.
   *
   * @param millibar the amount in millibars
   * @return {@code Hbar}
   */
  public static @NonNull Hbar fromMillibar(final long millibar) {
    return of(millibar, HbarUnit.MILLIBAR);
  }

  /**
   * Create a Hbar with given millibar.
   *
   * @param millibar the amount in millibars
   * @return {@code Hbar}
   */
  public static @NonNull Hbar fromMillibar(@NonNull final BigDecimal millibar) {
    Objects.requireNonNull(millibar, "millibar must not be null");
    return of(millibar, HbarUnit.MILLIBAR);
  }

  /**
   * Create a Hbar with given kilobar.
   *
   * @param kilobar the amount in kilobars
   * @return {@code Hbar}
   */
  public static @NonNull Hbar fromKilobar(final long kilobar) {
    return of(kilobar, HbarUnit.KILOBAR);
  }

  /**
   * Create a Hbar with given kilobar.
   *
   * @param kilobar the amount in kilobars
   * @return {@code Hbar}
   */
  public static @NonNull Hbar fromKilobar(@NonNull final BigDecimal kilobar) {
    Objects.requireNonNull(kilobar, "kilobar must not be null");
    return of(kilobar, HbarUnit.KILOBAR);
  }

  /**
   * Create a Hbar with given megabar.
   *
   * @param megabar the amount in megabars
   * @return {@code Hbar}
   */
  public static @NonNull Hbar fromMegabar(final long megabar) {
    return of(megabar, HbarUnit.MEGABAR);
  }

  /**
   * Create a Hbar with given megabar.
   *
   * @param megabar the amount in megabars
   * @return {@code Hbar}
   */
  public static @NonNull Hbar fromMegabar(@NonNull final BigDecimal megabar) {
    Objects.requireNonNull(megabar, "megabar must not be null");
    return of(megabar, HbarUnit.MEGABAR);
  }

  /**
   * Create a Hbar with given gigabar.
   *
   * @param gigabar the amount in gigabar
   * @return {@code Hbar}
   */
  public static @NonNull Hbar fromGigabar(final long gigabar) {
    return of(gigabar, HbarUnit.GIGABAR);
  }

  /**
   * Create a Hbar with given gigabar.
   *
   * @param gigabar the amount in gigabar
   * @return {@code Hbar}
   */
  public static @NonNull Hbar fromGigabar(@NonNull final BigDecimal gigabar) {
    Objects.requireNonNull(gigabar, "gigabar must not be null");
    return of(gigabar, HbarUnit.GIGABAR);
  }

  @Override
  public int compareTo(Hbar o) {
    return Long.compare(this.valueInTinybar, o.valueInTinybar);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || o.getClass() != this.getClass()) {
      return false;
    }

    Hbar hbar = (Hbar) o;
    return valueInTinybar == hbar.valueInTinybar;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(valueInTinybar);
  }

  @Override
  public String toString() {
    return "Hbar{" + "valueInTinybar=" + valueInTinybar + '}';
  }
}
