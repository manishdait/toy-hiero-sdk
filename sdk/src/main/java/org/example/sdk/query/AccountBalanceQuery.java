package org.example.sdk.query;

import com.hedera.hashgraph.sdk.proto.*;
import io.grpc.MethodDescriptor;
import org.example.sdk.Client;
import org.example.sdk.account.AccountBalance;
import org.example.sdk.account.AccountId;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class AccountBalanceQuery extends Query {
  private AccountId accountId;
  // todo for contract id

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
  protected ResponseHeader getResponseHeader(@NonNull Response response) {
    return response.getCryptogetAccountBalance().getHeader();
  }

  @Override
  public com.hedera.hashgraph.sdk.proto.Query toProto(@NonNull Client client) {
    var query = CryptoGetAccountBalanceQuery.newBuilder()
      .setAccountID(this.accountId.toProto())
      .setHeader(QueryHeader.newBuilder().setResponseType(ResponseType.ANSWER_ONLY).build())
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
