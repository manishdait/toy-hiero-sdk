package org.example.sdk.key;

import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.bouncycastle.util.encoders.Hex;
import org.jspecify.annotations.NonNull;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represent an ED25519 Private Key.
 */
public class PrivateKey extends Key {
  private final byte[] bytes;

  /**
   * Create a new instance of (ED25519) {@link PrivateKey}.
   *
   * @param bytes the rawBytes of the ED25519 private key
   */
  private PrivateKey(byte[] bytes) {
    this.bytes = bytes;
  }

  public byte[] getBytes() {
    return this.bytes;
  }

  public String toHexString() {
    return Hex.toHexString(this.bytes);
  }

  /**
   * Generates a new (ED25519) {@link PrivateKey}.
   *
   * @return an instance of ED25519 {@link PrivateKey}
   */
  public static PrivateKey generate() {
    byte[] bytes = new byte[Ed25519.SECRET_KEY_SIZE];
    Ed25519.generatePrivateKey(new SecureRandom(), bytes);

    return new PrivateKey(bytes);
  }

  /**
   * Create a ED25519 {@link PrivateKey} from {@link String}.
   *
   * @param privateKey a {@link String} representation of ED25519 private key
   * @return an instance of ED25519 {@link PrivateKey}
   */
  public static PrivateKey fromString(@NonNull final String privateKey) {
    Objects.requireNonNull(privateKey, "privateKey must not be null.");
    return fromBytes(Hex.decode(privateKey));
  }

  /**
   * Create an ED25519 {@link PrivateKey} from bytes.
   *
   * @param bytes {@link byte[]} from which the ED25519 key must be derived.
   * @return an instance of ED25519 {@link PrivateKey}
   */
  public static PrivateKey fromBytes(final byte[] bytes) {
    if (bytes.length == Ed25519.SECRET_KEY_SIZE) {
      return new PrivateKey(bytes);
    }

    PrivateKeyInfo pki = PrivateKeyInfo.getInstance(bytes);
    try {
      return new PrivateKey(((DEROctetString) pki.parsePrivateKey()).getOctets());
    } catch (Exception e) {
      throw new RuntimeException("Error parsing key from bytes.");
    }
  }

  /**
   * Get the ED25519 {@link PublicKey} for the {@link PrivateKey}.
   *
   * @return the {@link PublicKey} for the respected {@link PrivateKey}
   */
  public PublicKey getPublicKey() {
    byte[] bytes = new byte[Ed25519.SECRET_KEY_SIZE];
    Ed25519.generatePublicKey(this.getBytes(), 0, bytes, 0);
    return PublicKey.fromBytes(bytes);
  }

  /**
   * Sign the message with the respected {@link PrivateKey}.
   *
   * @param message message to be signed
   * @return the signed message
   */
  public byte[] sign(byte[] message) {
    byte[] signature = new byte[Ed25519.SIGNATURE_SIZE];
    Ed25519.sign(this.getBytes(), 0, message, 0, message.length, signature, 0);
    return  signature;
  }

  @Override
  public com.hedera.hashgraph.sdk.proto.Key toProto() {
    return this.getPublicKey().toProto();
  }

  @Override
  public String toString() {
    return "PrivateKey(ED25519)[hex=%s]".formatted(this.toHexString());
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null || this.getClass() !=o.getClass()) return false;

    return Arrays.equals(this.bytes, ((PrivateKey) o).bytes);
  }
}
