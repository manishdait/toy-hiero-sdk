package io.github.manishdait.sdk.exception;

import io.github.manishdait.sdk.Status;

public class PrecheckException extends RuntimeException {
  private final Status status;
  private final String message;

  public PrecheckException(final Status status, final String message) {
    super(message);
    this.status = status;
    this.message = message;
  }

  public Status getStatus() {
    return status;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
