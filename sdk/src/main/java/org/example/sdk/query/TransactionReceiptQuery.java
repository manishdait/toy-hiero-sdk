package org.example.sdk.query;

import com.hedera.hashgraph.sdk.proto.*;
import io.grpc.CallOptions;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import org.example.sdk.Client;
import org.example.sdk.Status;
import org.example.sdk.account.AccountInfo;
import org.example.sdk.transaction.TransactionId;
import org.example.sdk.transaction.TransactionReceipt;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

public class TransactionReceiptQuery extends Query {
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

  @Override
  public com.hedera.hashgraph.sdk.proto.Query toProto(@NonNull final Client client) {
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
  protected ResponseHeader getResponseHeader(Response response) {
    return response.getTransactionGetReceipt().getHeader();
  }

  @Override
  protected MethodDescriptor<com.hedera.hashgraph.sdk.proto.Query, Response> getMethodDescriptor() {
    return CryptoServiceGrpc.getGetTransactionReceiptsMethod();
  }



  public TransactionReceipt query(final @NonNull Client client) {
    Objects.requireNonNull(client, "client must not be null");
    var receipt = this.performQuery(client).getTransactionGetReceipt().getReceipt();

    for (int i = 0; i < MAX_ATTEMPTS; i++) {
      if (shouldRetry(receipt.getStatus())) {
        receipt = this.performQuery(client).getTransactionGetReceipt().getReceipt();
        continue;
      }

      if (Status.valueOf(receipt.getStatus()) == Status.SUCCESS) {
        return TransactionReceipt.fromProto(receipt);
      }
    }

    return TransactionReceipt.fromProto(receipt);
  }
}
