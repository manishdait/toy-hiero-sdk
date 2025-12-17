package org.example.sdk.query;

import com.hedera.hashgraph.sdk.proto.*;
import io.grpc.CallOptions;
import io.grpc.stub.ClientCalls;
import org.example.sdk.Client;
import org.example.sdk.Status;
import org.example.sdk.transaction.TransactionId;
import org.example.sdk.transaction.TransactionReceipt;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

public class TransactionReceiptQuery {
  private static final int MAX_ATTEMPTS = 10;

  private TransactionId transactionId;
  private boolean includeDuplicates;
  private boolean includeChildren;

  public TransactionReceiptQuery() {}

  public TransactionReceiptQuery(
    @NonNull final TransactionId transactionId,
    final boolean includeDuplicates,
    final boolean includeChildren
  ) {
    Objects.requireNonNull(transactionId, "transactionId must not be null");

    this.transactionId = transactionId;
    this.includeDuplicates = includeDuplicates;
    this.includeChildren = includeChildren;
  }

  public TransactionId getTransactionId() {
    return transactionId;
  }

  public boolean isIncludeDuplicates() {
    return includeDuplicates;
  }

  public boolean isIncludeChildren() {
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

  public TransactionReceipt query(final @NonNull Client client) {
    Objects.requireNonNull(client, "client must not be null");

    final List<Status> RETRYABLE_RESPONSE = List.of(
      Status.UNKNOWN,
      Status.BUSY,
      Status.RECEIPT_NOT_FOUND,
      Status.RECORD_NOT_FOUND,
      Status.PLATFORM_NOT_ACTIVE
    );

    var receiptQuery = TransactionGetReceiptQuery.newBuilder()
      .setTransactionID(this.transactionId.toProto())
      .setHeader(QueryHeader.newBuilder().setResponseType(ResponseType.ANSWER_ONLY).build())
      .build();

    var query = Query.newBuilder()
      .setTransactionGetReceipt(receiptQuery)
      .build();

    var method = CryptoServiceGrpc.getGetTransactionReceiptsMethod();

    for (int i = 0; i < MAX_ATTEMPTS; i++) {
      var call = client.getNode().getChannel().newCall(method, CallOptions.DEFAULT);
      var receipt = ClientCalls.blockingUnaryCall(call, query);
      if (!receipt.hasTransactionGetReceipt()) {
        break;
      }

      var receiptProto = receipt.getTransactionGetReceipt().getReceipt();

      if (Status.valueOf(receiptProto.getStatus()) == Status.SUCCESS) {
        return TransactionReceipt.fromProto(receiptProto);
      }

      if (!RETRYABLE_RESPONSE.contains(Status.valueOf(receiptProto.getStatus()))) {
        throw new RuntimeException("Fail to query receipt status " + Status.valueOf(receiptProto.getStatus()).toString());
      }
    }

    throw new RuntimeException("Unable to fetch transactionReceipt");
  }
}
