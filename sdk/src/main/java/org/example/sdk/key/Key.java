package org.example.sdk.key;

/**
 * Abstract base class representing keys in the Hedera network.
 */
public abstract class Key {
  /**
   * Converts this key to its protobuf representation.
   *
   * @return a {@link com.hedera.hashgraph.sdk.proto.Key} representing the protobuf object
   */
  public abstract com.hedera.hashgraph.sdk.proto.Key toProto();
}
