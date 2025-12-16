package org.example.sdk.transaction;

import org.example.sdk.Status;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public record TransactionResponse (@NonNull TransactionId transactionId, @NonNull Status nodeTransactionPrecheckCode, long cost) {
  public static TransactionResponse fromProto(@NonNull TransactionId transactionId, com.hedera.hashgraph.sdk.proto.TransactionResponse proto) {
    Objects.requireNonNull(transactionId, "transactionId must not be null");
    Objects.requireNonNull(proto, "proto must not be null");
    return new TransactionResponse(transactionId, Status.valueOf(proto.getNodeTransactionPrecheckCode()), proto.getCost());
  }
}
