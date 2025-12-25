package org.example.sdk.internal;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.proto.Key;
import org.bouncycastle.asn1.sec.SECObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
import org.example.sdk.key.KeyType;
import org.example.sdk.key.PublicKey;
import org.jspecify.annotations.NonNull;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represent an ECDSA public key using the secp256k1 curve.
 *
 * <p>
 *  30 2d                       2-bytes  Top-Level           (SEQUENCE LENGTH)
 *  30 07                       2-bytes  AlgorithmIdentifie  (SEQUENCE LENGTH)
 *  06 05 2b 81 04 00 0a        7-bytes  OID                 (ObjectIdentifier LENGTH 1×40+3→1.3 8104→132 00→0 0a→10) secp256k1
 *  03 22                       2-bytes  BitString PubKey    (BIT-STRING LENGTH)
 *  00                          1-bytes  Unused bits byte    (UNUSED-BITS)
 *  02 NEXT-32-BYTES            33-bytes EC public key point (compressed-EC-point  X-coordinate)
 * </p>
 *
 * <p>
 *  SEQUENCE tag + length	2
 *  AlgorithmIdentifier	9
 *  BIT STRING	36
 *  Total DER size	47 bytes
 * </p>
 */
public class ECDSAPublicKey implements PublicKey {
  private final byte[] bytes;

  /**
   * Creates a new ECDSA public key from raw key bytes.
   *
   * @param bytes the raw 32-byte ECDSA public key
   */
  private ECDSAPublicKey(byte[] bytes) {
    this.bytes = bytes.clone();
  }

  /**
   * Parse an ECDSA public key from its byte representation.
   *
   * @param bytes the public key bytes
   * @return the parsed {@link ECDSAPublicKey}
   * @throws RuntimeException if the bytes are invalid or not Ed25519
   */
  public static @NonNull ECDSAPublicKey fromBytes(final byte[] bytes) {
    ECPoint point;
    var curve = ECDSAPrivateKey.CURVE;

    if (bytes.length == 32) {
      throw new RuntimeException("Cannot generate public key from raw 32-byte scalar directly here. Use ECDSAPrivateKey.getPublicKey()");
    }

    try {
      var spki = SubjectPublicKeyInfo.getInstance(bytes);
      var pointBytes = spki.getPublicKeyData().getBytes();
      var p = curve.getCurve().decodePoint(pointBytes);
      return new ECDSAPublicKey(p.getEncoded(true));
    } catch (Exception ignored) {
      // fallback
    }

    if (bytes.length == 64) {
      byte[] uncompressed = new byte[65];
      uncompressed[0] = 0x04;
      System.arraycopy(bytes, 0, uncompressed, 1, 64);
      point = curve.getCurve().decodePoint(uncompressed);
    } else if (bytes.length == 65 && bytes[0] == 0x04) {
      point = curve.getCurve().decodePoint(bytes);
    } else if (bytes.length == 33 && (bytes[0] == 0x02 || bytes[0] == 0x03)) {
      point = curve.getCurve().decodePoint(bytes);
    } else {
      throw new IllegalArgumentException("Invalid ECDSA public key length");
    }

    return new ECDSAPublicKey(point.getEncoded(true));
  }

  /**
   * Parse an ECDSA public key from a hexadecimal string.
   *
   * @param str hexadecimal representation of the public key
   * @return the parsed {@link ECDSAPublicKey}
   * @throws RuntimeException if the hexadecimal string is invalid
   */
  public static @NonNull ECDSAPublicKey fromString(final @NonNull String str) {
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
      .setECDSASecp256K1(ByteString.copyFrom(bytes))
      .build();
  }

  /**
   * Returns the raw 32-byte ECDSA public key.
   *
   * @return a copy of the public key bytes
   */
  @Override
  public byte[] getBytes() {
    return this.bytes.clone();
  }

  /**
   * Returns the DER encoding of this ECDSA public key.
   *
   * @return a byte array containing the DER-encoded representation of this ECDSA public key
   * @throws RuntimeException if the key cannot be encoded to DER
   */
  @Override
  public byte[] getDERBytes() {
    try {
      var spki = new SubjectPublicKeyInfo(
        new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, SECObjectIdentifiers.secp256k1),
        this.bytes
      );

      return spki.getEncoded();
    } catch (Exception e) {
      throw new RuntimeException("Failed to encode ECDSA public key to DER", e);
    }
  }

  /**
   * Returns the hexadecimal string representation of the raw ECDSA public key bytes.
   *
   * @return a hexadecimal string representation of the 32-byte ECDSA public key bytes
   */
  @Override
  public @NonNull String toHexString() {
    return Hex.toHexString(this.getBytes());
  }

  /**
   * Returns the hexadecimal string representation of this ECDSA public key encoded in DER format.
   *
   * @return a hexadecimal string of the DER-encoded ECDSA public key
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
    return KeyType.ECDSA;
  }

  /**
   * Verifies an ECDSA signature over the given message.
   *
   * @param message the original message that was signed
   * @param signature the 64-byte ECDSA signature to verify
   * @return {@code true} if the signature is valid for the given message and this public key; {@code false} otherwise
   */
  @Override
  public boolean verify(byte[] message, byte[] signature) {
    if (signature.length != 64) {
      return false;
    }

    byte[] hash = Keccak256Utils.keccak256(message);

    BigInteger r = new BigInteger(1, Arrays.copyOfRange(signature, 0, 32));
    BigInteger s = new BigInteger(1, Arrays.copyOfRange(signature, 32, 64));

    BigInteger n = ECDSAPrivateKey.CURVE.getN();
    if (r.signum() <= 0 || r.compareTo(n) >= 0) return false;
    if (s.signum() <= 0 || s.compareTo(n) >= 0) return false;

    ECPoint q = ECDSAPrivateKey.CURVE
      .getCurve()
      .decodePoint(this.bytes)
      .normalize();

    var signer = new ECDSASigner();
    var pub = new ECPublicKeyParameters(q, ECDSAPrivateKey.CURVE);

    signer.init(false, pub);
    return signer.verifySignature(hash, r, s);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.bytes);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null || this.getClass() !=o.getClass()) return false;
    return Arrays.equals(this.bytes, ((ECDSAPublicKey) o).bytes);
  }
}
