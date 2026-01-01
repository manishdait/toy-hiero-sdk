package org.example.sdk.account;

import com.hedera.hashgraph.sdk.proto.CryptoGetAccountBalanceResponse;

public record AccountBalance(AccountId accountId, long balance) {
  public static AccountBalance fromProto(CryptoGetAccountBalanceResponse proto) {
    return new AccountBalance(AccountId.fromProto(proto.getAccountID()), proto.getBalance());
  }
}
