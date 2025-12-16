package org.example.sdk.account;

import com.hedera.hashgraph.sdk.proto.AccountID;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public record AccountId (
  long shard,
  long realm,
  long num
) {
  public AccountId {
    if (shard < 0) {
      throw new RuntimeException("shard must be non-negative number.");
    }

    if (realm < 0) {
      throw new RuntimeException("realm must be non-negative number.");
    }
  }

  public static AccountId fromString(@NonNull final String accountId) {
    Objects.requireNonNull(accountId, "accountId must not be null.");
    String[] parts = accountId.split("\\.");

    return new AccountId(
      Long.parseLong(parts[0]),
      Long.parseLong(parts[1]),
      Long.parseLong(parts[2])
    );
  }

  public static AccountId fromProto(@NonNull final AccountID proto) {
    Objects.requireNonNull(proto, "proto must not be null.");
    return new AccountId(
      proto.getShardNum(),
      proto.getRealmNum(),
      proto.getAccountNum()
    );
  }

  public AccountID toProto() {
    return AccountID.newBuilder()
      .setShardNum(this.shard)
      .setAccountNum(this.num)
      .setRealmNum(this.realm)
      .build();
  }
}
