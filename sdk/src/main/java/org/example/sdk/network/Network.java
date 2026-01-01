package org.example.sdk.network;

import org.example.sdk.internal.constant.NetworkConstant;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

public class Network {
  private final NetworkType networkType;
  private final List<Node> nodes;

  private int nodeIndex;

  public Network(final @NonNull NetworkType networkType) {
    Objects.requireNonNull(networkType, "networkType must not be null");

    this.networkType = networkType;
    this.nodes = NetworkConstant.DEFAULT_NODES.get(networkType);
    this.nodeIndex = 0;
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
}
