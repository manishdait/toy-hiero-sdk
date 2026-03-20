package io.github.manishdait.sdk.internal.key;

import org.bouncycastle.crypto.digests.KeccakDigest;
import org.jspecify.annotations.NonNull;

public final class Keccak256Utils {
   static byte[] keccak256(final byte[] input) {
    final var digest = new KeccakDigest(256);
    digest.update(input, 0, input.length);

    final byte[] out = new byte[32];
    digest.doFinal(out, 0);

    return out;
  }
}
