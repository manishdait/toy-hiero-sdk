package org.example.sdk.key;

import org.example.sdk.internal.ECDSAPublicKey;
import org.example.sdk.internal.ED25519PublicKey;
import org.jspecify.annotations.NonNull;

public interface PublicKey extends Key {
  static @NonNull PublicKey fromProto(com.hedera.hashgraph.sdk.proto.Key proto) {
    if (proto.hasEd25519()) {
      return ED25519PublicKey.fromBytes(proto.getEd25519().toByteArray());
    } else if (proto.hasECDSASecp256K1()) {
      return ECDSAPublicKey.fromBytes(proto.getECDSASecp256K1().toByteArray());
    } else  {
      throw new RuntimeException("Not implemented");
    }
  }

  boolean verify(byte[] message, byte[] signature);
}
