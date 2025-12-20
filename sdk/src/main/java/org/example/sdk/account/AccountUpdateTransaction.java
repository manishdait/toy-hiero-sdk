package org.example.sdk.account;

import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import com.hedera.hashgraph.sdk.proto.CryptoServiceGrpc;
import com.hedera.hashgraph.sdk.proto.CryptoUpdateTransactionBody;
import com.hedera.hashgraph.sdk.proto.TransactionBody;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import io.grpc.MethodDescriptor;
import org.example.sdk.Client;
import org.example.sdk.Duration;
import org.example.sdk.key.Key;
import org.example.sdk.key.PublicKey;
import org.example.sdk.transaction.Transaction;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class AccountUpdateTransaction extends Transaction<AccountUpdateTransaction> {
  private AccountId accountId;
  private Key key;
  private String accountMemo;
  private Duration autoRenewPeriod;
  private int maxAutomaticTokenAssociations;

  public AccountUpdateTransaction() {
    maxAutomaticTokenAssociations = Integer.MIN_VALUE;
  }

  public AccountUpdateTransaction withAccountId(@NonNull final AccountId accountId) {
    Objects.requireNonNull(accountId, "accountId must not be null");
    this.accountId = accountId;
    return this;
  }

  public AccountUpdateTransaction withKey(@NonNull final Key key) {
    Objects.requireNonNull(key, "key must not be null");
    this.key = key;
    return this;
  }

  public AccountUpdateTransaction withAccountMemo(@NonNull final String accountMemo) {
    Objects.requireNonNull(accountMemo, "accountMemo must not be null");
    this.accountMemo = accountMemo;
    return this;
  }

  public AccountUpdateTransaction withAutoRenewPeriod(@NonNull final Duration autoRenewPeriod) {
    Objects.requireNonNull(autoRenewPeriod, "autoRenewPeriod must not be null");
    this.autoRenewPeriod = autoRenewPeriod;
    return this;
  }

  public AccountUpdateTransaction withAutoRenewPeriod(final long autoRenewPeriod) {
    return withAutoRenewPeriod(Duration.of(autoRenewPeriod));
  }

  public AccountUpdateTransaction withMaxAutomaticTokenAssociations(int maxAutomaticTokenAssociations) {
    this.maxAutomaticTokenAssociations = maxAutomaticTokenAssociations;
    return this;
  }

  public CryptoUpdateTransactionBody toProto() {
    var updateProto = CryptoUpdateTransactionBody.newBuilder();
    updateProto.setAccountIDToUpdate(this.accountId.toProto());

    if (this.autoRenewPeriod != null) {
      updateProto.setAutoRenewPeriod(this.autoRenewPeriod.toProto());
    }

    if (this.accountMemo != null) {
      updateProto.setMemo(StringValue.of(this.accountMemo));
    }

    if (this.key != null) {
      updateProto.setKey(this.key.toProto());
    }

    if (this.maxAutomaticTokenAssociations != Integer.MIN_VALUE) {
      updateProto.setMaxAutomaticTokenAssociations(Int32Value.of(this.maxAutomaticTokenAssociations));
    }

    return updateProto.build();
  }

  @Override
  public AccountUpdateTransaction fromProto(@NonNull final TransactionBody transactionBodyProto) {
    Objects.requireNonNull(transactionBodyProto, "transactionBodyProto must not be null");
    var proto = transactionBodyProto.getCryptoUpdateAccount();
    AccountUpdateTransaction transaction = new AccountUpdateTransaction();

    transaction.withAccountId(AccountId.fromProto(proto.getAccountIDToUpdate()));
    if (proto.hasKey()) {
      transaction.withKey(PublicKey.fromProto(proto.getKey()));
    }

    if (proto.hasMemo()) {
      transaction.withAccountMemo(proto.getMemo().getValue());
    }

    if (proto.hasAutoRenewPeriod()) {
      transaction.withAutoRenewPeriod(Duration.fromProto(proto.getAutoRenewPeriod()));
    }

    if (proto.hasMaxAutomaticTokenAssociations()) {
      transaction.withMaxAutomaticTokenAssociations(proto.getMaxAutomaticTokenAssociations().getValue());
    }

    return transaction;
  }

  @Override
  protected void buildTransaction(@NonNull final Client client) {
    this.buildBaseTransaction(client);
    this.transactionBodyBuilder.setCryptoUpdateAccount(this.toProto());
  }

  @Override
  protected MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction, TransactionResponse> getMethodDescriptor() {
    return CryptoServiceGrpc.getUpdateAccountMethod();
  }
}
