package io.github.manishdait.sdk.network;

import io.github.manishdait.sdk.account.AccountId;
import io.github.manishdait.sdk.internal.Config;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public class Node {
  private final String address;
  private final int port;
  private final AccountId accountId;

  private ManagedChannel channel;

  private Duration minBackoff = Config.DEFAULT_NODE_MIN_BACKOFF;
  private Duration maxBackoff = Config.DEFAULT_NODE_MAX_BACKOFF;
  private Duration currentBackoff = minBackoff;
  private long readmitTime = System.nanoTime();
  private long badGrpcResponseCount = 0;

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

  public Duration getMinBackoff() {
    return minBackoff;
  }

  public Duration getMaxBackoff() {
    return maxBackoff;
  }

  public Duration getCurrentBackoff() {
    return currentBackoff;
  }

  public long getReadmitTime() {
    return readmitTime;
  }

  public long getBadGrpcResponseCount() {
    return badGrpcResponseCount;
  }

  public ManagedChannel getChannel() {
    if (channel != null) return channel;
    channel = ManagedChannelBuilder.forAddress(this.address, this.port).usePlaintext().build();
    return channel;
  }

  public void close() {
    if (channel != null && !channel.isShutdown()) {
      channel.shutdown();
    }
  }

  public boolean isHealthy() {
    return readmitTime <= System.nanoTime();
  }

  public void increaseBackoff() {
    badGrpcResponseCount += 1;
    currentBackoff = currentBackoff.multipliedBy(2);

    if (currentBackoff.compareTo(maxBackoff) > 0) {
      currentBackoff = maxBackoff;
    }

    readmitTime = currentBackoff.toNanos() + System.nanoTime();
  }

  public void decreaseBackoff() {
    currentBackoff = currentBackoff.dividedBy(2);

    if (currentBackoff.compareTo(minBackoff) < 0) {
      currentBackoff = minBackoff;
    }
  }

  @Override
  public String toString() {
    return "Node{"
        + "address='"
        + address
        + '\''
        + ", port="
        + port
        + ", accountId="
        + accountId
        + ", channel="
        + channel
        + '}';
  }

  protected static String resolveAddressFromBytes(byte[] bytes) {
    try {
      return InetAddress.getByAddress(bytes).getHostAddress();
    } catch (UnknownHostException e) {
      throw new RuntimeException("Unable to resolve ip address");
    }
  }
}
