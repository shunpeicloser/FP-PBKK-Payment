package me.pyradian.ojackpayment.service;

import io.jsonwebtoken.Claims;
import me.pyradian.ojackpayment.model.Wallet;
import me.pyradian.ojackpayment.repository.WalletRepository;

public interface WalletService {
    int isValidWallet(Wallet w);
    WalletRepository repository();
    Claims getClaims(String token);
}
