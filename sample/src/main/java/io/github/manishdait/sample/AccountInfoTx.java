package io.github.manishdait.sample;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.manishdait.sdk.Client;
import io.github.manishdait.sdk.Status;
import io.github.manishdait.sdk.account.AccountCreateTransaction;
import io.github.manishdait.sdk.account.AccountId;
import io.github.manishdait.sdk.account.AccountInfo;
import io.github.manishdait.sdk.key.PrivateKey;
import io.github.manishdait.sdk.query.AccountInfoQuery;
import io.github.manishdait.sdk.transaction.TransactionReceipt;
import io.github.manishdait.sdk.transaction.TransactionResponse;

public class AccountInfoTx {
  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.load();

    Client client = Client.forTestnet();
    client.setOperatorAccount(
      AccountId.fromString(dotenv.get("OPERATOR_ACCOUNT_ID")),
      PrivateKey.fromString(dotenv.get("OPERATOR_PRIVATE_KEY"))
    );

    PrivateKey privateKey = PrivateKey.generate();
    TransactionResponse transactionResponse = new AccountCreateTransaction()
      .withKey(privateKey)
      .withInitialBalance(1)
      .withAccountMemo("Test SDK Delete Account")
      .pack(client)
      .send();

    TransactionReceipt receipt = transactionResponse.queryReceipt();

    if (receipt.status() != Status.SUCCESS) {
      throw new RuntimeException("Unable to create an account");
    }

    AccountId accountId = receipt.accountId();
    System.out.println("Account Create with ID: " + receipt.accountId());

    AccountInfo accountInfo = new AccountInfoQuery()
      .withAccountId(accountId)
      .query(client);

    System.out.println(accountInfo);
  }
}
