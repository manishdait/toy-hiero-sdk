package org.example.sdk.query;

import com.hedera.hashgraph.sdk.proto.QueryHeader;
import com.hedera.hashgraph.sdk.proto.Transaction;
import com.hedera.hashgraph.sdk.proto.CryptoGetInfoQuery;
import com.hedera.hashgraph.sdk.proto.ResponseType;
import com.hedera.hashgraph.sdk.proto.CryptoTransferTransactionBody;
import com.hedera.hashgraph.sdk.proto.TransferList;
import com.hedera.hashgraph.sdk.proto.AccountAmount;
import com.hedera.hashgraph.sdk.proto.TransactionBody;
import com.hedera.hashgraph.sdk.proto.TransactionID;
import com.hedera.hashgraph.sdk.proto.Timestamp;
import com.hedera.hashgraph.sdk.proto.Duration;
import com.hedera.hashgraph.sdk.proto.SignedTransaction;
import com.hedera.hashgraph.sdk.proto.SignatureMap;
import com.hedera.hashgraph.sdk.proto.SignaturePair;
import com.hedera.hashgraph.sdk.proto.CryptoServiceGrpc;
import com.hedera.hashgraph.sdk.proto.Response;
import com.hedera.hashgraph.sdk.proto.ResponseHeader;

import com.google.protobuf.ByteString;
import io.grpc.MethodDescriptor;
import org.example.sdk.Client;
import org.example.sdk.account.AccountId;
import org.example.sdk.account.AccountInfo;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.Objects;

public class AccountInfoQuery extends Query {
  private AccountId accountId;

  public AccountInfoQuery() {}

  public AccountId getAccountId() {
    return this.accountId;
  }

  public AccountInfoQuery withAccountId(@NonNull final AccountId accountId) {
    Objects.requireNonNull(accountId, "accountId must not be null");
    this.accountId = accountId;
    return this;
  }

  @Override
  public com.hedera.hashgraph.sdk.proto.Query toProto(@NonNull final Client client) {
    Objects.requireNonNull(client, "client must not be null");

    var query = CryptoGetInfoQuery.newBuilder()
      .setAccountID(this.accountId.toProto())
      .setHeader(
        QueryHeader.newBuilder()
          .setResponseType(ResponseType.ANSWER_ONLY)
          .setPayment(getPayment(client))
          .build()
      )
      .build();

    return com.hedera.hashgraph.sdk.proto.Query.newBuilder()
      .setCryptoGetInfo(query)
      .build();
  }

  private Transaction getPayment(@NonNull final Client client) {
    Objects.requireNonNull(client, "client must not be null");

    CryptoTransferTransactionBody cryptoTx = CryptoTransferTransactionBody.newBuilder()
      .setTransfers(
        TransferList.newBuilder()
          .addAccountAmounts(
            AccountAmount.newBuilder()
              .setAccountID(client.getOperatorAccount().accountId().toProto())
              .setAmount(-10)
          )
          .addAccountAmounts(
            AccountAmount.newBuilder()
              .setAccountID(client.getNode().getAccountId().toProto())
              .setAmount(10)
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

  @Override
  protected MethodDescriptor<com.hedera.hashgraph.sdk.proto.Query, Response> getMethodDescriptor() {
    return CryptoServiceGrpc.getGetAccountInfoMethod();
  }

  @Override
  protected ResponseHeader getResponseHeader(@NonNull final Response response) {
    Objects.requireNonNull(response, "response must not be null");
    return response.getCryptoGetInfo().getHeader();
  }

  public AccountInfo query(@NonNull Client client) {
    Objects.requireNonNull(client, "client must not be null");

    var infoProto = this.performQuery(client).getCryptoGetInfo().getAccountInfo();
    return AccountInfo.fromProto(infoProto);
  }
}
