package org.example.sdk;

import org.example.sdk.account.Account;
import org.example.sdk.account.AccountId;
import org.example.sdk.key.PrivateKey;
import org.example.sdk.network.Network;
import org.example.sdk.network.NetworkType;
import org.example.sdk.network.Node;
import org.jspecify.annotations.NonNull;

public class Client {
  private Account operatorAccount;
  private final Network network;

  private Client(@NonNull final Network network) {
    this.network = network;
  };

  public static Client forTestnet() {
    return new Client(new Network(NetworkType.TESTNET));
  }

  public static Client forMainnet() {
    return new Client(new Network(NetworkType.MAINNET));
  }

  public static Client forPreviewnet() {
    return new Client(new Network(NetworkType.PREVIEWNET));
  }

  public static Client forSolo() {
    return new Client(new Network(NetworkType.SOLO));
  }

  public void setOperatorAccount(@NonNull final Account operatorAccount) {
    this.operatorAccount = operatorAccount;
  }

  public void setOperatorAccount(@NonNull final AccountId accountId, @NonNull final PrivateKey privateKey) {
    this.setOperatorAccount(new Account(accountId, privateKey));
  }

  public Account getOperatorAccount() {
    return this.operatorAccount;
  }

  public Node getNode() {
    return this.network.getNode();
  }

  public AccountId getOperatorAccountId() {
    return this.operatorAccount.accountId();
  }

  public PrivateKey getOperatorPrivateKey() {
    return this.operatorAccount.privateKey();
  }

  public Network getNetwork() {
    return this.network;
  }
}
