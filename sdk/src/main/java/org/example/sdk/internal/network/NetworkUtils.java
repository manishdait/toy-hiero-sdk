package org.example.sdk.internal.network;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.sdk.account.AccountId;
import org.example.sdk.internal.utils.MirrorNodeRestClient;
import org.example.sdk.network.NetworkType;
import org.example.sdk.network.Node;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class NetworkUtils {
  public static List<Node> fetchNodes(final @NonNull NetworkType networkType) {
    Objects.requireNonNull(networkType, "networkType must not be null");

    var response = MirrorNodeRestClient.query(networkType, "/api/v1/network/nodes?limit=25");
    if (response == null) {
      return List.of();
    }

    if (response.has("nodes") && response.get("nodes").isArray()) {
      var fetchNodes = response.get("nodes");
      var nodes = new ArrayList<Node>();

      for (JsonNode node : fetchNodes) {
        var serviceEndPoints = node.get("service_endpoints");

        for (JsonNode endpoint : serviceEndPoints) {
          if (endpoint.get("port").asInt() != 50211) continue;

          var address = endpoint.get("ip_address_v4").asText();
          var port = endpoint.get("port").asInt();
          var accountId = AccountId.fromString(node.get("node_account_id").asText());

          nodes.add(new Node(address, port, accountId));
        }
      }

      return nodes;
    }

    return List.of();
  }
}
