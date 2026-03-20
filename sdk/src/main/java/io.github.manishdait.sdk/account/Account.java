package io.github.manishdait.sdk.account;

import io.github.manishdait.sdk.key.PrivateKey;
import io.github.manishdait.sdk.key.PublicKey;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public record Account(
  @NonNull AccountId accountId,
  @NonNull PrivateKey privateKey,
  @NonNull PublicKey publicKey
) {
  public Account {
    Objects.requireNonNull(accountId, "accountId must not be null.");
    Objects.requireNonNull(privateKey, "privateKey must not be null.");
    Objects.requireNonNull(publicKey, "publicKey must not be null.");
  }

  public Account(@NonNull AccountId accountId, @NonNull PrivateKey privateKey) {
    this(accountId, privateKey, privateKey.getPublicKey());
  }
}
