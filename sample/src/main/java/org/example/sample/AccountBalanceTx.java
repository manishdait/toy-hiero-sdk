package org.example.sample;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.sdk.Client;
import org.example.sdk.Status;
import org.example.sdk.account.AccountBalance;
import org.example.sdk.account.AccountCreateTransaction;
import org.example.sdk.account.AccountId;
import org.example.sdk.account.AccountInfo;
import org.example.sdk.key.PrivateKey;
import org.example.sdk.query.AccountBalanceQuery;
import org.example.sdk.query.AccountInfoQuery;
import org.example.sdk.transaction.TransactionReceipt;
import org.example.sdk.transaction.TransactionResponse;

public class AccountBalanceTx {
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

    AccountBalance accountBalance = new AccountBalanceQuery()
      .withAccountId(accountId)
      .query(client);

    System.out.println(accountBalance);
  }
}
