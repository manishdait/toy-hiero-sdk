package com.example.sdk.internal.network;

import org.example.sdk.internal.network.NetworkUtils;
import org.example.sdk.network.NetworkType;
import org.junit.jupiter.api.Test;

public class NetworkUtilsTest {
  @Test
  void shouldFetchNetworkNodes() {
    var nodes = NetworkUtils.fetchNodes(NetworkType.TESTNET);
    System.out.println(nodes);
  }
}
