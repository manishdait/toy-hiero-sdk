package io.github.manishdait.sdk.internal;

import io.github.manishdait.sdk.Client;
import io.github.manishdait.sdk.Status;
import io.github.manishdait.sdk.exception.PrecheckException;
import io.github.manishdait.sdk.exception.TimeoutException;
import io.grpc.CallOptions;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import java.time.Duration;
import org.jspecify.annotations.NonNull;

public abstract class Executable<ProtoRequest, ProtoResponse> {
  private Duration minBackoff;
  private Duration maxBackoff;
  private Duration requestTimeout;
  private Duration grpcTimeout;
  private Integer maxAttempts;

  public int getMaxAttempts() {
    return maxAttempts;
  }

  public void setMaxAttempts(int maxAttempts) {
    this.maxAttempts = maxAttempts;
  }

  public Duration getMinBackoff() {
    return minBackoff;
  }

  public void setMinBackoff(Duration minBackoff) {
    this.minBackoff = minBackoff;
  }

  public Duration getMaxBackoff() {
    return maxBackoff;
  }

  public void setMaxBackoff(Duration maxBackoff) {
    this.maxBackoff = maxBackoff;
  }

  public Duration getRequestTimeout() {
    return requestTimeout;
  }

  public void setRequestTimeout(Duration requestTimeout) {
    this.requestTimeout = requestTimeout;
  }

  protected abstract MethodDescriptor<ProtoRequest, ProtoResponse> getMethodDescriptor();

  protected abstract ProtoRequest buildRequest();

  protected abstract ExecutionState getExecutionState(ProtoResponse response);

  protected abstract Status getStatus(ProtoResponse response);

  public ProtoResponse execute(@NonNull final Client client) {
    final var request = this.buildRequest();

    resolveConfig(client);

    long startTime = System.nanoTime();
    for (int i = 0; i < maxAttempts; i++) {
      if (System.nanoTime() >= startTime + requestTimeout.toNanos()) {
        throw new TimeoutException("Request timeout error");
      }

      final var channel = client.getNode().getChannel();

      final var response =
          ClientCalls.blockingUnaryCall(
              channel.newCall(this.getMethodDescriptor(), CallOptions.DEFAULT), request);

      final var executionState = this.getExecutionState(response);
      final var status = this.getStatus(response);

      switch (executionState) {
        case FINISH -> {
          return response;
        }
        case RETRY -> {
          client.getNetwork().selectNode();
          try {
            Thread.sleep(calculateBackoff(i));
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
          continue;
        }
        case EXPIRED -> {
          throw new PrecheckException(status, "Transaction expired");
        }
        case FAIL -> {
          throw new PrecheckException(status, "Transaction fail");
        }
      }
    }

    throw new TimeoutException("Execution max attempts reached");
  }

  private long calculateBackoff(int i) {
    var multiplicity = (long) Math.pow(2, (i + 1));
    return minBackoff.multipliedBy(multiplicity).toMillis();
  }

  private void resolveConfig(Client client) {
    if (maxAttempts == null) {
      maxAttempts = client.getMaxAttempts();
    }

    if (minBackoff == null) {
      minBackoff = client.getMinBackoff();
    }

    if (maxBackoff == null) {
      maxBackoff = client.getMaxBackoff();
    }

    if (requestTimeout == null) {
      requestTimeout = client.getRequestTimeout();
    }

    if (grpcTimeout == null) {
      grpcTimeout = client.getGrpcTimeout();
    }
  }
}
