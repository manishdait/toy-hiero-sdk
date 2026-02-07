package org.example.sdk.query;

import com.hedera.hashgraph.sdk.proto.CryptoGetAccountBalanceQuery;
import com.hedera.hashgraph.sdk.proto.Response;
import com.hedera.hashgraph.sdk.proto.ResponseHeader;
import com.hedera.hashgraph.sdk.proto.CryptoServiceGrpc;

import io.grpc.MethodDescriptor;
import org.example.sdk.Client;
import org.example.sdk.account.AccountBalance;
import org.example.sdk.account.AccountId;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class AccountBalanceQuery extends Query {
  private AccountId accountId;
  // TODO: add contractId

  public AccountBalanceQuery() {}

  public AccountId getAccountId() {
    return accountId;
  }

  public AccountBalanceQuery withAccountId(@NonNull final AccountId accountId) {
    Objects.requireNonNull(accountId, "accountId must not be null");
    this.accountId = accountId;
    return this;
  }

  @Override
  protected boolean requiredPayment() {
    return false;
  }

  @Override
  protected ResponseHeader getResponseHeader(@NonNull final Response response) {
    Objects.requireNonNull(response, "response must not be null");
    return response.getCryptogetAccountBalance().getHeader();
  }

  @Override
  public com.hedera.hashgraph.sdk.proto.Query toProto() {
    var query = CryptoGetAccountBalanceQuery.newBuilder()
      .setAccountID(this.accountId.toProto())
      .setHeader(this.queryHeader)
      .build();

    return com.hedera.hashgraph.sdk.proto.Query.newBuilder()
      .setCryptogetAccountBalance(query)
      .build();
  }

  @Override
  protected MethodDescriptor<com.hedera.hashgraph.sdk.proto.Query, Response> getMethodDescriptor() {
    return CryptoServiceGrpc.getCryptoGetBalanceMethod();
  }

  public AccountBalance query(@NonNull Client client) {
    Objects.requireNonNull(client, "client must not be null");
    this.doPreQueryCheck(client);

    var proto = this.execute(client).getCryptogetAccountBalance();
    return AccountBalance.fromProto(proto);
  }
}
