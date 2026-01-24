package org.example.sdk.transaction;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.proto.SignatureMap;
import com.hedera.hashgraph.sdk.proto.SignaturePair;
import com.hedera.hashgraph.sdk.proto.TransactionBody;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import io.grpc.CallOptions;
import io.grpc.ManagedChannel;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import org.example.sdk.Client;
import org.example.sdk.Status;
import org.example.sdk.internal.Executor;
import org.example.sdk.internal.utils.ExecutionState;
import org.example.sdk.key.KeyType;
import org.example.sdk.key.PrivateKey;
import org.example.sdk.key.PublicKey;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class PackedTransaction<T extends Transaction<T>> extends
  Executor<com.hedera.hashgraph.sdk.proto.Transaction, TransactionResponse> {
  private final TransactionBody transactionBody;
  private final Function<TransactionBody, T> unpacker;
  private final Supplier<MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction, TransactionResponse>> method;
  private final Client client;

  private final Map<PublicKey, byte[]> signatures = new HashMap<>();

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

  private SignatureMap buildSignatureMap() {
    final SignatureMap.Builder signatureMapBuilder = SignatureMap.newBuilder();

    for (PublicKey key : signatures.keySet()) {
      if (key.getType() == KeyType.ED25519) {
        signatureMapBuilder.addSigPair(
          SignaturePair.newBuilder()
            .setPubKeyPrefix(ByteString.copyFrom(key.getBytes()))
            .setEd25519(ByteString.copyFrom(signatures.get(key)))
            .build()
        );
      } else if (key.getType() == KeyType.ECDSA) {
        signatureMapBuilder.addSigPair(
          SignaturePair.newBuilder()
            .setPubKeyPrefix(ByteString.copyFrom(key.getBytes()))
            .setECDSASecp256K1(ByteString.copyFrom(signatures.get(key)))
            .build()
        );
      }
    }

    return signatureMapBuilder.build();
  }

  @Override
  protected com.hedera.hashgraph.sdk.proto.Transaction buildRequest() {
    signWith(client.getOperatorPrivateKey());

    return com.hedera.hashgraph.sdk.proto.Transaction.newBuilder()
      .setBodyBytes(this.transactionBody.toByteString())
      .setSigMap(this.buildSignatureMap())
      .build();
  }

  @Override
  protected ExecutionState getExecutionState(TransactionResponse transactionResponse) {
    final var retryable = List.of(
      Status.PLATFORM_TRANSACTION_NOT_CREATED,
      Status.PLATFORM_NOT_ACTIVE,
      Status.BUSY
    );

    final var status = Status.valueOf(transactionResponse.getNodeTransactionPrecheckCode());

    if (status == Status.OK) return ExecutionState.FINISH;
    if (retryable.contains(status)) return ExecutionState.RETRY;
    if (status == Status.TRANSACTION_EXPIRED) return ExecutionState.EXPIRED;

    return ExecutionState.FAIL;
  }

  @Override
  protected MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction, TransactionResponse> getMethod() {
    return this.method.get();
  }

  public org.example.sdk.transaction.TransactionResponse send() {
    final var protoResponse = execute(this.client);

    return org.example.sdk.transaction.TransactionResponse.fromProto(
      client,
      TransactionId.fromProto(transactionBody.getTransactionID()),
      protoResponse
    );
  }
}
