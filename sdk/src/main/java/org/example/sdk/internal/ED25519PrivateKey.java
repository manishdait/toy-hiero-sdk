package org.example.sdk.internal;

import com.hedera.hashgraph.sdk.proto.Key;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.bouncycastle.util.encoders.Hex;
import org.example.sdk.key.KeyType;
import org.example.sdk.key.PrivateKey;
import org.example.sdk.key.PublicKey;
import org.jspecify.annotations.NonNull;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents an Ed25519 private key backed by a 32-byte seed.
 */
public final class ED25519PrivateKey implements PrivateKey {
  private static final ASN1ObjectIdentifier ED25519_OID = new ASN1ObjectIdentifier("1.3.101.112");
  private final byte[] seed;

  /**
   * Creates a new ED25519 private key from a raw 32-byte seed.
   *
   * @param seed the Ed25519 private key seed (32 bytes)
   */
  private ED25519PrivateKey(final byte[] seed) {
    this.seed = seed.clone();
  }

  /**
   * Generates a new Ed25519 private key.
   *
   * @return a new instance of generated {@link ED25519PrivateKey}
   */
  public static @NonNull ED25519PrivateKey generate() {
    byte[] seed = new byte[Ed25519.SECRET_KEY_SIZE];
    Ed25519.generatePrivateKey(new SecureRandom(), seed);
    return new ED25519PrivateKey(seed);
  }

  /**
   * Parses an Ed25519 private key from its byte representation.
   *
   * @param bytes the private key bytes
   * @return the parsed {@link ED25519PrivateKey}
   * @throws RuntimeException if the encoding is invalid or not Ed25519
   */
  public static @NonNull ED25519PrivateKey fromBytes(byte[] bytes) {
    if (bytes.length == Ed25519.SECRET_KEY_SIZE) {
      return new ED25519PrivateKey(bytes);
    }

    try {
      final var pki = PrivateKeyInfo.getInstance(bytes);
      if (!pki.getPrivateKeyAlgorithm().getAlgorithm().equals(ED25519_OID)) {
        throw new RuntimeException("Not an Ed25519 private key");
      }

      var seed = ((DEROctetString) pki.parsePrivateKey()).getOctets();
      if (seed.length != Ed25519.SECRET_KEY_SIZE) {
        throw new RuntimeException("Invalid Ed25519 seed length");
      }
      return new ED25519PrivateKey(seed);
    } catch (Exception e) {
      throw new RuntimeException("Invalid Ed25519 private key encoding", e);
    }
  }

  /**
   * Parses an Ed25519 private key from a hexadecimal string.
   *
   * @param str hexadecimal string of the private key
   * @return the parsed {@link ED25519PrivateKey}
   * @throws RuntimeException if the encoding is invalid or not Ed25519
   */
  public static @NonNull ED25519PrivateKey fromString(@NonNull final String str) {
    Objects.requireNonNull(str, "key must not be null");
    return fromBytes(Hex.decode(str.replaceFirst("^0x", "")));
  }

  /**
   * Derives the Ed25519 public key corresponding to this private key.
   *
   * @return the corresponding {@link ED25519PublicKey}
   */
  @Override
  public @NonNull PublicKey getPublicKey() {
    if (this.seed.length != Ed25519.SECRET_KEY_SIZE) {
      throw new RuntimeException("Invalid Ed25519 private key seed length");
    }

    final var bytes = new byte[Ed25519.PUBLIC_KEY_SIZE];
    Ed25519.generatePublicKey(this.seed.clone(), 0, bytes, 0);
    return ED25519PublicKey.fromBytes(bytes);
  }

  /**
   * Signs a message using Ed25519.
   *
   * @param message the message to sign
   * @return the 64-byte Ed25519 signature
   * @throws RuntimeException if the private key is invalid
   */
  @Override
  public byte[] sign(byte[] message) {
    final var seed = this.seed.clone();

    if (seed.length != Ed25519.SECRET_KEY_SIZE) {
      throw new RuntimeException("Invalid Ed25519 private key seed length");
    }

    final var signature = new byte[Ed25519.SIGNATURE_SIZE];
    Ed25519.sign(seed, 0, message, 0, message.length, signature, 0);
    return  signature;
  }

  /**
   * Returns the raw 32-byte Ed25519 private key seed.
   *
   * @return a copy of the private key seed
   */
  @Override
  public byte[] getBytes() {
    return this.seed.clone();
  }

  /**
   * Returns the type of private key.
   *
   * @return the {@link KeyType} for the private key
   */
  @Override
  public @NonNull KeyType getType() {
    return KeyType.ED25519;
  }

  /**
   * Returns the DER encoding of this Ed25519 private key.
   *
   * @return a byte array containing the DER-encoded representation of this Ed25519 private key
   * @throws RuntimeException if the key cannot be encoded to DER
   */
  @Override
  public byte[] getDERBytes() {
    try {
      final var alg = new AlgorithmIdentifier(ED25519_OID);
      final var pki = new PrivateKeyInfo(alg, new DEROctetString(this.getBytes()));

      return pki.getEncoded();
    } catch (Exception e) {
      throw new RuntimeException("Failed to encode Ed25519 private key to DER", e);
    }
  }

  /**
   * Returns the hexadecimal string representation of the raw Ed25519 private key seed.
   *
   * @return a hexadecimal string representation of the 32-byte Ed25519 private key seed
   */
  @Override
  public @NonNull String toHexString() {
    return Hex.toHexString(this.getBytes());
  }

  /**
   * Returns the hexadecimal string representation of this Ed25519 private key encoded in DER format.
   *
   * @return a hexadecimal string of the DER-encoded Ed25519 private key
   */
  @Override
  public @NonNull String toDERHex() {
    return Hex.toHexString(this.getDERBytes());
  }

  /**
   * Converts the key into protobuf representation {@link Key}.
   *
   * @return protobuf {@link Key} representation
   */
  @Override
  public @NonNull Key toProto() {
    return getPublicKey().toProto();
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.seed);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null || this.getClass() !=o.getClass()) return false;
    return Arrays.equals(this.seed, ((ED25519PrivateKey) o).seed);
  }
}
