package org.example.sdk.internal;

import io.grpc.CallOptions;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import org.example.sdk.Client;
import org.example.sdk.internal.utils.ExecutionState;
import org.jspecify.annotations.NonNull;

public abstract class Executor <ProtoRequest, ProtoResponse> {
  private  final int MAX_ATTEMPTS = 10;

  protected abstract MethodDescriptor<ProtoRequest, ProtoResponse> getMethod();

  protected abstract ProtoRequest buildRequest();
  protected abstract ExecutionState getExecutionState(ProtoResponse response);

  public ProtoResponse execute(@NonNull final Client client) {
    final var request = this.buildRequest();

    for (int i = 0; i < this.MAX_ATTEMPTS; i++) {
      final var channel = client.getNode().getChannel();

      final var response = ClientCalls.blockingUnaryCall(
        channel.newCall(this.getMethod(), CallOptions.DEFAULT),
        request
      );

      final var executionState = this.getExecutionState(response);

      switch (executionState) {
        case FINISH -> {
          return response;
        }

        case RETRY -> {
          continue;
        }

        case EXPIRED -> throw new RuntimeException("Transaction Expired");

        case FAIL -> throw new RuntimeException("Transaction Fail");
      }
    }

    throw  new RuntimeException("Max attempts cross");
  }
}
