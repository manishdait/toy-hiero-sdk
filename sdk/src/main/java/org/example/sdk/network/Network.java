package org.example.sdk.network;

import org.example.sdk.account.AccountId;

public class Network {
  private final Node node = new Node("34.133.197.230", 50211, AccountId.fromString("0.0.9"));

  public Node getNode() {
    return this.node;
  }
}
