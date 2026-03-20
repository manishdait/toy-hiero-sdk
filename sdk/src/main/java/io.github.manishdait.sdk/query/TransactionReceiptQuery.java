package io.github.manishdait.sdk.query;

import com.hedera.hashgraph.sdk.proto.QueryHeader;
import com.hedera.hashgraph.sdk.proto.Response;
import com.hedera.hashgraph.sdk.proto.ResponseHeader;
import com.hedera.hashgraph.sdk.proto.ResponseType;
import com.hedera.hashgraph.sdk.proto.TransactionGetReceiptQuery;
import com.hedera.hashgraph.sdk.proto.CryptoServiceGrpc;

import io.grpc.MethodDescriptor;
import io.github.manishdait.sdk.Client;
import io.github.manishdait.sdk.Status;
import io.github.manishdait.sdk.transaction.TransactionId;
import io.github.manishdait.sdk.transaction.TransactionReceipt;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

public class TransactionReceiptQuery extends Query {
  private TransactionId transactionId;
  private boolean includeDuplicates;
  private boolean includeChildren;

  public TransactionReceiptQuery() {}

  public TransactionId getTransactionId() {
    return transactionId;
  }

  public boolean hasIncludeDuplicates() {
    return includeDuplicates;
  }

  public boolean hasIncludeChildren() {
    return includeChildren;
  }

  public TransactionReceiptQuery withTransactionId(@NonNull final TransactionId transactionId) {
    Objects.requireNonNull(transactionId, "transactionId must not be null");
    this.transactionId = transactionId;
    return this;
  }

  public TransactionReceiptQuery withIncludeDuplicates(final boolean includeDuplicates) {
    this.includeDuplicates = includeDuplicates;
    return this;
  }

  public TransactionReceiptQuery withIncludeChildren(final boolean includeChildren) {
    this.includeChildren = includeChildren;
    return this;
  }

  @Override
  protected boolean requiredPayment() {
    return false;
  }

  @Override
  public com.hedera.hashgraph.sdk.proto.Query toProto() {
    var receiptQuery = TransactionGetReceiptQuery.newBuilder()
      .setTransactionID(this.transactionId.toProto())
      .setIncludeChildReceipts(this.includeChildren)
      .setIncludeDuplicates(this.includeDuplicates)
      .setHeader(QueryHeader.newBuilder().setResponseType(ResponseType.ANSWER_ONLY).build())
      .build();

    return com.hedera.hashgraph.sdk.proto.Query.newBuilder()
      .setTransactionGetReceipt(receiptQuery)
      .build();
  }

  @Override
  protected ResponseHeader getResponseHeader(@NonNull final Response response) {
    Objects.requireNonNull(response, "response must not be null");
    return response.getTransactionGetReceipt().getHeader();
  }

  @Override
  protected MethodDescriptor<com.hedera.hashgraph.sdk.proto.Query, Response> getMethodDescriptor() {
    return CryptoServiceGrpc.getGetTransactionReceiptsMethod();
  }

  public TransactionReceipt query(@NonNull final Client client) {
    Objects.requireNonNull(client, "client must not be null");
    this.doPreQueryCheck(client);

    var receipt = this.execute(client).getTransactionGetReceipt().getReceipt();

    final var retryable = List.of(
      Status.UNKNOWN,
      Status.BUSY,
      Status.RECEIPT_NOT_FOUND,
      Status.RECORD_NOT_FOUND,
      Status.PLATFORM_NOT_ACTIVE
    );

    for (int i = 0; i < 10; i++) {
      if (retryable.contains(Status.valueOf(receipt.getStatus()))) {
        receipt = this.execute(client).getTransactionGetReceipt().getReceipt();
        continue;
      }

      if (Status.valueOf(receipt.getStatus()) == Status.SUCCESS) {
        return TransactionReceipt.fromProto(receipt);
      }
    }

    return TransactionReceipt.fromProto(receipt);
  }
}
