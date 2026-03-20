package io.github.manishdait.sdk.internal;

import io.grpc.CallOptions;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.github.manishdait.sdk.Client;
import org.jspecify.annotations.NonNull;

public abstract class Executable <ProtoRequest, ProtoResponse> {
  protected abstract MethodDescriptor<ProtoRequest, ProtoResponse> getMethodDescriptor();

  protected abstract ProtoRequest buildRequest();
  protected abstract ExecutionState getExecutionState(ProtoResponse response);

  public ProtoResponse execute(@NonNull final Client client) {
    final var request = this.buildRequest();

    for (int i = 0; i < Config.MAX_ATTEMPTS; i++) {
      final var channel = client.getNode().getChannel();

      final var response = ClientCalls.blockingUnaryCall(
        channel.newCall(this.getMethodDescriptor(), CallOptions.DEFAULT),
        request
      );

      final var executionState = this.getExecutionState(response);

      switch (executionState) {
        case FINISH -> {
          return response;
        }
        case RETRY -> {
          client.getNetwork().selectNode();
          continue;
        }
        case EXPIRED -> {
          throw new RuntimeException("Transaction Expired");
        }
        case FAIL -> {
          throw new RuntimeException("Transaction Fail");
        }
      }
    }

    throw  new RuntimeException("Max attempts cross");
  }
}
