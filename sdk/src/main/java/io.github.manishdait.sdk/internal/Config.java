package io.github.manishdait.sdk.internal;

import io.github.manishdait.sdk.network.NetworkType;
import java.util.Map;

public class Config {
  public static final int MAX_ATTEMPTS = 10;

  public static final int TLS_PORT = 50212;
  public static final int PLAIN_PORT = 50211;

  public static final Map<NetworkType, String> MIRROR_NODE_ADDRESS =
      Map.of(
          NetworkType.MAINNET, "mainnet.mirrornode.hedera.com:443",
          NetworkType.PREVIEWNET, "previewnet.mirrornode.hedera.com:443",
          NetworkType.TESTNET, "testnet.mirrornode.hedera.com:443",
          NetworkType.SOLO, "localhost:5600");
}
