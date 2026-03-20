package io.github.manishdait.sdk.query;

import com.hedera.hashgraph.sdk.proto.CryptoGetInfoQuery;
import com.hedera.hashgraph.sdk.proto.CryptoServiceGrpc;
import com.hedera.hashgraph.sdk.proto.Response;
import com.hedera.hashgraph.sdk.proto.ResponseHeader;

import io.grpc.MethodDescriptor;
import io.github.manishdait.sdk.Client;
import io.github.manishdait.sdk.account.AccountId;
import io.github.manishdait.sdk.account.AccountInfo;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class AccountInfoQuery extends Query {
  private AccountId accountId;

  public AccountInfoQuery() {}

  public AccountId getAccountId() {
    return this.accountId;
  }

  public AccountInfoQuery withAccountId(@NonNull final AccountId accountId) {
    Objects.requireNonNull(accountId, "accountId must not be null");
    this.accountId = accountId;
    return this;
  }

  @Override
  public com.hedera.hashgraph.sdk.proto.Query toProto() {
    var query = CryptoGetInfoQuery.newBuilder()
      .setAccountID(this.accountId.toProto())
      .setHeader(this.queryHeader)
      .build();

    return com.hedera.hashgraph.sdk.proto.Query.newBuilder()
      .setCryptoGetInfo(query)
      .build();
  }

  @Override
  protected MethodDescriptor<com.hedera.hashgraph.sdk.proto.Query, Response> getMethodDescriptor() {
    return CryptoServiceGrpc.getGetAccountInfoMethod();
  }


  @Override
  protected ResponseHeader getResponseHeader(@NonNull final Response response) {
    Objects.requireNonNull(response, "response must not be null");
    return response.getCryptoGetInfo().getHeader();
  }

  public AccountInfo query(@NonNull Client client) {
    Objects.requireNonNull(client, "client must not be null");
    this.doPreQueryCheck(client);

    var infoProto = this.execute(client).getCryptoGetInfo().getAccountInfo();
    return AccountInfo.fromProto(infoProto);
  }
}
