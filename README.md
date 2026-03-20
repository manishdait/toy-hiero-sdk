# hiero-lib
A Implementation of Hiero Sdk implemented in Java, created to understand how the real SDK works under the hood. It is intentionally simplified and intended purely for learning and experimental purposes only.

## Basic Usage
```java
import io.github.manishdait.sdk.Client;
import io.github.manishdait.sdk.account.AccountId;
import io.github.manishdait.sdk.key.PrivateKey;
import io.github.manishdait.sdk.transaction.TransactionReceipt;
import io.github.manishdait.sdk.transaction.TransactionResponse;

public class AccountCreateTx {
  public static void main(String[] args) {
    Client client = Client.forTestnet();
    client.setOperatorAccount(
      AccountId.fromString(OPERATOR_ACCOUNT_ID),
      PrivateKey.fromString(OPERATOR_PRIVATE_KEY)
    );

    TransactionResponse transactionResponse = new AccountCreateTransaction()
      .withKey(PrivateKey.generate())
      .withInitialBalance(1)
      .withAccountMemo("Test SDK")
      .pack(client)
      .send();

    TransactionReceipt receipt = transactionResponse.queryReceipt();
    System.out.println(receipt);
  }
}
```
