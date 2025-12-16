package com.example.sdk.key;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.proto.Key;
import org.assertj.core.api.Assertions;
import org.example.sdk.key.PrivateKey;
import org.example.sdk.key.PublicKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PrivateKeyTest {
  @Test
  @DisplayName("Should generate a ED25519 Private Key")
  public void shouldGeneratePrivateKey() {
    final PrivateKey key = PrivateKey.generate();
    Assertions.assertThat(key).isNotNull();
    Assertions.assertThat(key.getBytes().length).isEqualTo(32);
  }

  @Test
  @DisplayName("Should derive a ED25519 Private Key from hex string")
  public void shouldGeneratePrivateKeyFromHexString() {
    final String hexKey = "a1".repeat(32);
    final PrivateKey key = PrivateKey.fromString(hexKey);

    Assertions.assertThat(key).isNotNull();
    Assertions.assertThat(key.getBytes().length).isEqualTo(32);
  }

  @Test
  @DisplayName("Should derive a ED25519 Private Key from der string")
  public void shouldGeneratePrivateKeyFromDerKey() {
    final String derKey = "302e020100300506032b6570042204200101010101010101010101010101010101010101010101010101010101010101";
    final PrivateKey key = PrivateKey.fromString(derKey);

    Assertions.assertThat(key).isNotNull();
    Assertions.assertThat(key.getBytes().length).isEqualTo(32);
  }

  @Test
  @DisplayName("Round trip generate and creating private key")
  public void roundTripGeneratingAndCreatingPrivateKey() {
    final PrivateKey privateKey = PrivateKey.generate();
    final String hexKey = privateKey.toHexString();
    final PrivateKey newPrivateKey = PrivateKey.fromString(hexKey);

    Assertions.assertThat(privateKey).isEqualTo(newPrivateKey);
  }

  @Test
  @DisplayName("Should return a public key fro the private key")
  public void shouldGeneratePublicKey() {
    final PrivateKey key = PrivateKey.generate();
    final PublicKey publicKey = key.getPublicKey();

    Assertions.assertThat(publicKey).isNotNull();
  }

  @Test
  @DisplayName("Round trip to sign a message and verify it")
  public void shouldSignMessage() {
    final PrivateKey privateKey = PrivateKey.generate();
    final String message = "Hello";
    final byte[] signature = privateKey.sign(message.getBytes());

    Assertions.assertThat(signature).isNotNull();

    // Verify signature
    final PublicKey publicKey = privateKey.getPublicKey();
    final boolean verifies = publicKey.verify(message.getBytes(), signature);

    Assertions.assertThat(verifies).isTrue();
  }

  @Test
  @DisplayName("Should convert key to protobuf object")
  public void shouldConvertKeyToProto() {
    final PrivateKey privateKey = PrivateKey.generate();
    final Key proto = privateKey.toProto();

    Assertions.assertThat(proto).isNotNull();
    Assertions.assertThat(proto.getEd25519()).isEqualTo(ByteString.copyFrom(privateKey.getPublicKey().getBytes()));
  }
}
