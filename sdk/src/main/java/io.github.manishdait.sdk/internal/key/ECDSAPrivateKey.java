package io.github.manishdait.sdk.internal.key;

import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.sec.ECPrivateKey;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.sec.SECObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.util.encoders.Hex;
import io.github.manishdait.sdk.key.KeyType;
import io.github.manishdait.sdk.key.PrivateKey;
import io.github.manishdait.sdk.key.PublicKey;
import org.jspecify.annotations.NonNull;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents an ECDSA private key using the secp256k1 curve.
 */
public final class ECDSAPrivateKey implements PrivateKey {
  private static final int SCALAR_SIZE = 32;
  private static final byte[] LEGACY_PREFIX_BYTES = Hex.decode("3030020100300706052b8104000a04220420");

  static final ECDomainParameters CURVE;

  private final byte[] scalar;

  static {
    // Load the curve parameters
    var params = SECNamedCurves.getByName("secp256k1");
    CURVE = new ECDomainParameters(
      params.getCurve(),
      params.getG(),
      params.getN(),
      params.getH()
    );
  }

  private ECDSAPrivateKey(final byte[] scalar) {
    this.scalar = scalar.clone();
  }

  public static @NonNull ECDSAPrivateKey generate() {
    final var random = new SecureRandom();
    final var n = CURVE.getN();

    BigInteger d;
    do {
      byte[] buff = new byte[SCALAR_SIZE];
      random.nextBytes(buff);
      d = new BigInteger(1, buff);
    } while (d.signum() == 0 || d.compareTo(n) >= 0);

    return new ECDSAPrivateKey(toFixed(d));
  }

  public static @NonNull ECDSAPrivateKey fromBytes(final byte[] bytes) {
    if (bytes.length == SCALAR_SIZE) {
      return fromScalar(bytes);
    }

    if (hasPrefix(bytes)) {
      var scalar = Arrays.copyOfRange(
        bytes,
        LEGACY_PREFIX_BYTES.length,
        bytes.length
      );
      return fromScalar(scalar);
    }

    try {
      var pki = PrivateKeyInfo.getInstance(bytes);
      // ANS1Encodable
      var key = pki.parsePrivateKey();

      // PKCS#8 wrapping SEC1
      if (key instanceof ASN1OctetString octets) {
        return fromScalar(octets.getOctets());
      }

      // PKCS#8 → SEC1
      var ec = ECPrivateKey.getInstance(key);
      return fromScalar(toFixed(ec.getKey()));
    } catch (Exception e) {
      // fallback
    }

    try {
      var ec = ECPrivateKey.getInstance(bytes);
      return fromScalar(toFixed(ec.getKey()));
    } catch (Exception e) {
      // fallback
    }

    throw new RuntimeException("Invalid ECDSA private key encoding");
  }

  public static @NonNull ECDSAPrivateKey fromString(final @NonNull String str) {
    Objects.requireNonNull(str, "str must not be null");
    return fromBytes(Hex.decode(str.replaceFirst("^0x", "")));
  }

  @Override
  public @NonNull PublicKey getPublicKey() {
    final var d = new BigInteger(1, scalar);
    // Ec Point
    final var q = CURVE.getG().multiply(d).normalize();
    return ECDSAPublicKey.fromBytes(q.getEncoded(true));
  }

  @Override
  public @NonNull KeyType getType() {
    return KeyType.ECDSA;
  }

  @Override
  public byte[] getBytes() {
    return this.scalar.clone();
  }

  @Override
  public byte[] getDERBytes() {
    try {
      var ecPrivateKey = new ECPrivateKey(
        CURVE.getN().bitLength(),
        new BigInteger(1, scalar),
        null,
        null
      );

      var pki = new PrivateKeyInfo(
        new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, SECObjectIdentifiers.secp256k1),
        ecPrivateKey
      );

      return pki.getEncoded();
    } catch (Exception e) {
      throw new RuntimeException("Failed to encode ECDSA private key to DER", e);
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
  public byte[] sign(byte[] message) {
    byte[] hash = Keccak256Utils.keccak256(message);

    var signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
    var priv = new ECPrivateKeyParameters(new BigInteger(1, scalar), CURVE);

    signer.init(true, priv);
    BigInteger[] sig = signer.generateSignature(hash);

    byte[] r = toFixed(sig[0]);
    byte[] s = toFixed(sig[1]);

    return concat(r, s);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.scalar);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null || this.getClass() !=o.getClass()) return false;
    return Arrays.equals(this.scalar, ((ECDSAPrivateKey) o).scalar);
  }

  private static boolean hasPrefix(final byte[] data) {
    if (data.length < LEGACY_PREFIX_BYTES.length) {
      return false;
    }

    for (int i = 0; i < LEGACY_PREFIX_BYTES.length; i++) {
      if (data[i] != LEGACY_PREFIX_BYTES[i]) {
        return false;
      }
    }

    return true;
  }

  private static byte[] toFixed(BigInteger v) {
    byte[] raw = v.toByteArray();
    byte[] out = new byte[32];
    System.arraycopy(
      raw,
      Math.max(0, raw.length - 32),
      out,
      Math.max(0, 32 - raw.length),
      Math.min(32, raw.length)
    );
    return out;
  }

  private static byte[] concat(byte[] a, byte[] b) {
    byte[] out = new byte[64];
    System.arraycopy(a, 0, out, 0, 32);
    System.arraycopy(b, 0, out, 32, 32);
    return out;
  }

  private static ECDSAPrivateKey fromScalar(byte[] scalar) {
    if (scalar.length != SCALAR_SIZE) {
      throw new RuntimeException("Invalid ECDSA private key length");
    }

    BigInteger d = new BigInteger(1, scalar);
    BigInteger n = CURVE.getN();

    if (d.signum() == 0 || d.compareTo(n) >= 0) {
      throw new RuntimeException("ECDSA private key scalar out of range");
    }

    return new ECDSAPrivateKey(toFixed(d));
  }
}
