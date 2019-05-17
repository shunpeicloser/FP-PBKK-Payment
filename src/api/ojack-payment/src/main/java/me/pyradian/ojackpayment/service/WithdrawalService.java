package me.pyradian.ojackpayment.service;

import io.jsonwebtoken.Claims;
import me.pyradian.ojackpayment.model.Withdrawal;
import me.pyradian.ojackpayment.repository.WalletRepository;
import me.pyradian.ojackpayment.repository.WithdrawalRepository;

public interface WithdrawalService {
    WalletRepository getWalletRepository();
    WithdrawalRepository getWithdrawalRepository();

    Claims getClaims(String token);

    int isValid(Withdrawal wd);
    boolean isPending(Withdrawal wd);
}
