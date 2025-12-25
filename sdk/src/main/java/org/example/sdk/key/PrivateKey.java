package org.example.sdk.key;

import org.jspecify.annotations.NonNull;

/**
 * Represents an asymmetric private key used for digital signatures.
 */
public interface PrivateKey extends Key {
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
