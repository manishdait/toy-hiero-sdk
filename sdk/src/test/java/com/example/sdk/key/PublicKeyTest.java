package com.example.sdk.key;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.proto.Key;
import org.assertj.core.api.Assertions;
import org.bouncycastle.util.encoders.Hex;
import org.example.sdk.key.PrivateKey;
import org.example.sdk.key.PublicKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//public class PublicKeyTest {
//  @Test
//  @DisplayName("Should create public key from hex string")
//  void shouldDerivePublicKeyFromHexString() {
//    final String hex = "09fe6e485c1fb4e24c80b591fc79103c28006d549428a0d3ccb2a88412f2bda8";
//    final PublicKey publicKey = PublicKey.fromString(hex);
//    Assertions.assertThat(publicKey).isNotNull();
//  }
//
//  @Test
//  @DisplayName("Should create public key from bytes")
//  void shouldDerivePublicKeyFromBytes() {
//    final String hex = "09fe6e485c1fb4e24c80b591fc79103c28006d549428a0d3ccb2a88412f2bda8";
//    final byte[] bytes = Hex.decode(hex);
//    final PublicKey publicKey = PublicKey.fromBytes(bytes);
//    Assertions.assertThat(publicKey).isNotNull();
//  }
//
//  @Test
//  @DisplayName("Round trip generate and creating public key")
//  void roundTripGeneratingAndCreatingPublicKey() {
//    final PublicKey publicKey = PrivateKey.generate().getPublicKey();
//    final String hexKey = publicKey.toHexString();
//    final PublicKey newPublicKey = PublicKey.fromString(hexKey);
//
//    Assertions.assertThat(publicKey).isEqualTo(newPublicKey);
//  }
//
//  @Test
//  @DisplayName("Should convert key to protobuf object")
//  void shouldConvertKeyToProto() {
//    final PublicKey publicKey = PrivateKey.generate().getPublicKey();
//    final Key proto = publicKey.toProto();
//
//    Assertions.assertThat(proto).isNotNull();
//    Assertions.assertThat(proto.getEd25519()).isEqualTo(ByteString.copyFrom(publicKey.getBytes()));
//  }
//
//  @Test
//  @DisplayName("Should create a public key from proto")
//  void shouldCreatePublicKeyFromProto() {
//    final PublicKey publicKey = PrivateKey.generate().getPublicKey();
//    final Key proto = publicKey.toProto();
//
//    final PublicKey newPublicKey = PublicKey.fromProto(proto);
//    Assertions.assertThat(newPublicKey).isEqualTo(publicKey);
//  }
//}
