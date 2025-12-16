package org.example.sdk.transaction;

import com.hedera.hashgraph.sdk.proto.Timestamp;
import com.hedera.hashgraph.sdk.proto.TransactionID;
import org.example.sdk.account.AccountId;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.Objects;

/**
 * Represent a TransactionID class.
 */
public class TransactionId {
  private final Instant transactionValidStart;
  private final AccountId accountId;
  private boolean scheduled;
  private int nonce;

  /**
   * Returns a new instance of {@link TransactionId}.
   *
   * @param transactionValidStart  the timestamp at which a transaction begins to be valid
   * @param accountId {@link AccountId} paying for the transaction fee
   * @param scheduled tells weather transaction type is scheduled or not
   * @param nonce identifier for an internal transaction that was spawned as part of handling a user transaction
   */
  private TransactionId(
    @NonNull final Instant transactionValidStart,
    @NonNull final AccountId accountId,
    final boolean scheduled,
    final int nonce
  ) {
    Objects.requireNonNull(transactionValidStart, "transactionValidStart must not be null.");
    Objects.requireNonNull(accountId, "accountId must not be null.");

    this.transactionValidStart = transactionValidStart;
    this.accountId = accountId;
    this.scheduled = scheduled;
    this.nonce = nonce;
  }

  /**
   * Returns a new instance of {@link TransactionId}.
   *
   * @param transactionValidStart  the timestamp at which a transaction begins to be valid
   * @param accountId {@link AccountId} paying for the transaction fee
   */
  private TransactionId(
    @NonNull final Instant transactionValidStart,
    @NonNull final AccountId accountId
  ) {
    this(transactionValidStart, accountId, false, 0);
  }

  public Instant getTransactionValidStart() {
    return transactionValidStart;
  }

  public AccountId getAccountId() {
    return accountId;
  }

  public boolean isScheduled() {
    return this.scheduled;
  }

  public int getNonce() {
    return nonce;
  }

  /**
   * Creates a {@link TransactionId} for a given {@link AccountId}.
   *
   * @param accountId {@link AccountId} for which the transactionId is to be generated
   * @return an instance of {@link TransactionId}
   */
  public static TransactionId fromAccountId(@NonNull final AccountId accountId) {
    Objects.requireNonNull(accountId, "accountId must not be null.");
    return new TransactionId(Instant.now(), accountId);
  }

  /**
   * Creates a {@link TransactionId} form protobuf {@link TransactionID} object.
   *
   * @param proto protobuf object of {@link TransactionID}
   * @return an instance of {@link TransactionId}
   */
  public static TransactionId fromProto(@NonNull final TransactionID proto) {
    Objects.requireNonNull(proto, "proto must not be null.");

    return new TransactionId(
      Instant.ofEpochSecond(
        proto.getTransactionValidStart().getSeconds(),
        proto.getTransactionValidStart().getNanos()
      ),
      AccountId.fromProto(proto.getAccountID()),
      proto.getScheduled(),
      proto.getNonce()
    );
  }

  /**
   * Converts the {@link TransactionId} to its protobuf object {@link TransactionID}.
   *
   * @return protobuf object {@link TransactionID}
   */
  public TransactionID toProto() {
    return TransactionID.newBuilder()
      .setTransactionValidStart(
        Timestamp.newBuilder()
          .setSeconds(this.transactionValidStart.getEpochSecond())
          .setNanos(this.transactionValidStart.getNano())
          .build()
      )
      .setAccountID(this.accountId.toProto())
      .setScheduled(this.isScheduled())
      .setNonce(this.nonce)
      .build();
  }
}
