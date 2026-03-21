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

    System.out.println(client.getOperatorAccount());
    System.out.println(client.getOperatorPrivateKey());

    var response = tx.send();
    System.out.println(response);

    var receipt = response.queryReceipt();

    System.out.println(receipt);
    assertThat(receipt).isNotNull();
    assertThat(receipt.accountId()).isNotNull();
  }
}
