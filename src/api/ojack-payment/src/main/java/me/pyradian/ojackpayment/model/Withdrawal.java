package me.pyradian.ojackpayment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

public class Withdrawal extends Transaction{
    @Field("withdrawal_wallet_number")
    private String walletNumber;
    @Field("withdrawal_amount")
    private int amount;
    @Field("withdrawal_bank_name")
    private String bankName;
    @Field("withdrawal_bank_account")
    private String bankAccount;

    @JsonCreator
    public Withdrawal(@JsonProperty("amount") int amount,
                      @JsonProperty("bank_name") String bankName,
                      @JsonProperty("bank_account") String bankAccount) {
        // generate withdrawal transaction id
        String hash = DigestUtils.sha1Hex(new Date() + walletNumber + amount + bankAccount + bankName);

        this.transactionId = "OJAP-WDR-" + hash;
        this.amount = amount;
        this.status = "pending";
        this.bankName = bankName;
        this.bankAccount = bankAccount;

        this.cashflow = "debit";
        this.transactionType = "WITHDRAWAL";
    }

    @JsonProperty("wallet_number")
    public String getWalletNumber() {
        return walletNumber;
    }

    public void setWalletNumber(String walletNumber) {
        this.walletNumber = walletNumber;
    }

    public int getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    @JsonProperty("bank_name")
    public String getBankName() {
        return bankName;
    }

    @JsonProperty("bank_account")
    public String getBankAccount() {
        return bankAccount;
    }
}
