package org.example.sdk.internal;

import org.bouncycastle.crypto.digests.KeccakDigest;

/**
 * Utility methods for computing Keccak-256 hashes.
 */
public final class Keccak256Utils {
  /**
   * Computes the Keccak-256 hash of the given input.
   *
   * @param input the data to hash
   * @return a 32-byte Keccak-256 hash
   */
   static byte[] keccak256(final byte[] input) {
    final KeccakDigest digest = new KeccakDigest(256);
    digest.update(input, 0, input.length);
    final byte[] out = new byte[32];
    digest.doFinal(out, 0);
    return out;
  }
}
