package org.example.sdk.key;

import org.jspecify.annotations.NonNull;

public interface Key {
  com.hedera.hashgraph.sdk.proto.Key toProto();

  byte[] getBytes();
  byte[] getDERBytes();
  @NonNull String toHexString();
  @NonNull String toDERHex();
  @NonNull KeyType getType();
}
