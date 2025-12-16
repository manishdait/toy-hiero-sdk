package org.example.sdk.transaction;

import com.hedera.hashgraph.sdk.proto.TransactionBody;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import io.grpc.MethodDescriptor;
import org.example.sdk.Client;
import org.example.sdk.Duration;
import org.example.sdk.Hbar;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * Abstract Transaction Class
 */
public abstract class Transaction <T extends Transaction<T>> {
  private static final Hbar MAX_TRANSACTION_FEE = Hbar.of(1);

  protected TransactionId transactionId;
  protected Duration validDuration;
  protected String memo;

  protected TransactionBody.Builder transactionBodyBuilder;

  protected Transaction() {
    this.validDuration = Duration.of(120);
  }

  public Transaction<T> withMemo(String memo) {
    this.memo = memo;
    return this;
  }

  public Transaction<T> withValidDuration(Duration validDuration) {
    this.validDuration = validDuration;
    return this;
  }

  public Transaction<T> withValidDuration(long validDuration) {
    return this.withValidDuration(Duration.of(validDuration));
  }

  public abstract T fromProto(final TransactionBody proto);
  protected abstract void buildTransaction(final Client client);
  protected abstract MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction, TransactionResponse> getMethodDescriptor();

  public void buildBaseTransaction(@NonNull final Client client) {
    Objects.requireNonNull(client, "client must not be null");

    this.transactionId = TransactionId.fromAccountId(client.getOperatorAccountId());

    this.transactionBodyBuilder = TransactionBody.newBuilder()
      .setTransactionID(this.transactionId.toProto())
      .setTransactionValidDuration(this.validDuration.toProto())
      .setMemo(this.memo == null ? "" : memo)
      .setNodeAccountID(client.getNode().getAccountId().toProto())
      .setTransactionFee(MAX_TRANSACTION_FEE.tinybars());
  }

  public PackedTransaction<T> pack(@NonNull final Client client) {
    buildBaseTransaction(client);
    buildTransaction(client);

    return new PackedTransaction<T>(
      client,
      transactionBodyBuilder.build(),
      this::fromProto,
      this::getMethodDescriptor
    );
  }
}
