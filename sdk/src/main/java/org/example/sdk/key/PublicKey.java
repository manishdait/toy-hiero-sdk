package org.example.sdk.key;

import com.google.protobuf.ByteString;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.bouncycastle.util.encoders.Hex;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represent an ED25519 Public Key.
 */
public class PublicKey extends Key {
  private final byte[] bytes;

  /**
   * Create a new instance of (ED25519) {@link PublicKey}.
   *
   * @param bytes the rawBytes of the ED25519 public key
   */
  private PublicKey(byte[] bytes) {
    this.bytes = bytes;
  }

  public byte[] getBytes() {
    return this.bytes;
  }

  public String toHexString() {
    return Hex.toHexString(this.bytes);
  }

  /**
   * Create a ED25519 {@link PublicKey} from {@link String}.
   *
   * @param key a {@link String} representation of ED25519 public key
   * @return an instance of ED25519 {@link PublicKey}
   */
  public static PublicKey fromString(@NonNull String key) {
    Objects.requireNonNull(key, "key must not be null");
    byte[] bytes = Hex.decode(key);
    return fromBytes(bytes);
  }

  /**
   * Create an ED25519 {@link PublicKey} from bytes.
   *
   * @param bytes from which the ED25519 key must be derived.
   * @return an instance of ED25519 {@link PublicKey}
   */
  public static PublicKey fromBytes(byte[] bytes) {
    if (bytes.length == Ed25519.SECRET_KEY_SIZE) {
      return new PublicKey(bytes);
    }

    SubjectPublicKeyInfo ski = SubjectPublicKeyInfo.getInstance(bytes);
    return new PublicKey(ski.getPublicKeyData().getBytes());
  }

  /**
   * Create an ED25519 {@link PublicKey} from proto {@link com.hedera.hashgraph.sdk.proto.Key} object.
   *
   * @param proto a {@link com.hedera.hashgraph.sdk.proto.Key} object.
   * @return an instance of ED25519 {@link PublicKey}
   */
  public static PublicKey fromProto(com.hedera.hashgraph.sdk.proto.Key proto) {
    if (!proto.hasEd25519()) {
      throw new RuntimeException("Only Ed25519 supported for now.");
    }

    ByteString byteString = proto.getEd25519();
    return new PublicKey(byteString.toByteArray());
  }

  /**
   * Verifies that a given message was signed with this key.
   *
   * @param message the original message that was signed
   * @param signature the signature to verify
   * @return {@code true} if the signature is valid for the given message and this key; {@code false} otherwise
   */
  public boolean verify(byte[] message, byte[] signature) {
    return Ed25519.verify(signature, 0, this.getBytes(), 0, message, 0, message.length);
  }

  @Override
  public com.hedera.hashgraph.sdk.proto.Key toProto() {
    return com.hedera.hashgraph.sdk.proto.Key.newBuilder()
      .setEd25519(ByteString.copyFrom(this.getBytes()))
      .build();
  }

  @Override
  public String toString() {
    return  "PublicKey(ED25519)[hex=%s]".formatted(this.toHexString());
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null || this.getClass() != o.getClass()) return false;

    return Arrays.equals(this.bytes, ((PublicKey) o).bytes);
  }
}
