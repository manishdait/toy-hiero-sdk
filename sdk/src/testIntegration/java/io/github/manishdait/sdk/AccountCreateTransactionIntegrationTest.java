package io.github.manishdait.sdk;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.github.manishdait.sdk.account.AccountCreateTransaction;
import io.github.manishdait.sdk.key.PrivateKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountCreateTransactionIntegrationTest {
  Client client;

  @BeforeEach
  void setup() {
    client = Client.fromEnv();
  }

  @Test
  void shouldCreateAccount() {
    var tx =
        new AccountCreateTransaction()
            .withKey(PrivateKey.generate())
            .withInitialBalance(Hbar.of(1))
            .pack(client);

    var response = tx.send();
    var receipt = response.queryReceipt();

    assertThat(receipt).isNotNull();
    assertThat(receipt.accountId()).isNotNull();
  }
}
