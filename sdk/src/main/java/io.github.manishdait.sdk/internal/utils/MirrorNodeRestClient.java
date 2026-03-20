package io.github.manishdait.sdk.internal.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.manishdait.sdk.internal.network.NetworkConstant;
import io.github.manishdait.sdk.network.NetworkType;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;

public final class MirrorNodeRestClient {
  private static final HttpClient CLIENT;
  private static final ObjectMapper MAPPER;

  static {
    CLIENT = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_1_1)
      .connectTimeout(Duration.ofSeconds(10))
      .build();

    MAPPER = new ObjectMapper();
  }

  public static JsonNode query(
    @NonNull final NetworkType networkType,
    @NonNull final String endpoint
  ) {
    Objects.requireNonNull(networkType, "networkType must not be null");

    try {
      var url = NetworkConstant.MIRROR_NODE_REST_API.get(networkType) + endpoint;
      var response = makeRequest(url);

      return MAPPER.readTree(response);
    } catch (Exception e) {
      return null;
    }
  }

  private static String makeRequest(@NonNull final String endpoint) {
    Objects.requireNonNull(endpoint, "endpoint must not be null");

    var request = HttpRequest.newBuilder()
      .uri(URI.create(endpoint))
      .GET()
      .build();

    try {
      var response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        return response.body();
      }

      throw new RuntimeException("Failed to query " + endpoint + " status: " + response.statusCode());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to query " + endpoint, e);
    }
  }
}
