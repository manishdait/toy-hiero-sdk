package org.example.sdk.internal.constant;

import org.example.sdk.network.NetworkType;
import org.example.sdk.network.Node;

import java.util.List;
import java.util.Map;

public class NetworkConstant {
  public static Map<NetworkType, List<Node>> DEFAULT_NODES = Map.of(
    NetworkType.MAINNET, List.of(
      new Node("35.237.200.180:50211", "0.0.3"),
      new Node("35.186.191.247:50211", "0.0.4"),
      new Node("35.192.2.25:50211", "0.0.5"),
      new Node("35.199.161.108:50211", "0.0.6"),
      new Node("35.203.82.240:50211", "0.0.7"),
      new Node("35.236.5.219:50211", "0.0.8")
    ),

    NetworkType.PREVIEWNET, List.of(
      new Node("0.previewnet.hedera.com:50211", "0.0.3"),
      new Node("1.previewnet.hedera.com:50211", "0.0.4"),
      new Node("2.previewnet.hedera.com:50211", "0.0.5"),
      new Node("3.previewnet.hedera.com:50211", "0.0.6")
    ),

    NetworkType.TESTNET, List.of(
      new Node("0.testnet.hedera.com:50211", "0.0.3"),
      new Node("1.testnet.hedera.com:50211", "0.0.4"),
      new Node("2.testnet.hedera.com:50211", "0.0.5"),
      new Node("3.testnet.hedera.com:50211", "0.0.6")
    ),

    NetworkType.SOLO, List.of(
      new Node("localhost:50211", "0.0.3")
    )
  );
}
