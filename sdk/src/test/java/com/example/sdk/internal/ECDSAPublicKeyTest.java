package com.example.sdk.internal;

import org.assertj.core.api.Assertions;
import org.bouncycastle.util.encoders.Hex;
import org.example.sdk.internal.key.ECDSAPrivateKey;
import org.example.sdk.internal.key.ECDSAPublicKey;
import org.example.sdk.key.KeyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ECDSAPublicKeyTest {
  // Derive public key.

  @Test
  @DisplayName("Should derive a ECDSA public key from private key")
  void shouldDerivePublicKeyFromPrivateKey() {
    final var privateKey = ECDSAPrivateKey.generate();
    final var publicKey = privateKey.getPublicKey();

    Assertions.assertThat(publicKey).isNotNull();
    Assertions.assertThat(publicKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(publicKey.getBytes()).hasSize(33);
  }

  // Parse public key from bytes.

  @Test
  @DisplayName("Should parse a ECDSA public key from bytes")
  void shouldParsePublicKeyFromBytes() {
    final var bytes = Hex.decode("0281c2e57fecef82ff4f546dece3684acb6e2fe12a97af066348de81ccaf05d0a4");
    final var publicKey = ECDSAPublicKey.fromBytes(bytes);

    Assertions.assertThat(publicKey).isNotNull();
    Assertions.assertThat(publicKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(publicKey.getBytes()).hasSize(33);
  }

  @Test
  @DisplayName("Should parse a ECDSA public key from uncompress bytes")
  void shouldParsePublicKeyFromUncompressBytes() {
    final var hex = "04" +
      "0abe0517fcf06e0c160ca821aa2909945752e08169f46c984cb6b02076a3b" +
      "29513f047e5c13770101c321f332157377d2862b7c7ed14eedca3978b3b9d007659";

    final var bytes = Hex.decode(hex);
    final var publicKey = ECDSAPublicKey.fromBytes(bytes);

    Assertions.assertThat(publicKey).isNotNull();
    Assertions.assertThat(publicKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(publicKey.getBytes()).hasSize(33);
  }

  @Test
  @DisplayName("Should parse a ECDSA public key from DER bytes")
  void shouldParsePublicKeyFromDerBytes() {
    final var hex = "302d300706052b8104000a032200024f832d18d92c9d967afa32e0655400b16b2993f5629bd6837b4ad82755e52a02";
    final var bytes = Hex.decode(hex);
    final var publicKey = ECDSAPublicKey.fromBytes(bytes);

    Assertions.assertThat(publicKey).isNotNull();
    Assertions.assertThat(publicKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(publicKey.getBytes()).hasSize(33);
  }

  @Test
  @DisplayName("Should round-trip ECDSA public key via bytes")
  void roundTripToDeriveECDSAPublicKeyViaBytes() {
    final var privateKey = ECDSAPrivateKey.generate();
    final var publicKey = privateKey.getPublicKey();
    final var bytes = publicKey.getBytes();

    final var newPublicKey = ECDSAPublicKey.fromBytes(bytes);

    Assertions.assertThat(newPublicKey).isNotNull();
    Assertions.assertThat(newPublicKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(newPublicKey.getBytes().length).isEqualTo(33);
    Assertions.assertThat(newPublicKey.getBytes()).isEqualTo(publicKey.getBytes());
  }

  @Test
  @DisplayName("Should round-trip ECDSA public key via DER bytes")
  void roundTripToDeriveECDSAPublicKeyViaDerBytes() {
    final var privateKey = ECDSAPrivateKey.generate();
    final var publicKey = privateKey.getPublicKey();
    final var bytes = publicKey.getDERBytes();

    final var newPublicKey = ECDSAPublicKey.fromBytes(bytes);

    Assertions.assertThat(newPublicKey).isNotNull();
    Assertions.assertThat(newPublicKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(newPublicKey.getBytes().length).isEqualTo(33);
    Assertions.assertThat(newPublicKey.getBytes()).isEqualTo(publicKey.getBytes());
  }

  @Test
  @DisplayName("Should raise error when parsing ECDSA public key from invalid bytes")
  void shouldRaiseErrorWhenParsePublicKeyFromInvalidBytes() {
    final var bytes = Hex.decode("a1".repeat(31));
    Assertions.assertThatThrownBy(() -> ECDSAPublicKey.fromBytes(bytes))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid ECDSA public key length");
  }

  @Test
  @DisplayName("Should raise error when parsing ECDSA public key from invalid Der bytes")
  void shouldRaiseErrorWhenParsePublicKeyFromInvalidDerBytes() {
    final var bytes = Hex.decode("302a300506032b6570032100" + "11".repeat(31));
    Assertions.assertThatThrownBy(() -> ECDSAPublicKey.fromBytes(bytes))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid ECDSA public key length");
  }

  // Parse public key from string.

  @Test
  @DisplayName("Should parse a ECDSA public key from string")
  void shouldParsePublicKeyFromString() {
    final var hex = "0281c2e57fecef82ff4f546dece3684acb6e2fe12a97af066348de81ccaf05d0a4";
    final var publicKey = ECDSAPublicKey.fromString(hex);

    Assertions.assertThat(publicKey).isNotNull();
    Assertions.assertThat(publicKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(publicKey.getBytes()).hasSize(33);
  }

  @Test
  @DisplayName("Should parse a ECDSA public key from uncompress hex")
  void shouldParsePublicKeyFromUncompressString() {
    final var hex = "04" +
      "0abe0517fcf06e0c160ca821aa2909945752e08169f46c984cb6b02076a3b" +
      "29513f047e5c13770101c321f332157377d2862b7c7ed14eedca3978b3b9d007659";

    final var publicKey = ECDSAPublicKey.fromString(hex);

    Assertions.assertThat(publicKey).isNotNull();
    Assertions.assertThat(publicKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(publicKey.getBytes()).hasSize(33);
  }

  @Test
  @DisplayName("Should parse a ECDSA public key from DER string")
  void shouldParsePublicKeyFromDerString() {
    final var derHex = "302d300706052b8104000a032200024f832d18d92c9d967afa32e0655400b16b2993f5629bd6837b4ad82755e52a02";
    final var publicKey = ECDSAPublicKey.fromString(derHex);

    Assertions.assertThat(publicKey).isNotNull();
    Assertions.assertThat(publicKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(publicKey.getBytes()).hasSize(33);
  }

  @Test
  @DisplayName("Should round-trip ECDSA public key via hex")
  void roundTripToDeriveECDSAPublicKeyViaString() {
    final var privateKey = ECDSAPrivateKey.generate();
    final var publicKey = privateKey.getPublicKey();
    final var hex = publicKey.toHexString();

    final var newPublicKey = ECDSAPublicKey.fromString(hex);

    Assertions.assertThat(newPublicKey).isNotNull();
    Assertions.assertThat(newPublicKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(newPublicKey.getBytes().length).isEqualTo(33);
    Assertions.assertThat(newPublicKey.getBytes()).isEqualTo(publicKey.getBytes());
  }

  @Test
  @DisplayName("Should round-trip ECDSA public key via DER hex")
  void roundTripToDeriveECDSAPublicKeyViaDerHex() {
    final var privateKey = ECDSAPrivateKey.generate();
    final var publicKey = privateKey.getPublicKey();
    final var derHex = publicKey.toDERHex();

    final var newPublicKey = ECDSAPublicKey.fromString(derHex);

    Assertions.assertThat(newPublicKey).isNotNull();
    Assertions.assertThat(newPublicKey.getType()).isEqualTo(KeyType.ECDSA);
    Assertions.assertThat(newPublicKey.getBytes().length).isEqualTo(33);
    Assertions.assertThat(newPublicKey.getBytes()).isEqualTo(publicKey.getBytes());
  }

  @Test
  @DisplayName("Should raise error when parsing ECDSA public key from invalid hex")
  void shouldRaiseErrorWhenParsePublicKeyFromInvalidHex() {
    final var hex = "a1".repeat(31);
    Assertions.assertThatThrownBy(() -> ECDSAPublicKey.fromString(hex))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid ECDSA public key length");
  }

  @Test
  @DisplayName("Should raise error when parsing ECDSA public key from invalid Der hex")
  void shouldRaiseErrorWhenParsePublicKeyFromInvalidDerHex() {
    final var derHex = "302a300506032b6570032100" + "11".repeat(31);
    Assertions.assertThatThrownBy(() -> ECDSAPublicKey.fromString(derHex))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Invalid ECDSA public key length");
  }

  // To string methods.

  @Test
  @DisplayName("Should return the hex representation of the public key")
  void shouldReturnHexRepresentationOfPublicKey() {
    final var publicKey = ECDSAPrivateKey.generate().getPublicKey();
    final var hexStr = publicKey.toHexString();

    Assertions.assertThat(hexStr).isNotNull();
  }

  @Test
  @DisplayName("Should return the Der hex representation of the public key")
  void shouldReturnDerHexRepresentationOfPublicKey() {
    final var publicKey = ECDSAPrivateKey.generate().getPublicKey();
    final var hexStr = publicKey.toDERHex();

    Assertions.assertThat(hexStr).isNotNull();
  }

  // Verify signature.

  @Test
  @DisplayName("Should verify the signature using the public key")
  void shouldVerifySignatureWithECDSAPublicKey() {
    final var message = "Hello World";
    final var privateKey = ECDSAPrivateKey.generate();

    final var signature = privateKey.sign(message.getBytes());
    Assertions.assertThat(signature)
      .isNotNull()
      .hasSize(64);

    final var publicKey = privateKey.getPublicKey();
    Assertions.assertThat(publicKey.verify(message.getBytes(), signature)).isTrue();
  }

  // Equality and Hashcode checks.

  @Test
  @DisplayName("Should return true if the keys are same")
  void shouldReturnTrueIfKeysAreEquals() {
    final var key1 = ECDSAPrivateKey.generate().getPublicKey();
    final var key2 = ECDSAPrivateKey.generate().getPublicKey();
    final var key3 = ECDSAPublicKey.fromBytes(key1.getDERBytes());

    Assertions.assertThat(key1.equals(key2)).isFalse();
    Assertions.assertThat(key1.equals(key3)).isTrue();
    Assertions.assertThat(key2.equals(key3)).isFalse();
  }

  @Test
  @DisplayName("Should return same hash for the same key")
  void shouldReturnSameHashForSameKey() {
    final var key1 = ECDSAPrivateKey.generate().getPublicKey();
    final var key2 = ECDSAPrivateKey.generate().getPublicKey();
    final var key3 = ECDSAPublicKey.fromBytes(key1.getDERBytes());

    Assertions.assertThat(key1.hashCode()).isEqualTo(key3.hashCode());
    Assertions.assertThat(key1.hashCode()).isNotEqualTo(key2.hashCode());
    Assertions.assertThat(key2.hashCode()).isNotEqualTo(key3.hashCode());
  }
}
