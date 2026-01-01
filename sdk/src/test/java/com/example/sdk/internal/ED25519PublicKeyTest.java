package com.example.sdk.internal;

import org.assertj.core.api.Assertions;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.bouncycastle.util.encoders.Hex;
import org.example.sdk.internal.key.ED25519PrivateKey;
import org.example.sdk.internal.key.ED25519PublicKey;
import org.example.sdk.key.KeyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ED25519PublicKeyTest {
  // Derive public key.

  @Test
  @DisplayName("Should derive a Ed25519 public key from private key")
  void shouldDerivePublicKeyFromPrivateKey() {
    final var privateKey = ED25519PrivateKey.generate();
    final var publicKey = privateKey.getPublicKey();

    Assertions.assertThat(publicKey).isNotNull();
    Assertions.assertThat(publicKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(publicKey.getBytes()).hasSize(Ed25519.PUBLIC_KEY_SIZE);
  }

  // Parse public key from bytes.

  @Test
  @DisplayName("Should parse a Ed25519 public key from bytes")
  void shouldParsePublicKeyFromBytes() {
    final var bytes = Hex.decode("a1".repeat(32));
    final var publicKey = ED25519PublicKey.fromBytes(bytes);

    Assertions.assertThat(publicKey).isNotNull();
    Assertions.assertThat(publicKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(publicKey.getBytes()).hasSize(Ed25519.PUBLIC_KEY_SIZE);
  }

  @Test
  @DisplayName("Should parse a Ed25519 public key from DER bytes")
  void shouldParsePublicKeyFromDerBytes() {
    final var bytes = Hex.decode("302a300506032b6570032100" + "11".repeat(32));
    final var publicKey = ED25519PublicKey.fromBytes(bytes);

    Assertions.assertThat(publicKey).isNotNull();
    Assertions.assertThat(publicKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(publicKey.getBytes()).hasSize(Ed25519.PUBLIC_KEY_SIZE);
  }

  @Test
  @DisplayName("Should round-trip Ed25519 public key via bytes")
  void roundTripToDeriveED25519PublicKeyViaBytes() {
    final var privateKey = ED25519PrivateKey.generate();
    final var publicKey = privateKey.getPublicKey();
    final var bytes = publicKey.getBytes();

    final var newPublicKey = ED25519PublicKey.fromBytes(bytes);

    Assertions.assertThat(newPublicKey).isNotNull();
    Assertions.assertThat(newPublicKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(newPublicKey.getBytes().length).isEqualTo(Ed25519.PUBLIC_KEY_SIZE);
    Assertions.assertThat(newPublicKey.getBytes()).isEqualTo(publicKey.getBytes());
  }

  @Test
  @DisplayName("Should round-trip Ed25519 public key via DER bytes")
  void roundTripToDeriveED25519PublicKeyViaDerBytes() {
    final var privateKey = ED25519PrivateKey.generate();
    final var publicKey = privateKey.getPublicKey();
    final var bytes = publicKey.getDERBytes();

    final var newPublicKey = ED25519PublicKey.fromBytes(bytes);

    Assertions.assertThat(newPublicKey).isNotNull();
    Assertions.assertThat(newPublicKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(newPublicKey.getBytes().length).isEqualTo(Ed25519.PUBLIC_KEY_SIZE);
    Assertions.assertThat(newPublicKey.getBytes()).isEqualTo(publicKey.getBytes());
  }

  @Test
  @DisplayName("Should raise error when parsing Ed25519 public key from invalid bytes")
  void shouldRaiseErrorWhenParsePublicKeyFromInvalidBytes() {
    final var bytes = Hex.decode("a1".repeat(31));
    Assertions.assertThatThrownBy(() -> ED25519PublicKey.fromBytes(bytes))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid Ed25519 public key encoding");
  }

  @Test
  @DisplayName("Should raise error when parsing Ed25519 public key from invalid Der bytes")
  void shouldRaiseErrorWhenParsePublicKeyFromInvalidDerBytes() {
    final var bytes = Hex.decode("302a300506032b6570032100" + "11".repeat(31));
    Assertions.assertThatThrownBy(() -> ED25519PublicKey.fromBytes(bytes))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid Ed25519 public key encoding");
  }

  // Parse public key from string.

  @Test
  @DisplayName("Should parse a Ed25519 public key from string")
  void shouldParsePublicKeyFromString() {
    final var hex = "a1".repeat(32);
    final var publicKey = ED25519PublicKey.fromString(hex);

    Assertions.assertThat(publicKey).isNotNull();
    Assertions.assertThat(publicKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(publicKey.getBytes()).hasSize(Ed25519.PUBLIC_KEY_SIZE);
  }

  @Test
  @DisplayName("Should parse a Ed25519 public key from DER string")
  void shouldParsePublicKeyFromDerString() {
    final var derHex = "302a300506032b6570032100" + "11".repeat(32);
    final var publicKey = ED25519PublicKey.fromString(derHex);

    Assertions.assertThat(publicKey).isNotNull();
    Assertions.assertThat(publicKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(publicKey.getBytes()).hasSize(Ed25519.PUBLIC_KEY_SIZE);
  }

  @Test
  @DisplayName("Should round-trip Ed25519 public key via hex")
  void roundTripToDeriveED25519PublicKeyViaString() {
    final var privateKey = ED25519PrivateKey.generate();
    final var publicKey = privateKey.getPublicKey();
    final var hex = publicKey.toHexString();

    final var newPublicKey = ED25519PublicKey.fromString(hex);

    Assertions.assertThat(newPublicKey).isNotNull();
    Assertions.assertThat(newPublicKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(newPublicKey.getBytes().length).isEqualTo(Ed25519.PUBLIC_KEY_SIZE);
    Assertions.assertThat(newPublicKey.getBytes()).isEqualTo(publicKey.getBytes());
  }

  @Test
  @DisplayName("Should round-trip Ed25519 public key via DER hex")
  void roundTripToDeriveED25519PublicKeyViaDerHex() {
    final var privateKey = ED25519PrivateKey.generate();
    final var publicKey = privateKey.getPublicKey();
    final var derHex = publicKey.toDERHex();

    final var newPublicKey = ED25519PublicKey.fromString(derHex);

    Assertions.assertThat(newPublicKey).isNotNull();
    Assertions.assertThat(newPublicKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(newPublicKey.getBytes().length).isEqualTo(Ed25519.PUBLIC_KEY_SIZE);
    Assertions.assertThat(newPublicKey.getBytes()).isEqualTo(publicKey.getBytes());
  }

  @Test
  @DisplayName("Should raise error when parsing Ed25519 public key from invalid hex")
  void shouldRaiseErrorWhenParsePublicKeyFromInvalidHex() {
    final var hex = "a1".repeat(31);
    Assertions.assertThatThrownBy(() -> ED25519PublicKey.fromString(hex))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid Ed25519 public key encoding");
  }

  @Test
  @DisplayName("Should raise error when parsing Ed25519 public key from invalid Der hex")
  void shouldRaiseErrorWhenParsePublicKeyFromInvalidDerHex() {
    final var derHex = "302a300506032b6570032100" + "11".repeat(31);
    Assertions.assertThatThrownBy(() -> ED25519PublicKey.fromString(derHex))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid Ed25519 public key encoding");
  }

  // To string methods.

  @Test
  @DisplayName("Should return the hex representation of the public key")
  void shouldReturnHexRepresentationOfPublicKey() {
    final var publicKey = ED25519PrivateKey.generate().getPublicKey();
    final var hexStr = publicKey.toHexString();

    Assertions.assertThat(hexStr).isNotNull();
  }

  @Test
  @DisplayName("Should return the Der hex representation of the public key")
  void shouldReturnDerHexRepresentationOfPublicKey() {
    final var publicKey = ED25519PrivateKey.generate().getPublicKey();
    final var hexStr = publicKey.toDERHex();

    Assertions.assertThat(hexStr).isNotNull();
  }

  // Verify signature.

  @Test
  @DisplayName("Should verify the signature using the public key")
  void shouldVerifySignatureWithEd25519PublicKey() {
    final var message = "Hello World";
    final var privateKey = ED25519PrivateKey.generate();

    final var signature = privateKey.sign(message.getBytes());
    Assertions.assertThat(signature)
      .isNotNull()
      .hasSize(Ed25519.SIGNATURE_SIZE);

    final var publicKey = privateKey.getPublicKey();
    Assertions.assertThat(publicKey.verify(message.getBytes(), signature)).isTrue();
  }

  // Equality and Hashcode checks.

  @Test
  @DisplayName("Should return true if the keys are same")
  void shouldReturnTrueIfKeysAreEquals() {
    final var key1 = ED25519PrivateKey.generate().getPublicKey();
    final var key2 = ED25519PrivateKey.generate().getPublicKey();
    final var key3 = ED25519PublicKey.fromBytes(key1.getDERBytes());

    Assertions.assertThat(key1.equals(key2)).isFalse();
    Assertions.assertThat(key1.equals(key3)).isTrue();
    Assertions.assertThat(key2.equals(key3)).isFalse();
  }

  @Test
  @DisplayName("Should return same hash for the same key")
  void shouldReturnSameHashForSameKey() {
    final var key1 = ED25519PrivateKey.generate().getPublicKey();
    final var key2 = ED25519PrivateKey.generate().getPublicKey();
    final var key3 = ED25519PublicKey.fromBytes(key1.getDERBytes());

    Assertions.assertThat(key1.hashCode()).isEqualTo(key3.hashCode());
    Assertions.assertThat(key1.hashCode()).isNotEqualTo(key2.hashCode());
    Assertions.assertThat(key2.hashCode()).isNotEqualTo(key3.hashCode());
  }
}
