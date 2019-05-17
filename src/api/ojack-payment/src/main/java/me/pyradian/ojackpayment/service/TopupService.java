package me.pyradian.ojackpayment.service;

import io.jsonwebtoken.Claims;
import me.pyradian.ojackpayment.model.Topup;
import me.pyradian.ojackpayment.repository.TopupRepository;
import me.pyradian.ojackpayment.repository.WalletRepository;

public interface TopupService {
     WalletRepository getWalletRepository();
     TopupRepository getTopupRepository();

     Claims getClaims(String token);

     int isValid(Topup t);
     boolean isPending(Topup t);
}
