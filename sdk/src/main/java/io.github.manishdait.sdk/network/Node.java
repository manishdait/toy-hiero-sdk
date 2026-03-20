package io.github.manishdait.sdk.network;

import io.github.manishdait.sdk.account.AccountId;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public class Node {
  private final String address;
  private final int port;
  private final AccountId accountId;

  private ManagedChannel channel;

  public Node(@NonNull final String address, @NonNull final String accountId) {
    this(
        address.split(":")[0],
        Integer.parseInt(address.split(":")[1]),
        AccountId.fromString(accountId));
  }

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

  public int getPort() {
    return port;
  }

  public String getAddress() {
    return address;
  }

  public String getNodeAddress() {
    return "%s:%d".formatted(this.address, this.port);
  }

  public ManagedChannel getChannel() {
    if (channel != null) return channel;
    channel = ManagedChannelBuilder.forAddress(this.address, this.port).usePlaintext().build();
    return channel;
  }

  @Override
  public String toString() {
    return "Node["
        + "address='"
        + address
        + '\''
        + ", port="
        + port
        + ", accountId="
        + accountId
        + ", channel="
        + channel
        + ']';
  }

  protected static String resolveAddressFromBytes(byte[] bytes) {
    try {
      return InetAddress.getByAddress(bytes).getHostAddress();
    } catch (UnknownHostException e) {
      throw new RuntimeException("Unable to resolve ip address");
    }
  }
}
