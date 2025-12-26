package org.example.sdk.key;

import org.example.sdk.internal.KeyFactory;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Represents an asymmetric private key used for digital signatures.
 */
public interface PrivateKey extends Key {
  /**
   * Generate an Ed25519 private key.
   *
   * @return a new instance of Ed25519 {@link PrivateKey}
   */
  static @NonNull PrivateKey generate() {
    return KeyFactory.generatePrivateKey();
  }

  /**
   * Generated a new private key of specified {@link KeyType}.
   *
   * @param keyType the type of private key to generate
   * @return a newly generated {@link PrivateKey} of specified type
   */
  static @NonNull PrivateKey generate(final @NonNull KeyType keyType) {
    Objects.requireNonNull(keyType, "keyType must not be null");
    return KeyFactory.generatePrivateKey(keyType);
  }

  /**
   * Parse a private key from provide byte array.
   *
   * @param bytes the encoded private key bytes
   * @return the parsed {@link PrivateKey}
   */
  static @NonNull PrivateKey fromBytes(final byte[] bytes) {
    return KeyFactory.privateKeyFromBytes(bytes);
  }

  /**
   * Parse a private key from provide string.
   *
   * @param str the private key string
   * @return the parsed {@link PrivateKey}
   */
  static @NonNull PrivateKey fromString(final @NonNull String str) {
    Objects.requireNonNull(str, "str must not be null");
    return KeyFactory.privateKeyFromString(str);
  }

  /**
   * Converts the key into protobuf representation {@link com.hedera.hashgraph.sdk.proto.Key}.
   *
   * @return protobuf {@link com.hedera.hashgraph.sdk.proto.Key} representation
   */
  @Override
  default com.hedera.hashgraph.sdk.proto.Key toProto() {
    return getPublicKey().toProto();
  }

  /**
   * Derives the public key corresponding to this private key.
   *
   * @return the corresponding {@link PublicKey}
   */
  @NonNull PublicKey getPublicKey();

  /**
   * Signs a message using this private key.
   *
   * @param message the message to sign
   * @return the digital signature
   */
  byte[] sign(byte[] message);
}
