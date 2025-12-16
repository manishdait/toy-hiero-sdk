package org.example.sdk.account;

import com.hedera.hashgraph.sdk.proto.CryptoCreateTransactionBody;
import com.hedera.hashgraph.sdk.proto.CryptoServiceGrpc;
import com.hedera.hashgraph.sdk.proto.TransactionBody;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import io.grpc.MethodDescriptor;
import org.example.sdk.Client;
import org.example.sdk.Duration;
import org.example.sdk.key.Key;
import org.example.sdk.key.PublicKey;
import org.example.sdk.transaction.Transaction;
import org.jspecify.annotations.NonNull;

public class AccountCreateTransaction extends Transaction<AccountCreateTransaction> {
  private Key key;
  private long initialBalance;
  private Duration autoRenewPeriod;
  private String accountMemo;
  private int maxAutomaticTokenAssociations;

  public AccountCreateTransaction() {
    this.autoRenewPeriod = Duration.of(7_890_000L);
  }

  public AccountCreateTransaction withKey(Key key) {
    this.key = key;
    return this;
  }

  public AccountCreateTransaction withInitialBalance(long initialBalance) {
    this.initialBalance = initialBalance;
    return this;
  }

  public AccountCreateTransaction withAutoRenewPeriod(Duration autoRenewPeriod) {
    this.autoRenewPeriod = autoRenewPeriod;
    return this;
  }

  public AccountCreateTransaction withAutoRenewPeriod(long autoRenewPeriod) {
    return this.withAutoRenewPeriod(Duration.of(autoRenewPeriod));
  }

  public AccountCreateTransaction withAccountMemo(String memo) {
    this.accountMemo = memo;
    return this;
  }

  public AccountCreateTransaction withMaxAutomaticTokenAssociation(int maxAutomaticTokenAssociations) {
    this.maxAutomaticTokenAssociations = maxAutomaticTokenAssociations;
    return this;
  }

  private CryptoCreateTransactionBody toProto() {
    return CryptoCreateTransactionBody.newBuilder()
      .setKey(key.toProto())
      .setMemo(this.accountMemo)
      .setInitialBalance(this.initialBalance)
      .setAutoRenewPeriod(this.autoRenewPeriod.toProto())
      .setMaxAutomaticTokenAssociations(this.maxAutomaticTokenAssociations)
      .build();
  }

  @Override
  public AccountCreateTransaction fromProto(@NonNull final TransactionBody transactionBodyProto) {
    CryptoCreateTransactionBody proto = transactionBodyProto.getCryptoCreateAccount();

    return new AccountCreateTransaction()
      .withAccountMemo(proto.getMemo())
      .withInitialBalance(proto.getInitialBalance())
      .withAutoRenewPeriod(Duration.fromProto(proto.getAutoRenewPeriod()))
      .withKey(PublicKey.fromProto(proto.getKey()))
      .withMaxAutomaticTokenAssociation(proto.getMaxAutomaticTokenAssociations());
  }

  @Override
  protected void buildTransaction(Client client) {
    this.buildBaseTransaction(client);
    this.transactionBodyBuilder.setCryptoCreateAccount(this.toProto());
  }

  @Override
  protected MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction, TransactionResponse> getMethodDescriptor() {
    return CryptoServiceGrpc.getCreateAccountMethod();
  }
}
