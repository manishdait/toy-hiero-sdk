package org.example.sdk.transaction;

import com.hedera.hashgraph.sdk.proto.*;
import io.grpc.CallOptions;
import io.grpc.stub.ClientCalls;
import org.example.sdk.Client;
import org.example.sdk.Status;
import org.example.sdk.query.TransactionReceiptQuery;
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
    @NonNull final Client client,
    @NonNull final TransactionId transactionId,
    final com.hedera.hashgraph.sdk.proto.TransactionResponse proto
  ) {
    Objects.requireNonNull(transactionId, "transactionId must not be null");
    Objects.requireNonNull(proto, "proto must not be null");

    return new TransactionResponse(
      client,
      transactionId,
      Status.valueOf(proto.getNodeTransactionPrecheckCode()),
      proto.getCost()
    );
  }

  public TransactionReceipt queryReceipt() {
    return new TransactionReceiptQuery()
      .withTransactionId(this.transactionId)
      .query(client);
  }
}
