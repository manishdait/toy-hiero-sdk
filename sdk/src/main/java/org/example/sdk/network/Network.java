package org.example.sdk.network;

import org.example.sdk.Client;
import org.example.sdk.account.AccountId;
import org.example.sdk.address_book.AddressBookQuery;
import org.example.sdk.internal.Config;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Network {
  private final NetworkType networkType;
  private final List<Node> nodes;
  private final Map<AccountId, List<Node>> proxies;

  private int nodeIndex;

  public Network(@NonNull final NetworkType networkType) {
    Objects.requireNonNull(networkType, "networkType must not be null");

    this.nodeIndex = 0;
    this.networkType = networkType;
    this.nodes = new ArrayList<>();
    this.proxies = new HashMap<>();
  }

  public NetworkType getNetworkType() {
    return networkType;
  }

  public Node getNode() {
    return this.nodes.get(nodeIndex);
  }

  public Node selectNode() {
    nodeIndex++;
    nodeIndex = nodeIndex % nodes.size();
    return getNode();
  }

  public void setNodes(@NonNull final Client client) {
    Objects.requireNonNull(client, "client must not be null");
    this.nodes.clear();
    this.proxies.clear();

    var addressBook = new AddressBookQuery().execute(client);

    for (var address : addressBook) {
      var nodeAccountId = address.nodeAccountId();

      for (var endpoint : address.serviceEndpoints()) {
        var node = new Node(Node.resolveAddressFromBytes(endpoint.ipAddressV4()), endpoint.port(), nodeAccountId);

        if (node.getPort() == Config.PLAIN_PORT) {
          if (!proxies.containsKey(nodeAccountId)) {
            proxies.put(nodeAccountId, new ArrayList<>());
          } else {
            proxies.get(nodeAccountId).add(node);
          }
        }
      }

      nodes.add(proxies.get(nodeAccountId).getFirst());
    }
  }
}
