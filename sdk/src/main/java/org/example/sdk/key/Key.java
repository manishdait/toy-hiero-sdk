package org.example.sdk.key;

import org.jspecify.annotations.NonNull;

/**
 * Abstract base class representing keys in the Hedera network.
 */
public interface Key {
  /**
   * Converts this key to its protobuf representation.
   *
   * @return a {@link com.hedera.hashgraph.sdk.proto.Key} representing the protobuf object
   */
  com.hedera.hashgraph.sdk.proto.Key toProto();
  byte[] getBytes();
  byte[] getDERBytes();
  @NonNull String toHexString();
  @NonNull String toDERHex();
  @NonNull KeyType getType();
}
