package org.example.sdk.account;

import com.hedera.hashgraph.sdk.proto.CryptoDeleteTransactionBody;
import com.hedera.hashgraph.sdk.proto.CryptoServiceGrpc;
import com.hedera.hashgraph.sdk.proto.TransactionBody;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import io.grpc.MethodDescriptor;
import org.example.sdk.Client;
import org.example.sdk.transaction.Transaction;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class AccountDeleteTransaction extends Transaction<AccountDeleteTransaction> {
  private AccountId accountId;
  private AccountId transferAccountId;

  public AccountDeleteTransaction() {}

  public CryptoDeleteTransactionBody toProto() {
    return CryptoDeleteTransactionBody.newBuilder()
      .setDeleteAccountID(accountId.toProto())
      .setTransferAccountID(transferAccountId.toProto())
      .build();
  }

  public AccountDeleteTransaction withAccountId(@NonNull final AccountId accountId) {
    Objects.requireNonNull(accountId, "accountId must not be null");
    this.accountId = accountId;
    return this;
  }

  public AccountDeleteTransaction withTransferAccountId(@NonNull final AccountId transferAccountId) {
    Objects.requireNonNull(transferAccountId, "transferAccountId must not be null");
    this.transferAccountId = transferAccountId;
    return this;
  }

  @Override
  public AccountDeleteTransaction fromProto(@NonNull final TransactionBody transactionBodyProto) {
    Objects.requireNonNull(transactionBodyProto, "transactionBodyProto must not be null");

    CryptoDeleteTransactionBody proto = transactionBodyProto.getCryptoDelete();
    AccountDeleteTransaction transaction = new AccountDeleteTransaction();
    transaction.withAccountId(AccountId.fromProto(proto.getDeleteAccountID()));

    if (proto.hasTransferAccountID()) {
      transaction.withTransferAccountId(AccountId.fromProto(proto.getTransferAccountID()));
    }

    return transaction;
  }

  @Override
  protected void buildTransaction(@NonNull final Client client) {
    Objects.requireNonNull(client, "client must not be null");
    this.buildBaseTransaction(client);
    this.transactionBodyBuilder.setCryptoDelete(this.toProto());
  }

  @Override
  protected MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction, TransactionResponse> getMethodDescriptor() {
    return CryptoServiceGrpc.getCryptoDeleteMethod();
  }
}
