package io.github.manishdait.sdk;

import io.github.manishdait.sdk.account.Account;
import io.github.manishdait.sdk.account.AccountId;
import io.github.manishdait.sdk.internal.Config;
import io.github.manishdait.sdk.internal.network.NetworkConstant;
import io.github.manishdait.sdk.key.PrivateKey;
import io.github.manishdait.sdk.network.Network;
import io.github.manishdait.sdk.network.NetworkType;
import io.github.manishdait.sdk.network.Node;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.time.Duration;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

/** Client for Hiero network containing the operator account and network information. */
public class Client {
  private int maxAttempts = Config.DEFAULT_MAX_ATTEMPTS;
  private Duration minBackoff = Config.DEFAULT_MIN_BACKOFF;
  private Duration maxBackoff = Config.DEFAULT_MAX_BACKOFF;
  private Duration grpcTimeout = Config.DEFAULT_GRPC_TIMEOUT;
  private Duration requestTimeout = Config.DEFAULT_REQUEST_TIMEOUT;

  private Account operatorAccount;
  private final Network network;

  private final ManagedChannel mirrorChannel;

  /**
   * Constructor.
   *
   * @param network type of {@code Network} for which the client must create
   */
  private Client(@NonNull final Network network) {
    Objects.requireNonNull(network, "network must not be null");

    this.network = network;
    this.mirrorChannel =
        ManagedChannelBuilder.forTarget(Config.MIRROR_NODE_ADDRESS.get(network.getNetworkType()))
            .build();

    if (network.getNetworkType() != NetworkType.SOLO) {
      this.network.setNodes(this);
    }
  }

  public int getMaxAttempts() {
    return maxAttempts;
  }

  public Client withMaxAttempts(int maxAttempts) {
    if (maxAttempts <= 0) {
      throw new IllegalArgumentException("maxAttempts must be greater than 0");
    }
    this.maxAttempts = maxAttempts;
    return this;
  }

  public Duration getMinBackoff() {
    return minBackoff;
  }

  public Client withMinBackoff(@NonNull final Duration minBackoff) {
    Objects.requireNonNull(minBackoff, "minBackoff must not be null");

    if (minBackoff.toNanos() <= 0 && minBackoff.compareTo(maxBackoff) < 0) {
      throw new IllegalArgumentException(
          "minBackoff must be greater than 0 and less than maxBackoff");
    }

    this.minBackoff = minBackoff;
    return this;
  }

  public Duration getMaxBackoff() {
    return maxBackoff;
  }

  public Client withMaxBackoff(@NonNull final Duration maxBackoff) {
    Objects.requireNonNull(maxBackoff, "maxBackoff must not be null");

    if (maxBackoff.toNanos() <= 0 && maxBackoff.compareTo(minBackoff) > 0) {
      throw new IllegalArgumentException(
          "maxBackoff must be greater than 0 and greater than minBackoff");
    }

    this.maxBackoff = maxBackoff;
    return this;
  }

  public Duration getRequestTimeout() {
    return requestTimeout;
  }

  public Client withRequestTimeout(final Duration requestTimeout) {
    this.requestTimeout = requestTimeout;
    return this;
  }

  public Duration getGrpcTimeout() {
    return grpcTimeout;
  }

  public Client withGrpcTimeout(final Duration grpcTimeout) {
    this.grpcTimeout = grpcTimeout;
    return this;
  }

  /**
   * Create a client for testnet.
   *
   * @return the new instance of {@code Client}
   */
  public @NonNull static Client forTestnet() {
    return new Client(new Network(NetworkType.TESTNET));
  }

  /**
   * Create a client for mainnet.
   *
   * @return the new instance of {@code Client}
   */
  public @NonNull static Client forMainnet() {
    return new Client(new Network(NetworkType.MAINNET));
  }

  /**
   * Create a client for previewnet.
   *
   * @return the new instance of {@code Client}
   */
  public @NonNull static Client forPreviewnet() {
    return new Client(new Network(NetworkType.PREVIEWNET));
  }

  /**
   * Create a client form env var.
   *
   * @return the new instance of {@code Client}
   */
  public @NonNull static Client fromEnv() {
    String accountId = System.getenv("HIERO_ACCOUNT_ID");
    String privateKey = System.getenv("HIERO_PRIVATE_KEY");

    Client client = new Client(new Network(NetworkType.SOLO));
    client.setOperatorAccount(AccountId.fromString(accountId), PrivateKey.fromString(privateKey));
    client.network.setNodes(NetworkConstant.DEFAULT_NODES.get(NetworkType.SOLO));
    return client;
  }

  /**
   * Set operator account for the client.
   *
   * @param operatorAccount the account to be set as operator.
   */
  public void setOperatorAccount(@NonNull final Account operatorAccount) {
    Objects.requireNonNull(operatorAccount, "operatorAccount must not be null");
    this.operatorAccount = operatorAccount;
  }

  /**
   * Set operator account for the client.
   *
   * @param accountId the operator account id
   * @param privateKey the operator private key
   */
  public void setOperatorAccount(
      @NonNull final AccountId accountId, @NonNull final PrivateKey privateKey) {
    Objects.requireNonNull(accountId, "accountId must not be null");
    Objects.requireNonNull(privateKey, "privateKey must not be null");
    this.setOperatorAccount(new Account(accountId, privateKey));
  }

  /**
   * Get operator account for the client.
   *
   * @return the operator {@code Account}
   */
  public @NonNull Account getOperatorAccount() {
    return this.operatorAccount;
  }

  /**
   * Get network node for the client.
   *
   * @return the network {@code Node}
   */
  public @NonNull Node getNode() {
    return this.network.getNode();
  }

  /**
   * Get operator account id for the client.
   *
   * @return the operator {@code AccountId}
   */
  public @NonNull AccountId getOperatorAccountId() {
    return this.operatorAccount.accountId();
  }

  /**
   * Get operator account private key for the client.
   *
   * @return the operator {@code PrivateKey}
   */
  public @NonNull PrivateKey getOperatorPrivateKey() {
    return this.operatorAccount.privateKey();
  }

  /**
   * Get network for the client.
   *
   * @return {@code Network}
   */
  public @NonNull Network getNetwork() {
    return this.network;
  }

  /**
   * Get mirror channel for the client.
   *
   * @return {@code ManagedChannel}
   */
  public @NonNull ManagedChannel getMirrorChannel() {
    return this.mirrorChannel;
  }
}
