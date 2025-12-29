package org.example.sdk.account;

import com.hedera.hashgraph.sdk.proto.CryptoGetInfoResponse;
import org.example.sdk.key.PublicKey;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record AccountInfo(
  @NonNull AccountId accountId,
  @Nullable String contractAccountId,
  boolean deleted,
  @NonNull PublicKey key,
  long balance
) {
  public static AccountInfo fromProto(final CryptoGetInfoResponse.AccountInfo proto) {
    return new AccountInfo(
      AccountId.fromProto(proto.getAccountID()),
      proto.getContractAccountID(),
      proto.getDeleted(),
      PublicKey.fromProto(proto.getKey()),
      proto.getBalance()
    );
  }
}
