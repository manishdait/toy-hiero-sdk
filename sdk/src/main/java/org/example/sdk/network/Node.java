package org.example.sdk.network;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.sdk.account.AccountId;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class Node {
  private final String address;
  private final int port;
  private final AccountId accountId;

  private ManagedChannel channel;

  public Node(@NonNull final String address, final int port, @NonNull final AccountId accountId) {
    Objects.requireNonNull(address, "address must not be null.");
    Objects.requireNonNull(accountId, "accountId must not be null.");

    this.address = address;
    this.port = port;
    this.accountId = accountId;
  }

  public AccountId getAccountId() {
    return this.accountId;
  }

  public String getNodeAddress() {
    return "%s:%d".formatted(this.address, this.port);
  }

  public ManagedChannel getChannel() {
    if (channel != null) return channel;

    channel = ManagedChannelBuilder.forAddress(this.address, this.port).usePlaintext().build();

    return channel;
  }
}
