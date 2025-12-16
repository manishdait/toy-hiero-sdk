package org.example.sample;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.sdk.Client;
import org.example.sdk.account.AccountId;
import org.example.sdk.key.PrivateKey;
import org.example.sdk.transaction.TransactionResponse;

public class AccountCreateTransaction {
  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.load();

    Client client = Client.forTestnet();
    client.setOperatorAccount(
      AccountId.fromString(dotenv.get("OPERATOR_ACCOUNT_ID")),
      PrivateKey.fromString(dotenv.get("OPERATOR_PRIVATE_KEY"))
    );

    TransactionResponse transactionResponse = new org.example.sdk.account.AccountCreateTransaction()
      .withKey(PrivateKey.generate())
      .withInitialBalance(1)
      .withMemo("Test SDK")
      .pack(client)
      .send();
  }
}