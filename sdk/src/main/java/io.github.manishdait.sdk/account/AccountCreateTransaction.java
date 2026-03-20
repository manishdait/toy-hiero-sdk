package io.github.manishdait.sdk.account;

import com.hedera.hashgraph.sdk.proto.CryptoCreateTransactionBody;
import com.hedera.hashgraph.sdk.proto.CryptoServiceGrpc;
import com.hedera.hashgraph.sdk.proto.TransactionBody;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import io.github.manishdait.sdk.Hbar;
import io.github.manishdait.sdk.HbarUnit;
import io.grpc.MethodDescriptor;
import io.github.manishdait.sdk.Client;
import io.github.manishdait.sdk.Duration;
import io.github.manishdait.sdk.key.Key;
import io.github.manishdait.sdk.key.PublicKey;
import io.github.manishdait.sdk.transaction.Transaction;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class AccountCreateTransaction extends Transaction<AccountCreateTransaction> {
  private Key key;
  private Hbar initialBalance;
  private Duration autoRenewPeriod;
  private String accountMemo;
  private int maxAutomaticTokenAssociations;

  public AccountCreateTransaction() {
    this.autoRenewPeriod = Duration.of(7_890_000L);
    this.accountMemo = "";
  }

  public Key getKey() {
    return key;
  }

  public AccountCreateTransaction withKey(Key key) {
    this.key = key;
    return this;
  }

  public Hbar getInitialBalance() {
    return initialBalance;
  }

  public AccountCreateTransaction withInitialBalance(@NonNull Hbar initialBalance) {
    Objects.requireNonNull(initialBalance, "initialBalance must not be null");
    this.initialBalance = initialBalance;
    return this;
  }

  public AccountCreateTransaction withInitialBalance(long initialBalance) {
    return withInitialBalance(Hbar.of(initialBalance, HbarUnit.TINYBAR));
  }

  public AccountCreateTransaction withAutoRenewPeriod(Duration autoRenewPeriod) {
    this.autoRenewPeriod = autoRenewPeriod;
    return this;
  }

  public Duration getAutoRenewPeriod() {
    return autoRenewPeriod;
  }

  public AccountCreateTransaction withAutoRenewPeriod(long autoRenewPeriod) {
    return this.withAutoRenewPeriod(Duration.of(autoRenewPeriod));
  }

  public AccountCreateTransaction withMaxAutomaticTokenAssociation(int maxAutomaticTokenAssociations) {
    this.maxAutomaticTokenAssociations = maxAutomaticTokenAssociations;
    return this;
  }

  public String getAccountMemo() {
    return accountMemo;
  }

  public AccountCreateTransaction withAccountMemo(String memo) {
    this.accountMemo = memo;
    return this;
  }


  private CryptoCreateTransactionBody toProto() {
    return CryptoCreateTransactionBody.newBuilder()
      .setKey(key.toProto())
      .setMemo(this.accountMemo)
      .setInitialBalance(this.initialBalance.getValueInTinybar())
      .setAutoRenewPeriod(this.autoRenewPeriod.toProto())
      .setMaxAutomaticTokenAssociations(this.maxAutomaticTokenAssociations)
      .build();
  }

  @Override
  public AccountCreateTransaction fromProto(@NonNull final TransactionBody transactionBodyProto) {
    CryptoCreateTransactionBody proto = transactionBodyProto.getCryptoCreateAccount();

    return new AccountCreateTransaction()
      .withAccountMemo(proto.getMemo())
      .withInitialBalance(Hbar.of(proto.getInitialBalance(), HbarUnit.TINYBAR))
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
