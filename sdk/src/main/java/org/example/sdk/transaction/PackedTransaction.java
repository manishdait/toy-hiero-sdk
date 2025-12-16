package org.example.sdk.transaction;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.proto.SignatureMap;
import com.hedera.hashgraph.sdk.proto.SignaturePair;
import com.hedera.hashgraph.sdk.proto.TransactionBody;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import io.grpc.CallOptions;
import io.grpc.ClientCall;
import io.grpc.ManagedChannel;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import org.example.sdk.Client;
import org.example.sdk.key.PrivateKey;
import org.example.sdk.key.PublicKey;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class PackedTransaction<T extends Transaction<T>> {
  private final TransactionBody transactionBody;
  private final Function<TransactionBody, T> unpacker;
  private final Supplier<MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction, TransactionResponse>> method;
  private final Client client;

  private Map<PublicKey, byte[]> signatures = new HashMap<>();

  protected PackedTransaction(
    @NonNull Client client,
    @NonNull final TransactionBody transactionBody,
    @NonNull final Function<TransactionBody,T> unpacker,
    @NonNull final Supplier<MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction, TransactionResponse>> method
  ) {
    this.client = client;
    this.transactionBody = transactionBody;
    this.unpacker = unpacker;
    this.method = method;
  }

  public T unpack() {
    return this.unpacker.apply(this.transactionBody);
  }

  public PackedTransaction<T> signWith(PrivateKey key) {
    if (signatures.containsKey(key.getPublicKey())) {
      return this;
    }

    byte[] signature = key.sign(transactionBody.toByteArray());
    signatures.put(key.getPublicKey(), signature);
    return this;
  }

  public org.example.sdk.transaction.TransactionResponse send() {
    signWith(client.getOperatorPrivateKey());

    final SignatureMap.Builder signatureMapBuilder = SignatureMap.newBuilder();
    for (PublicKey key : signatures.keySet()) {
      signatureMapBuilder.addSigPair(
        SignaturePair.newBuilder()
          .setPubKeyPrefix(ByteString.copyFrom(key.getBytes()))
          .setEd25519(ByteString.copyFrom(signatures.get(key)))
          .build()
      );
    }


    com.hedera.hashgraph.sdk.proto.Transaction transactionProto = com.hedera.hashgraph.sdk.proto.Transaction.newBuilder()
      .setBodyBytes(transactionBody.toByteString())
      .setSigMap(signatureMapBuilder.build())
      .build();

    ManagedChannel channel = client.getNode().getChannel();
    ClientCall<com.hedera.hashgraph.sdk.proto.Transaction, TransactionResponse> clientCall = channel.newCall(method.get(), CallOptions.DEFAULT);
    var res = ClientCalls.blockingUnaryCall(clientCall, transactionProto);
    return org.example.sdk.transaction.TransactionResponse.fromProto(TransactionId.fromProto(transactionBody.getTransactionID()), res);
  }
}
