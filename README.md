# toy-hiero-sdk
A Implementation of Hiero Sdk implemented in Java, created to understand how the real SDK works under the hood. It is intentionally simplified and intended purely for learning and experimental purposes only.

## Basic Usage
```java
import org.example.sdk.Client;
import org.example.sdk.account.AccountId;
import org.example.sdk.key.PrivateKey;
import org.example.sdk.transaction.TransactionReceipt;
import org.example.sdk.transaction.TransactionResponse;

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