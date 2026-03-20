package io.github.manishdait.sdk;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.github.manishdait.sdk.account.Account;
import io.github.manishdait.sdk.account.AccountId;
import io.github.manishdait.sdk.internal.Config;
import io.github.manishdait.sdk.key.PrivateKey;
import io.github.manishdait.sdk.network.Network;
import io.github.manishdait.sdk.network.NetworkType;
import io.github.manishdait.sdk.network.Node;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Client for Hiero network containing the operator account and network information.
 */
public class Client {
  private Account operatorAccount;
  private final Network network;

  private final ManagedChannel mirrorChannel;

  /**
   * Constructor.
   * @param network type of {@code Network} for which the client must create
   */
  private Client(@NonNull final Network network) {
    Objects.requireNonNull(network, "network must not be null");

    this.network = network;
    this.mirrorChannel = ManagedChannelBuilder
      .forTarget(Config.MIRROR_NODE_ADDRESS.get(network.getNetworkType()))
      .build();

    this.network.setNodes(this);
  };

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
   * Create a client for solo.
   *
   * @return the new instance of {@code Client}
   */
  public @NonNull static Client forSolo() {
    return new Client(new Network(NetworkType.SOLO));
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
  public void setOperatorAccount(@NonNull final AccountId accountId, @NonNull final PrivateKey privateKey) {
    Objects.requireNonNull(accountId, "accountId must not be null");
    Objects.requireNonNull(privateKey, "privateKey must not be null");
    this.setOperatorAccount(new Account(accountId, privateKey));
  }

  /**
   * Get operator account for the client.
   * @return the operator {@code Account}
   */
  public @NonNull Account getOperatorAccount() {
    return this.operatorAccount;
  }

  /**
   * Get network node for the client.
   * @return the network {@code Node}
   */
  public @NonNull Node getNode() {
    return this.network.getNode();
  }

  /**
   * Get operator account id for the client.
   * @return the operator {@code AccountId}
   */
  public @NonNull AccountId getOperatorAccountId() {
    return this.operatorAccount.accountId();
  }

  /**
   * Get operator account private key for the client.
   * @return the operator {@code PrivateKey}
   */
  public @NonNull PrivateKey getOperatorPrivateKey() {
    return this.operatorAccount.privateKey();
  }

  /**
   * Get network for the client.
   * @return {@code Network}
   */
  public @NonNull Network getNetwork() {
    return this.network;
  }

  /**
   * Get mirror channel for the client.
   * @return {@code ManagedChannel}
   */
  public  @NonNull ManagedChannel getMirrorChannel() {
    return this.mirrorChannel;
  }
}
