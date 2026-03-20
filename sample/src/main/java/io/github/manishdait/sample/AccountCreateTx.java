package io.github.manishdait.sample;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.manishdait.sdk.Client;
import io.github.manishdait.sdk.account.AccountId;
import io.github.manishdait.sdk.key.PrivateKey;
import io.github.manishdait.sdk.transaction.TransactionReceipt;
import io.github.manishdait.sdk.transaction.TransactionResponse;

public class AccountCreateTx {
  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.load();

    Client client = Client.forTestnet();
    client.setOperatorAccount(
        AccountId.fromString(dotenv.get("HIERO_ACCOUNT_ID")),
        PrivateKey.fromString(dotenv.get("HIERO_PRIVATE_KEY")));

    TransactionResponse transactionResponse =
        new io.github.manishdait.sdk.account.AccountCreateTransaction()
            .withKey(PrivateKey.generate())
            .withInitialBalance(1)
            .withMemo("Test SDK")
            .pack(client)
            .send();

    TransactionReceipt receipt = transactionResponse.queryReceipt();
    System.out.println(receipt);
  }
}
