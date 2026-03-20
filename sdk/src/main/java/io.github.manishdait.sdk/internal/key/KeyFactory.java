package io.github.manishdait.sdk.internal.key;

import org.bouncycastle.util.encoders.Hex;
import io.github.manishdait.sdk.key.KeyType;
import io.github.manishdait.sdk.key.PrivateKey;
import io.github.manishdait.sdk.key.PublicKey;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Factory class for generating and loading cryptographic keys.
 */
public final class KeyFactory {
  /**
   * Generate a ED25519PrivateKey.
   *
   * @return the new instance of {@link PrivateKey}
   */
  public static @NonNull PrivateKey generatePrivateKey() {
    return generatePrivateKey(KeyType.ED25519);
  }

  /**
   * Generate a new Private key of specific key type.
   *
   * @param keyType the type of key to create
   * @return the new instance of {@link PrivateKey}
   */
  public static @NonNull PrivateKey generatePrivateKey(@NonNull final KeyType keyType) {
    Objects.requireNonNull(keyType, "keyType must not be null");

    return switch(keyType) {
      case ED25519 -> ED25519PrivateKey.generate();
      case ECDSA -> ECDSAPrivateKey.generate();
      default -> throw new IllegalArgumentException("Unsupported key type.");
    };
  }

  /**
   * Generate a PrivateKey from given bytes.
   *
   * @param bytes the bytes from which key must be derived
   * @return the new instance of {@link PrivateKey}
   */
  public static @NonNull PrivateKey privateKeyFromBytes(final byte[] bytes) {
    try {
      return ED25519PrivateKey.fromBytes(bytes);
    } catch (Exception e) {
      // fallback
    }

    try {
      return ECDSAPrivateKey.fromBytes(bytes);
    } catch (Exception e) {
      //fallback
    }

    throw new RuntimeException("Error parsing Private Key using bytes");
  }

  /**
   * Generate a PrivateKey from given string.
   *
   * @param str the string from which key must be derived
   * @return the new instance of {@link PrivateKey}
   */
  public static @NonNull PrivateKey privateKeyFromString(final @NonNull String str) {
    Objects.requireNonNull(str, "str must not be null");
    return privateKeyFromBytes(Hex.decode(str));
  }

  /**
   * Generate a PublicKey from given bytes.
   *
   * @param bytes the bytes from which key must be derived
   * @return the new instance of {@link PublicKey}
   */
  public static @NonNull PublicKey publicKeyFromBytes(final byte[] bytes) {
    if (bytes.length == 32) {
      return ED25519PublicKey.fromBytes(bytes);
    }

    return ECDSAPublicKey.fromBytes(bytes);
  }

  /**
   * Generate a PublicKey from given string.
   *
   * @param str the string from which key must be derived
   * @return the new instance of {@link PublicKey}
   */
  public static @NonNull PublicKey publicKeyFromString(final @NonNull String str) {
    Objects.requireNonNull(str, "str must not be null");
    return publicKeyFromBytes(Hex.decode(str));
  }
}
