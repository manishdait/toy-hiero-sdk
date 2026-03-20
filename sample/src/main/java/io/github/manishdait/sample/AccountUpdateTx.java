package io.github.manishdait.sample;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.manishdait.sdk.Client;
import io.github.manishdait.sdk.Status;
import io.github.manishdait.sdk.account.AccountCreateTransaction;
import io.github.manishdait.sdk.account.AccountId;
import io.github.manishdait.sdk.account.AccountUpdateTransaction;
import io.github.manishdait.sdk.key.PrivateKey;
import io.github.manishdait.sdk.transaction.TransactionReceipt;
import io.github.manishdait.sdk.transaction.TransactionResponse;

public class AccountUpdateTx {
  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.load();

    Client client = Client.forTestnet();
    client.setOperatorAccount(
        AccountId.fromString(dotenv.get("HIERO_ACCOUNT_ID")),
        PrivateKey.fromString(dotenv.get("HIERO_PRIVATE_KEY")));

    PrivateKey privateKey = PrivateKey.generate();
    TransactionResponse transactionResponse =
        new AccountCreateTransaction()
            .withKey(privateKey)
            .withInitialBalance(1)
            .withAccountMemo("Test SDK Account")
            .pack(client)
            .send();

    TransactionReceipt receipt = transactionResponse.queryReceipt();

    if (receipt.status() != Status.SUCCESS) {
      throw new RuntimeException("Unable to create an account");
    }

    AccountId accountId = receipt.accountId();
    System.out.println("Account Create with ID: " + receipt.accountId());

    TransactionReceipt accountUpdateReceipt =
        new AccountUpdateTransaction()
            .withAccountId(accountId)
            .withAccountMemo("Update the Account Tx")
            .pack(client)
            .signWith(privateKey)
            .send()
            .queryReceipt();

    System.out.println(accountUpdateReceipt);
  }
}
