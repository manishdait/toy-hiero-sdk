package org.example.sdk.transaction;

import com.hedera.hashgraph.sdk.proto.*;
import io.grpc.CallOptions;
import io.grpc.stub.ClientCalls;
import org.example.sdk.Client;
import org.example.sdk.Status;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

public record TransactionResponse (
  @NonNull Client client,
  @NonNull TransactionId transactionId,
  @NonNull Status nodeTransactionPrecheckCode,
  long cost
) {
  public static TransactionResponse fromProto(
    @NonNull Client client,
    @NonNull TransactionId transactionId,
    com.hedera.hashgraph.sdk.proto.TransactionResponse proto
  ) {
    Objects.requireNonNull(transactionId, "transactionId must not be null");
    Objects.requireNonNull(proto, "proto must not be null");
    return new TransactionResponse(client, transactionId, Status.valueOf(proto.getNodeTransactionPrecheckCode()), proto.getCost());
  }

  public TransactionReceipt queryReceipt() {
    final List<Status> RETRYABLE_RESPONSE = List.of(
      Status.UNKNOWN,
      Status.BUSY,
      Status.RECEIPT_NOT_FOUND,
      Status.RECORD_NOT_FOUND,
      Status.PLATFORM_NOT_ACTIVE
    );

    int maxAttempts = 10;

    var receiptQuery = TransactionGetReceiptQuery.newBuilder()
      .setTransactionID(this.transactionId.toProto())
      .setHeader(QueryHeader.newBuilder().setResponseType(ResponseType.ANSWER_ONLY).build())
      .build();

    var query = Query.newBuilder()
      .setTransactionGetReceipt(receiptQuery)
      .build();

    var method = CryptoServiceGrpc.getGetTransactionReceiptsMethod();

    for (int i = 0; i < maxAttempts; i++) {
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
