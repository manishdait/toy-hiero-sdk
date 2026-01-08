package org.example.sdk;

import org.example.sdk.account.Account;
import org.example.sdk.account.AccountId;
import org.example.sdk.key.PrivateKey;
import org.example.sdk.network.Network;
import org.example.sdk.network.NetworkType;
import org.example.sdk.network.Node;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class Client {
  private Account operatorAccount;
  private final Network network;

  private Client(@NonNull final Network network) {
    Objects.requireNonNull(network, "network must not be null");
    this.network = network;
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
}
