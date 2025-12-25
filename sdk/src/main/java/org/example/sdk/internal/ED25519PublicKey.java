package org.example.sdk.internal;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.proto.Key;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.bouncycastle.util.encoders.Hex;
import org.example.sdk.key.KeyType;
import org.example.sdk.key.PublicKey;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents an Ed25519 public key.
 */
public final class ED25519PublicKey implements PublicKey {
  private static final ASN1ObjectIdentifier ED25519_OID = new ASN1ObjectIdentifier("1.3.101.112");
  private final byte[] bytes;

  /**
   * Creates a new Ed25519 public key from raw key bytes.
   *
   * @param bytes the raw 32-byte Ed25519 public key
   */
  private ED25519PublicKey(final byte[] bytes) {
    this.bytes = bytes.clone();
  }

  /**
   * Parse an Ed25519 public key from its byte representation.
   *
   * @param bytes the public key bytes
   * @return the parsed {@link ED25519PublicKey}
   * @throws RuntimeException if the bytes are invalid or not Ed25519
   */
  public static @NonNull ED25519PublicKey fromBytes(final byte[] bytes) {
    if (bytes.length == Ed25519.PUBLIC_KEY_SIZE) {
      return new ED25519PublicKey(bytes);
    }

    try {
      final var spki = SubjectPublicKeyInfo.getInstance(bytes);

      if (!spki.getAlgorithm().getAlgorithm().equals(ED25519_OID)) {
        throw new RuntimeException("Not an Ed25519 public key");
      }

      final byte[] keyBytes = spki.getPublicKeyData().getBytes();
      if (keyBytes.length != Ed25519.PUBLIC_KEY_SIZE) {
        throw new RuntimeException("Invalid Ed25519 public key length");
      }

      return new ED25519PublicKey(keyBytes);
    } catch (Exception e) {
      throw new RuntimeException("Invalid Ed25519 public key encoding", e);
    }
  }

  /**
   * Parse an Ed25519 public key from a hexadecimal string.
   *
   * @param str hexadecimal representation of the public key
   * @return the parsed {@link ED25519PublicKey}
   * @throws RuntimeException if the hexadecimal string is invalid
   */
  public static @NonNull ED25519PublicKey fromString(@NonNull final String str) {
    Objects.requireNonNull(str, "str must not be null");
    return fromBytes(Hex.decode(str.replaceFirst("^0x", "")));
  }

  /**
   * Converts this public key to its protobuf representation.
   *
   * @return protobuf {@link Key} representation
   */
  @Override
  public Key toProto() {
    return Key.newBuilder()
      .setEd25519(ByteString.copyFrom(this.bytes))
      .build();
  }

  /**
   * Returns the raw 32-byte Ed25519 public key.
   *
   * @return a copy of the public key bytes
   */
  @Override
  public byte[] getBytes() {
    return this.bytes.clone();
  }

  /**
   * Returns the DER encoding of this Ed25519 public key.
   *
   * @return a byte array containing the DER-encoded representation of this Ed25519 public key
   * @throws RuntimeException if the key cannot be encoded to DER
   */
  @Override
  public byte[] getDERBytes() {
    try {
      final var alg = new AlgorithmIdentifier(ED25519_OID);
      final var spki = new SubjectPublicKeyInfo(alg, this.getBytes());

      return spki.getEncoded();
    } catch (Exception e) {
      throw new RuntimeException("Failed to encode Ed25519 public key to DER", e);
    }
  }

  /**
   * Returns the hexadecimal string representation of the raw Ed25519 public key bytes.
   *
   * @return a hexadecimal string representation of the 32-byte Ed25519 public key bytes
   */
  @Override
  public @NonNull String toHexString() {
    return Hex.toHexString(this.getBytes());
  }

  /**
   * Returns the hexadecimal string representation of this Ed25519 public key encoded in DER format.
   *
   * @return a hexadecimal string of the DER-encoded Ed25519 public key
   */
  @Override
  public @NonNull String toDERHex() {
    return Hex.toHexString(this.getDERBytes());
  }

  /**
   * Returns the type of public key.
   *
   * @return the {@link KeyType} for the public key
   */
  @Override
  public @NonNull KeyType getType() {
    return KeyType.ED25519;
  }

  /**
   * Verifies an Ed25519 signature over the given message.
   *
   * @param message the original message that was signed
   * @param signature the 64-byte Ed25519 signature to verify
   * @return {@code true} if the signature is valid for the given message and this public key; {@code false} otherwise
   */
  @Override
  public boolean verify(final byte[] message, final byte[] signature) {
    if (signature.length != Ed25519.SIGNATURE_SIZE) {
      return false;
    }

    return Ed25519.verify(signature, 0, this.bytes, 0, message, 0, message.length);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.bytes);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null || this.getClass() !=o.getClass()) return false;
    return Arrays.equals(this.bytes, ((ED25519PublicKey) o).bytes);
  }
}
