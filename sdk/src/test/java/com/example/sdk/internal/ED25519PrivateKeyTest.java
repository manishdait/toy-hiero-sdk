package com.example.sdk.internal;

import org.assertj.core.api.Assertions;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.bouncycastle.util.encoders.Hex;
import org.example.sdk.internal.ED25519PrivateKey;
import org.example.sdk.key.KeyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ED25519PrivateKeyTest {
  // Generate key.

  @Test
  @DisplayName("Should generate an ED25519 private key")
  void shouldGenerateEd25519PrivateKey() {
    final var privateKey = ED25519PrivateKey.generate();
    Assertions.assertThat(privateKey).isNotNull();
    Assertions.assertThat(privateKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(privateKey.getBytes()).hasSize(Ed25519.SECRET_KEY_SIZE);
  }

  // Parse key from bytes.

  @Test
  @DisplayName("Should derive an ED25519 private key from bytes")
  void shouldDeriveEd25519PrivateKeyFromBytes() {
    final var bytes = Hex.decode("a1".repeat(32));
    final var privateKey = ED25519PrivateKey.fromBytes(bytes);

    Assertions.assertThat(privateKey).isNotNull();
    Assertions.assertThat(privateKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(privateKey.getBytes()).hasSize(Ed25519.SECRET_KEY_SIZE);
  }

  @Test
  @DisplayName("Should derive an ED25519 private key from DER bytes")
  void shouldDeriveEd25519PrivateKeyFromDerBytes() {
    final var derStr = "302e020100300506032b6570042204200101010101010101010101010101010101010101010101010101010101010101";
    final var bytes = Hex.decode(derStr);
    final var privateKey = ED25519PrivateKey.fromBytes(bytes);

    Assertions.assertThat(privateKey).isNotNull();
    Assertions.assertThat(privateKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(privateKey.getBytes()).hasSize(Ed25519.SECRET_KEY_SIZE);
  }

  @Test
  @DisplayName("Should round-trip Ed25519 private key via bytes")
  void roundTripToDeriveEd25519PrivateKeyFromBytes() {
    final var privateKey = ED25519PrivateKey.generate();
    final var bytes = privateKey.getBytes();

    final var newPrivateKey = ED25519PrivateKey.fromBytes(bytes);

    Assertions.assertThat(newPrivateKey).isNotNull();
    Assertions.assertThat(newPrivateKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(newPrivateKey.getBytes())
      .hasSize(Ed25519.SECRET_KEY_SIZE)
      .isEqualTo(privateKey.getBytes());
  }

  @Test
  @DisplayName("Should round-trip Ed25519 private key via DER bytes")
  void roundTripToDeriveEd25519PrivateKeyFromDerBytes() {
    final var privateKey = ED25519PrivateKey.generate();
    final var derBytes = privateKey.getDERBytes();

    final var newPrivateKey = ED25519PrivateKey.fromBytes(derBytes);

    Assertions.assertThat(newPrivateKey).isNotNull();
    Assertions.assertThat(newPrivateKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(newPrivateKey.getBytes())
      .hasSize(Ed25519.SECRET_KEY_SIZE)
      .isEqualTo(privateKey.getBytes());
  }

  @Test
  @DisplayName("Should raise an exception creating ED255519 key from invalid bytes")
  void shouldRaiseErrorOnEd25519PrivateKeyFromInvalidBytes() {
    final var bytes = Hex.decode("a1".repeat(31));
    Assertions.assertThatThrownBy(() -> ED25519PrivateKey.fromBytes(bytes))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid Ed25519 private key encoding");
  }

  @Test
  @DisplayName("Should raise an exception creating ED255519 key from invalid Der bytes")
  void shouldRaiseErrorOnEd25519PrivateKeyFromInvalidDerBytes() {
    final var bytes = Hex.decode("302c020100300506032b65700420" + "a1".repeat(31));
    Assertions.assertThatThrownBy(() -> ED25519PrivateKey.fromBytes(bytes))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid Ed25519 private key encoding");
  }

  @Test
  @DisplayName("Should raise an exception creating ED255519 key from invalid length Der bytes")
  void shouldRaiseErrorOnEd25519PrivateKeyFromInvalidLengthDerBytes() {
    final var bytes = Hex.decode("302e020100300506032b8104000a04220420" + "a1".repeat(32));
    Assertions.assertThatThrownBy(() -> ED25519PrivateKey.fromBytes(bytes))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid Ed25519 private key encoding");
  }

  // Parse from string.

  @Test
  @DisplayName("Should derive an ED25519 private key from hexadecimal string")
  void shouldDeriveEd25519PrivateKeyFromString() {
    final var hex = "a1".repeat(32);
    final var privateKey = ED25519PrivateKey.fromString(hex);

    Assertions.assertThat(privateKey).isNotNull();
    Assertions.assertThat(privateKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(privateKey.getBytes()).hasSize(Ed25519.SECRET_KEY_SIZE);
  }

  @Test
  @DisplayName("Should derive an ED25519 private key from DER string")
  void shouldDeriveEd25519PrivateKeyFromDerString() {
    final var derHex = "302e020100300506032b6570042204200101010101010101010101010101010101010101010101010101010101010101";
    final var privateKey = ED25519PrivateKey.fromString(derHex);

    Assertions.assertThat(privateKey).isNotNull();
    Assertions.assertThat(privateKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(privateKey.getBytes()).hasSize(Ed25519.SECRET_KEY_SIZE);
  }

  @Test
  @DisplayName("Should round-trip Ed25519 private key via string")
  void roundTripToDeriveEd25519PrivateKeyFromString() {
    final var privateKey = ED25519PrivateKey.generate();
    final var hex = privateKey.toHexString();

    final var newPrivateKey = ED25519PrivateKey.fromString(hex);

    Assertions.assertThat(newPrivateKey).isNotNull();
    Assertions.assertThat(newPrivateKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(newPrivateKey.getBytes())
      .hasSize(Ed25519.SECRET_KEY_SIZE)
      .isEqualTo(privateKey.getBytes());
  }

  @Test
  @DisplayName("Should round-trip Ed25519 private key via DER string")
  void roundTripToDeriveEd25519PrivateKeyFromDerString() {
    final var privateKey = ED25519PrivateKey.generate();
    final var derHex = privateKey.toDERHex();

    final var newPrivateKey = ED25519PrivateKey.fromString(derHex);

    Assertions.assertThat(newPrivateKey).isNotNull();
    Assertions.assertThat(newPrivateKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(newPrivateKey.getBytes())
      .hasSize(Ed25519.SECRET_KEY_SIZE)
      .isEqualTo(privateKey.getBytes());
  }

  @Test
  @DisplayName("Should raise an exception creating ED255519 key from invalid string")
  void shouldRaiseErrorOnEd25519PrivateKeyFromInvalidHex() {
    final var hex = "a1".repeat(31);
    Assertions.assertThatThrownBy(() -> ED25519PrivateKey.fromString(hex))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid Ed25519 private key encoding");
  }

  @Test
  @DisplayName("Should raise an exception creating ED255519 key from invalid Der hex")
  void shouldRaiseErrorOnEd25519PrivateKeyFromInvalidDerHex() {
    final var derHex = "302c020100300506032b65700420" + "a1".repeat(31);
    Assertions.assertThatThrownBy(() -> ED25519PrivateKey.fromString(derHex))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid Ed25519 private key encoding");
  }

  @Test
  @DisplayName("Should raise an exception creating ED255519 key from invalid length Der hex")
  void shouldRaiseErrorOnEd25519PrivateKeyFromInvalidLengthDerHex() {
    final var derHex = "302e020100300506032b8104000a04220420" + "a1".repeat(32);
    Assertions.assertThatThrownBy(() -> ED25519PrivateKey.fromString(derHex))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid Ed25519 private key encoding");
  }

  // Derive public key.

  @Test
  @DisplayName("Should derive a ED25519 public key for corresponding private key")
  void shouldDeriveED25519PublicKeyFromPrivateKey() {
    final var privateKey = ED25519PrivateKey.generate();
    final var publicKey = privateKey.getPublicKey();

    Assertions.assertThat(publicKey).isNotNull();
    Assertions.assertThat(publicKey.getType()).isEqualTo(KeyType.ED25519);
    Assertions.assertThat(publicKey.getBytes().length).isEqualTo(Ed25519.PUBLIC_KEY_SIZE);
  }

  // To string methods.

  @Test
  @DisplayName("Should return the hex representation of the private key")
  void shouldReturnHexRepresentationOfPrivateKey() {
    final var privateKey = ED25519PrivateKey.generate();
    final var hexStr = privateKey.toHexString();

    Assertions.assertThat(hexStr).isNotNull();
  }

  @Test
  @DisplayName("Should return the Der hex representation of the private key")
  void shouldReturnDerHexRepresentationOfPrivateKey() {
    final var privateKey = ED25519PrivateKey.generate();
    final var hexStr = privateKey.toDERHex();

    Assertions.assertThat(hexStr).isNotNull();
  }

  // Sign message.

  @Test
  @DisplayName("Should sign the message using the private key")
  void shouldSignMessageWithEd25519PrivateKey() {
    final var message = "Hello World";
    final var privateKey = ED25519PrivateKey.generate();

    final var signature = privateKey.sign(message.getBytes());
    Assertions.assertThat(signature)
      .isNotNull()
      .hasSize(Ed25519.SIGNATURE_SIZE);

    // Verify signature with the public key
    final var publicKey = privateKey.getPublicKey();
    Assertions.assertThat(publicKey.verify(message.getBytes(), signature)).isTrue();
  }

  // Equality and Hashcode checks.

  @Test
  @DisplayName("Should return true if the keys are same")
  void shouldReturnTrueIfKeysAreEquals() {
    final var key1 = ED25519PrivateKey.generate();
    final var key2 = ED25519PrivateKey.generate();
    final var key3 = ED25519PrivateKey.fromBytes(key1.getDERBytes());

    Assertions.assertThat(key1.equals(key2)).isFalse();
    Assertions.assertThat(key1.equals(key3)).isTrue();
    Assertions.assertThat(key2.equals(key3)).isFalse();
  }

  @Test
  @DisplayName("Should return same has for the same key")
  void shouldReturnSameHashForSameKey() {
    final var key1 = ED25519PrivateKey.generate();
    final var key2 = ED25519PrivateKey.generate();
    final var key3 = ED25519PrivateKey.fromBytes(key1.getDERBytes());

    Assertions.assertThat(key1.hashCode()).isEqualTo(key3.hashCode());
    Assertions.assertThat(key1.hashCode()).isNotEqualTo(key2.hashCode());
    Assertions.assertThat(key2.hashCode()).isNotEqualTo(key3.hashCode());
  }
}
