package org.example.sdk.internal;

import org.example.sdk.key.KeyType;
import org.example.sdk.key.PrivateKey;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Factory class for generating and loading cryptographic keys.
 */
public final class KeyFactory {
  /**
   * Generates a new ED25519 private key.
   *
   * @return a newly generated ED25519 {@link PrivateKey}
   */
  public static @NonNull PrivateKey generatePrivateKey() {
    return generatePrivateKey(KeyType.ED25519);
  }

  /**
   * Generated a new private key of specified {@link KeyType}.
   *
   * @param keyType the type of private key to generate
   * @return a newly generated {@link PrivateKey} of specified type
   */
  public static @NonNull PrivateKey generatePrivateKey(@NonNull final KeyType keyType) {
    Objects.requireNonNull(keyType, "keyType must not be null");
    if (keyType == KeyType.ED25519) {
      return ED25519PrivateKey.generate();
    } else {
      return ECDSAPrivateKey.generate();
    }
  }

  /**
   * Parse a private key from provide byte array.
   *
   * @param bytes the encoded private key bytes
   * @return the parsed {@link PrivateKey}
   */
  public static @NonNull PrivateKey generatePrivateKeyFromBytes(final byte[] bytes) {
    try {
      return ED25519PrivateKey.fromBytes(bytes);
    } catch (Exception e) {
      return null;
    }
  }
}
