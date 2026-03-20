package io.github.manishdait.sdk.internal.key;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.proto.Key;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.bouncycastle.util.encoders.Hex;
import io.github.manishdait.sdk.key.KeyType;
import io.github.manishdait.sdk.key.PublicKey;
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
   * Constructor.
   * @param bytes the bytes for the Ed25519PublicKey
   */
  private ED25519PublicKey(final byte[] bytes) {
    this.bytes = bytes.clone();
  }

  /**
   * Create a {@code ED25519PublicKey} from given bytes.
   *
   * @param bytes the bytes from which the key is to be derived
   * @return the new instance of {@code ED25519PublicKey}
   */
  public static @NonNull ED25519PublicKey fromBytes(final byte[] bytes) {
    if (bytes.length == Ed25519.PUBLIC_KEY_SIZE) {
      return new ED25519PublicKey(bytes);
    }

    try {
      final SubjectPublicKeyInfo spki = SubjectPublicKeyInfo.getInstance(bytes);

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
   * Create a {@code ED25519PublicKey} from given string.
   *
   * @param str the string from which the key is to be derived
   * @return the new instance of {@code ED25519PublicKey}
   */
  public static @NonNull ED25519PublicKey fromString(@NonNull final String str) {
    Objects.requireNonNull(str, "str must not be null");
    return fromBytes(Hex.decode(str.replaceFirst("^0x", "")));
  }

  @Override
  public Key toProto() {
    return Key.newBuilder()
      .setEd25519(ByteString.copyFrom(this.bytes))
      .build();
  }

  @Override
  public byte[] getBytes() {
    return this.bytes.clone();
  }

  @Override
  public byte[] getDERBytes() {
    try {
      final AlgorithmIdentifier alg = new AlgorithmIdentifier(ED25519_OID);
      final SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo(alg, this.getBytes());

      return spki.getEncoded();
    } catch (Exception e) {
      throw new RuntimeException("Failed to encode Ed25519 public key to DER", e);
    }
  }

  @Override
  public @NonNull String toHexString() {
    return Hex.toHexString(this.getBytes());
  }

  @Override
  public @NonNull String toDERHex() {
    return Hex.toHexString(this.getDERBytes());
  }

  @Override
  public @NonNull KeyType getType() {
    return KeyType.ED25519;
  }

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
