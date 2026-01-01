package org.example.sdk.query;

import com.hedera.hashgraph.sdk.proto.Response;
import com.hedera.hashgraph.sdk.proto.ResponseCodeEnum;
import com.hedera.hashgraph.sdk.proto.ResponseHeader;
import io.grpc.CallOptions;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import org.example.sdk.Client;
import org.example.sdk.Status;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

public abstract class Query {
  protected static final int MAX_ATTEMPTS = 10;

  protected abstract ResponseHeader getResponseHeader(@NonNull final Response response);
  public abstract com.hedera.hashgraph.sdk.proto.Query toProto(@NonNull final Client client);
  protected abstract MethodDescriptor<com.hedera.hashgraph.sdk.proto.Query, Response> getMethodDescriptor();

  protected boolean shouldRetry(@NonNull final ResponseCodeEnum responseCodeEnum) {
    Objects.requireNonNull(responseCodeEnum, "responseCodeEnum must not be null");
    final List<Status> RETRYABLE_RESPONSE = List.of(
      Status.UNKNOWN,
      Status.BUSY,
      Status.RECEIPT_NOT_FOUND,
      Status.RECORD_NOT_FOUND,
      Status.PLATFORM_NOT_ACTIVE
    );

    return RETRYABLE_RESPONSE.contains(Status.valueOf(responseCodeEnum));
  }

  protected Response performQuery(@NonNull final Client client) {
    for (int i = 0; i <= MAX_ATTEMPTS; i++) {
      var call = client.getNode().getChannel().newCall(this.getMethodDescriptor(), CallOptions.DEFAULT);
      var response = ClientCalls.blockingUnaryCall(call, this.toProto(client));

      var header = getResponseHeader(response);
      if (Status.fromResponseCode(header.getNodeTransactionPrecheckCode().getNumber()) == Status.OK) {
        return response;
      }

      if (!shouldRetry(header.getNodeTransactionPrecheckCode())) {
        throw new RuntimeException("Fail to query receipt status " + Status.valueOf(header.getNodeTransactionPrecheckCode()).toString());
      }

      client.getNetwork().selectNode();
    }

    throw new RuntimeException("Fail to get query result timeout");
  }
}
