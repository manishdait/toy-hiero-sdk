package com.example.sdk.internal;

import org.assertj.core.api.Assertions;
import org.bouncycastle.util.encoders.Hex;
import org.example.sdk.internal.ECDSAPrivateKey;
import org.example.sdk.key.KeyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ECDSAPrivateKeyTest {
  // Generate key.

  @Test
  @DisplayName("Should generate an ECDSA private key")
  void shouldGenerateECDSAPrivateKey() {
    final var privateKey = ECDSAPrivateKey.generate();
    Assertions.assertThat(privateKey).isNotNull();
    Assertions.assertThat(privateKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(privateKey.getBytes()).hasSize(32);
  }

  // Parse key from bytes.

  @Test
  @DisplayName("Should derive an ECDSA private key from bytes")
  void shouldDeriveECDSAPrivateKeyFromBytes() {
    final var bytes = Hex.decode("a1".repeat(32));
    final var privateKey = ECDSAPrivateKey.fromBytes(bytes);

    Assertions.assertThat(privateKey).isNotNull();
    Assertions.assertThat(privateKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(privateKey.getBytes()).hasSize(32);
  }

  @Test
  @DisplayName("Should derive an ECDSA private key from DER bytes")
  void shouldDeriveECDSAPrivateKeyFromDerBytes() {
    final var derStr = "30740201010" +
      "4200000000000000000000000000000000000000000000000000000000000000001" +
      "a00706052b8104000aa1440342000479be667ef9dcbbac55a06295ce870b07029bfc" +
      "db2dce28d959f2815b16f81798483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8";

    final var bytes = Hex.decode(derStr);
    final var privateKey = ECDSAPrivateKey.fromBytes(bytes);

    Assertions.assertThat(privateKey).isNotNull();
    Assertions.assertThat(privateKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(privateKey.getBytes()).hasSize(32);
  }

  @Test
  @DisplayName("Should round-trip ECDSA private key via bytes")
  void roundTripToDeriveECDSAPrivateKeyFromBytes() {
    final var privateKey = ECDSAPrivateKey.generate();
    final var bytes = privateKey.getBytes();

    final var newPrivateKey = ECDSAPrivateKey.fromBytes(bytes);

    Assertions.assertThat(newPrivateKey).isNotNull();
    Assertions.assertThat(newPrivateKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(newPrivateKey.getBytes())
      .hasSize(32)
      .isEqualTo(privateKey.getBytes());
  }

  @Test
  @DisplayName("Should round-trip ECDSA private key via DER bytes")
  void roundTripToDeriveECDSAPrivateKeyFromDerBytes() {
    final var privateKey = ECDSAPrivateKey.generate();
    final var derBytes = privateKey.getDERBytes();

    final var newPrivateKey = ECDSAPrivateKey.fromBytes(derBytes);

    Assertions.assertThat(newPrivateKey).isNotNull();
    Assertions.assertThat(newPrivateKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(newPrivateKey.getBytes())
      .hasSize(32)
      .isEqualTo(privateKey.getBytes());
  }

  @Test
  @DisplayName("Should raise an exception creating ECDSA key from invalid bytes")
  void shouldRaiseErrorOnECDSAPrivateKeyFromInvalidBytes() {
    final var bytes = Hex.decode("a1".repeat(31));
    Assertions.assertThatThrownBy(() -> ECDSAPrivateKey.fromBytes(bytes))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid ECDSA private key encoding");
  }

  @Test
  @DisplayName("Should raise an exception creating ED255519 key from invalid Der bytes")
  void shouldRaiseErrorOnECDSAPrivateKeyFromInvalidDerBytes() {
    final var bytes = Hex.decode("302c020100300506032b65700420" + "a1".repeat(31));
    Assertions.assertThatThrownBy(() -> ECDSAPrivateKey.fromBytes(bytes))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid ECDSA private key encoding");
  }

  @Test
  @DisplayName("Should raise an exception creating ED255519 key from invalid length Der bytes")
  void shouldRaiseErrorOnECDSAPrivateKeyFromInvalidLengthDerBytes() {
    final var bytes = Hex.decode("302e020100300506032b8104000a04220420" + "a1".repeat(32));
    Assertions.assertThatThrownBy(() -> ECDSAPrivateKey.fromBytes(bytes))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid ECDSA private key encoding");
  }

  // Parse from string.

  @Test
  @DisplayName("Should derive an ECDSA private key from hexadecimal string")
  void shouldDeriveECDSAPrivateKeyFromString() {
    final var hex = "a1".repeat(32);
    final var privateKey = ECDSAPrivateKey.fromString(hex);

    Assertions.assertThat(privateKey).isNotNull();
    Assertions.assertThat(privateKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(privateKey.getBytes()).hasSize(32);
  }

  @Test
  @DisplayName("Should derive an ECDSA private key from DER string")
  void shouldDeriveECDSAPrivateKeyFromDerString() {
    final var derHex = "302e020100300506032b6570042204200101010101010101010101010101010101010101010101010101010101010101";
    final var privateKey = ECDSAPrivateKey.fromString(derHex);

    Assertions.assertThat(privateKey).isNotNull();
    Assertions.assertThat(privateKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(privateKey.getBytes()).hasSize(32);
  }

  @Test
  @DisplayName("Should round-trip ECDSA private key via string")
  void roundTripToDeriveECDSAPrivateKeyFromString() {
    final var privateKey = ECDSAPrivateKey.generate();
    final var hex = privateKey.toHexString();

    final var newPrivateKey = ECDSAPrivateKey.fromString(hex);

    Assertions.assertThat(newPrivateKey).isNotNull();
    Assertions.assertThat(newPrivateKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(newPrivateKey.getBytes())
      .hasSize(32)
      .isEqualTo(privateKey.getBytes());
  }

  @Test
  @DisplayName("Should round-trip ECDSA private key via DER string")
  void roundTripToDeriveECDSAPrivateKeyFromDerString() {
    final var privateKey = ECDSAPrivateKey.generate();
    final var derHex = privateKey.toDERHex();

    final var newPrivateKey = ECDSAPrivateKey.fromString(derHex);

    Assertions.assertThat(newPrivateKey).isNotNull();
    Assertions.assertThat(newPrivateKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(newPrivateKey.getBytes())
      .hasSize(32)
      .isEqualTo(privateKey.getBytes());
  }

  @Test
  @DisplayName("Should raise an exception creating ED255519 key from invalid string")
  void shouldRaiseErrorOnECDSAPrivateKeyFromInvalidHex() {
    final var hex = "a1".repeat(31);
    Assertions.assertThatThrownBy(() -> ECDSAPrivateKey.fromString(hex))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid ECDSA private key encoding");
  }

  @Test
  @DisplayName("Should raise an exception creating ED255519 key from invalid Der hex")
  void shouldRaiseErrorOnECDSAPrivateKeyFromInvalidDerHex() {
    final var derHex = "302c020100300506032b65700420" + "a1".repeat(31);
    Assertions.assertThatThrownBy(() -> ECDSAPrivateKey.fromString(derHex))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid ECDSA private key encoding");
  }

  @Test
  @DisplayName("Should raise an exception creating ED255519 key from invalid length Der hex")
  void shouldRaiseErrorOnECDSAPrivateKeyFromInvalidLengthDerHex() {
    final var derHex = "302e020100300506032b8104000a04220420" + "a1".repeat(32);
    Assertions.assertThatThrownBy(() -> ECDSAPrivateKey.fromString(derHex))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid ECDSA private key encoding");
  }

  // Derive public key.

  @Test
  @DisplayName("Should derive a ECDSA public key for corresponding private key")
  void shouldDeriveECDSAPublicKeyFromPrivateKey() {
    final var privateKey = ECDSAPrivateKey.generate();
    final var publicKey = privateKey.getPublicKey();

    Assertions.assertThat(publicKey).isNotNull();
    Assertions.assertThat(publicKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(publicKey.getBytes().length).isEqualTo(33);
  }

  // To string methods.

  @Test
  @DisplayName("Should return the hex representation of the private key")
  void shouldReturnHexRepresentationOfPrivateKey() {
    final var privateKey = ECDSAPrivateKey.generate();
    final var hexStr = privateKey.toHexString();

    Assertions.assertThat(hexStr).isNotNull();
  }

  @Test
  @DisplayName("Should return the Der hex representation of the private key")
  void shouldReturnDerHexRepresentationOfPrivateKey() {
    final var privateKey = ECDSAPrivateKey.generate();
    final var hexStr = privateKey.toDERHex();

    Assertions.assertThat(hexStr).isNotNull();
  }

  // Sign message.

  @Test
  @DisplayName("Should sign the message using the private key")
  void shouldSignMessageWithECDSAPrivateKey() {
    final var message = "Hello World";
    final var privateKey = ECDSAPrivateKey.generate();

    final var signature = privateKey.sign(message.getBytes());
    Assertions.assertThat(signature)
      .isNotNull()
      .hasSize(64);

    // Verify signature with the public key
    final var publicKey = privateKey.getPublicKey();
    Assertions.assertThat(publicKey.verify(message.getBytes(), signature)).isTrue();
  }

  // Equality and Hashcode checks.

  @Test
  @DisplayName("Should return true if the keys are same")
  void shouldReturnTrueIfKeysAreEquals() {
    final var key1 = ECDSAPrivateKey.generate();
    final var key2 = ECDSAPrivateKey.generate();
    final var key3 = ECDSAPrivateKey.fromBytes(key1.getDERBytes());

    Assertions.assertThat(key1.equals(key2)).isFalse();
    Assertions.assertThat(key1.equals(key3)).isTrue();
    Assertions.assertThat(key2.equals(key3)).isFalse();
  }

  @Test
  @DisplayName("Should return same has for the same key")
  void shouldReturnSameHashForSameKey() {
    final var key1 = ECDSAPrivateKey.generate();
    final var key2 = ECDSAPrivateKey.generate();
    final var key3 = ECDSAPrivateKey.fromBytes(key1.getDERBytes());

    Assertions.assertThat(key1.hashCode()).isEqualTo(key3.hashCode());
    Assertions.assertThat(key1.hashCode()).isNotEqualTo(key2.hashCode());
    Assertions.assertThat(key2.hashCode()).isNotEqualTo(key3.hashCode());
  }
}
