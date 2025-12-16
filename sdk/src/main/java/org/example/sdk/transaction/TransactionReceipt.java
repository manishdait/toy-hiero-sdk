package org.example.sdk.transaction;

import org.example.sdk.ExchangeRate;
import org.example.sdk.Status;
import org.example.sdk.account.AccountId;

public record TransactionReceipt(Status status, AccountId accountId, ExchangeRate currentRate, ExchangeRate nextRate) {
  public static TransactionReceipt fromProto(com.hedera.hashgraph.sdk.proto.TransactionReceipt proto) {
    return new TransactionReceipt(
      Status.valueOf(proto.getStatus()),
      proto.hasAccountID()? AccountId.fromProto(proto.getAccountID()) : null,
      ExchangeRate.fromProto(proto.getExchangeRate().getCurrentRate()),
      ExchangeRate.fromProto(proto.getExchangeRate().getNextRate())
    );
  }
}
