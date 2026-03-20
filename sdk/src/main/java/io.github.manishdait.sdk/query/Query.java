package io.github.manishdait.sdk.query;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.proto.AccountAmount;
import com.hedera.hashgraph.sdk.proto.CryptoTransferTransactionBody;
import com.hedera.hashgraph.sdk.proto.Duration;
import com.hedera.hashgraph.sdk.proto.QueryHeader;
import com.hedera.hashgraph.sdk.proto.Response;
import com.hedera.hashgraph.sdk.proto.ResponseHeader;

import com.hedera.hashgraph.sdk.proto.ResponseType;
import com.hedera.hashgraph.sdk.proto.SignatureMap;
import com.hedera.hashgraph.sdk.proto.SignaturePair;
import com.hedera.hashgraph.sdk.proto.SignedTransaction;
import com.hedera.hashgraph.sdk.proto.Timestamp;
import com.hedera.hashgraph.sdk.proto.Transaction;
import com.hedera.hashgraph.sdk.proto.TransactionBody;
import com.hedera.hashgraph.sdk.proto.TransactionID;
import com.hedera.hashgraph.sdk.proto.TransferList;
import io.grpc.MethodDescriptor;
import io.github.manishdait.sdk.Client;
import io.github.manishdait.sdk.Hbar;
import io.github.manishdait.sdk.HbarUnit;
import io.github.manishdait.sdk.Status;
import io.github.manishdait.sdk.internal.Executable;
import io.github.manishdait.sdk.internal.ExecutionState;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.List;

public abstract class Query extends Executable<com.hedera.hashgraph.sdk.proto.Query, Response> {
  protected QueryHeader queryHeader;
  protected Hbar cost = Hbar.of(0);

  public abstract com.hedera.hashgraph.sdk.proto.Query toProto();
  protected abstract MethodDescriptor<com.hedera.hashgraph.sdk.proto.Query, Response> getMethodDescriptor();

  protected abstract ResponseHeader getResponseHeader(final Response response);

  protected boolean requiredPayment() {
    return true;
  }

  private Transaction preparePayment(@NonNull final Client client, @NonNull final Hbar cost) {
    CryptoTransferTransactionBody cryptoTx = CryptoTransferTransactionBody.newBuilder()
      .setTransfers(
        TransferList.newBuilder()
          .addAccountAmounts(
            AccountAmount.newBuilder()
              .setAccountID(client.getOperatorAccount().accountId().toProto())
              .setAmount(-cost.getValueInTinybar())
          )
          .addAccountAmounts(
            AccountAmount.newBuilder()
              .setAccountID(client.getNode().getAccountId().toProto())
              .setAmount(cost.getValueInTinybar())
          )
          .build()
      ).build();

    TransactionBody txBody = TransactionBody.newBuilder()
      .setTransactionID(
        TransactionID.newBuilder()
          .setAccountID(client.getOperatorAccount().accountId().toProto())
          .setTransactionValidStart(
            Timestamp.newBuilder()
              .setSeconds(Instant.now().getEpochSecond())
              .setNanos(Instant.now().getNano())
          )
      )
      .setNodeAccountID(client.getNode().getAccountId().toProto())
      .setTransactionFee(100_000_000)
      .setTransactionValidDuration(Duration.newBuilder().setSeconds(120))
      .setCryptoTransfer(cryptoTx)
      .build();

    byte[] bodyBytes = txBody.toByteArray();
    byte[] signature = client.getOperatorAccount().privateKey().sign(bodyBytes);

    SignedTransaction signedTx = SignedTransaction.newBuilder()
      .setBodyBytes(ByteString.copyFrom(bodyBytes))
      .setSigMap(
        SignatureMap.newBuilder()
          .addSigPair(
            SignaturePair.newBuilder()
              .setPubKeyPrefix(ByteString.copyFrom(client.getOperatorAccount().privateKey().getPublicKey().getBytes()))
              .setEd25519(ByteString.copyFrom(signature))
          )
      )
      .build();

    return Transaction.newBuilder()
      .setSignedTransactionBytes(signedTx.toByteString())
      .build();
  }

  protected void doPreQueryCheck(@NonNull final Client client) {
    if (requiredPayment()) {
      queryHeader = QueryHeader.newBuilder().setResponseType(ResponseType.COST_ANSWER).build();
      var response = execute(client);

      cost = Hbar.of(getResponseHeader(response).getCost(), HbarUnit.TINYBAR);
    }

    var queryHeaderBuilder = QueryHeader.newBuilder()
      .setResponseType(ResponseType.ANSWER_ONLY);

    if (cost.getValueInTinybar() > 0) {
      queryHeaderBuilder.setPayment(preparePayment(client, cost));
    }

    queryHeader = queryHeaderBuilder.build();
  }

  @Override
  protected ExecutionState getExecutionState(Response queryResponse) {
    final var retryable = List.of(
      Status.UNKNOWN,
      Status.BUSY,
      Status.RECEIPT_NOT_FOUND,
      Status.RECORD_NOT_FOUND,
      Status.PLATFORM_NOT_ACTIVE
    );

    final var status = Status.valueOf(getResponseHeader(queryResponse).getNodeTransactionPrecheckCode());

    if (status == Status.OK) return ExecutionState.FINISH;
    if (retryable.contains(status)) return ExecutionState.RETRY;

    return ExecutionState.FAIL;
  }

  @Override
  protected com.hedera.hashgraph.sdk.proto.Query buildRequest() {
    return this.toProto();
  }
}
