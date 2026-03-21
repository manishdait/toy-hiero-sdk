package io.github.manishdait.sdk;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.hedera.hashgraph.sdk.proto.ResponseCodeEnum;
import org.junit.jupiter.api.Test;

public class StatusTest {
  @Test
  void shouldCreateStatusForResponseCodeEnum() {
    for (ResponseCodeEnum responseCodeEnum : ResponseCodeEnum.values()) {
      if (responseCodeEnum == ResponseCodeEnum.UNRECOGNIZED) {
        continue;
      }

      var status = Status.valueOf(responseCodeEnum);
      assertThat(status.toResponseCode()).isEqualTo(responseCodeEnum.getNumber());
    }
  }

  @Test
  void shouldCreateStatusFromResponseCodeValue() {
    for (ResponseCodeEnum responseCodeEnum : ResponseCodeEnum.values()) {
      if (responseCodeEnum == ResponseCodeEnum.UNRECOGNIZED) {
        continue;
      }

      var status = Status.fromResponseCode(responseCodeEnum.getNumber());
      assertThat(status.toResponseCode()).isEqualTo(responseCodeEnum.getNumber());
    }
  }
}
