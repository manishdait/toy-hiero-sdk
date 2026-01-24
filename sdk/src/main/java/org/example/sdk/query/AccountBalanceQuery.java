package org.example.sdk.query;

import com.hedera.hashgraph.sdk.proto.CryptoGetAccountBalanceQuery;
import com.hedera.hashgraph.sdk.proto.QueryHeader;
import com.hedera.hashgraph.sdk.proto.Response;
import com.hedera.hashgraph.sdk.proto.ResponseHeader;
import com.hedera.hashgraph.sdk.proto.CryptoServiceGrpc;

import com.hedera.hashgraph.sdk.proto.ResponseType;
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
  protected ResponseHeader getResponseHeader(@NonNull final Response response) {
    Objects.requireNonNull(response, "response must not be null");
    return response.getCryptogetAccountBalance().getHeader();
  }

  @Override
  public com.hedera.hashgraph.sdk.proto.Query toProto(@NonNull final Client client) {
    Objects.requireNonNull(client, "client must not be null");

    var query = CryptoGetAccountBalanceQuery.newBuilder()
      .setAccountID(this.accountId.toProto())
      .setHeader(
        QueryHeader.newBuilder()
          .setResponseType(ResponseType.ANSWER_ONLY)
          .build()
      )
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

    var proto = this.performQuery(client).getCryptogetAccountBalance();
    return AccountBalance.fromProto(proto);
  }
}
