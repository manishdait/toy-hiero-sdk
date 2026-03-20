package io.github.manishdait.sdk.key;

import io.github.manishdait.sdk.internal.key.KeyFactory;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public interface PrivateKey extends Key {
  static @NonNull PrivateKey generate() {
    return KeyFactory.generatePrivateKey();
  }

  static @NonNull PrivateKey generate(final @NonNull KeyType keyType) {
    Objects.requireNonNull(keyType, "keyType must not be null");
    return KeyFactory.generatePrivateKey(keyType);
  }

  static @NonNull PrivateKey fromBytes(final byte[] bytes) {
    return KeyFactory.privateKeyFromBytes(bytes);
  }

  static @NonNull PrivateKey fromString(final @NonNull String str) {
    Objects.requireNonNull(str, "str must not be null");
    return KeyFactory.privateKeyFromString(str);
  }

  @Override
  default com.hedera.hashgraph.sdk.proto.Key toProto() {
    return getPublicKey().toProto();
  }

  @NonNull PublicKey getPublicKey();

  byte[] sign(byte[] message);
}
