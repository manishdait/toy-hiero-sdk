package io.github.manishdait.sdk.transaction;

import io.github.manishdait.sdk.Client;
import io.github.manishdait.sdk.Status;
import io.github.manishdait.sdk.query.TransactionReceiptQuery;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record TransactionResponse(
    @NonNull Client client,
    @NonNull TransactionId transactionId,
    @NonNull Status nodeTransactionPrecheckCode,
    long cost) {
  public static TransactionResponse fromProto(
      @NonNull final Client client,
      @NonNull final TransactionId transactionId,
      final com.hedera.hashgraph.sdk.proto.TransactionResponse proto) {
    Objects.requireNonNull(transactionId, "transactionId must not be null");
    Objects.requireNonNull(proto, "proto must not be null");

    return new TransactionResponse(
        client,
        transactionId,
        Status.valueOf(proto.getNodeTransactionPrecheckCode()),
        proto.getCost());
  }

  public TransactionReceipt queryReceipt() {
    return new TransactionReceiptQuery().withTransactionId(this.transactionId).query(client);
  }
}
