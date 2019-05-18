package me.pyradian.ojackpayment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryEntity;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@QueryEntity
@Document(collection = "wallets")
public class Wallet extends BaseEntity{
    @Indexed(direction = IndexDirection.ASCENDING)
    @Field("wallet_number")
    private String walletNumber;
    @Field("wallet_balance")
    private int balance;
    @Field("wallet_type")
    private String type; // DRIVER, CUSTOMER, RESTAURANT

//    public Wallet() {
//
//    }

    @JsonCreator
    public Wallet(@JsonProperty("wallet_number") String walletNumber,
                  @JsonProperty("type") String type) {
        this.walletNumber = walletNumber;
        this.type = type;
    }


    @JsonProperty("wallet_number")
    public String getWalletNumber() {
        return walletNumber;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

}
