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

public class Client {
  private Account operatorAccount;
  private final Network network;

  private final ManagedChannel mirrorChannel;

  private Client(@NonNull final Network network) {
    Objects.requireNonNull(network, "network must not be null");
    this.network = network;
    this.mirrorChannel = ManagedChannelBuilder
      .forTarget(Config.MIRROR_NODE_ADDRESS.get(network.getNetworkType()))
      .build();

    this.network.setNodes(this);
  };

  public @NonNull static Client forTestnet() {
    return new Client(new Network(NetworkType.TESTNET));
  }

  public @NonNull static Client forMainnet() {
    return new Client(new Network(NetworkType.MAINNET));
  }

  public @NonNull static Client forPreviewnet() {
    return new Client(new Network(NetworkType.PREVIEWNET));
  }

  public @NonNull static Client forSolo() {
    return new Client(new Network(NetworkType.SOLO));
  }

  public void setOperatorAccount(@NonNull final Account operatorAccount) {
    Objects.requireNonNull(operatorAccount, "operatorAccount must not be null");
    this.operatorAccount = operatorAccount;
  }

  public void setOperatorAccount(@NonNull final AccountId accountId, @NonNull final PrivateKey privateKey) {
    Objects.requireNonNull(accountId, "accountId must not be null");
    Objects.requireNonNull(privateKey, "privateKey must not be null");
    this.setOperatorAccount(new Account(accountId, privateKey));
  }

  public @NonNull Account getOperatorAccount() {
    return this.operatorAccount;
  }

  public @NonNull Node getNode() {
    return this.network.getNode();
  }

  public @NonNull AccountId getOperatorAccountId() {
    return this.operatorAccount.accountId();
  }

  public @NonNull PrivateKey getOperatorPrivateKey() {
    return this.operatorAccount.privateKey();
  }

  public @NonNull Network getNetwork() {
    return this.network;
  }

  public  @NonNull ManagedChannel getMirrorChannel() {
    return this.mirrorChannel;
  }
}
