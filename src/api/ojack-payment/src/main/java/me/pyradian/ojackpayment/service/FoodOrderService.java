package me.pyradian.ojackpayment.service;

import io.jsonwebtoken.Claims;
import me.pyradian.ojackpayment.model.FoodOrder;
import me.pyradian.ojackpayment.repository.FoodOrderRepository;
import me.pyradian.ojackpayment.repository.WalletRepository;

public interface FoodOrderService {
    WalletRepository getWalletRepository();
    FoodOrderRepository getFoodOrderRepository();

    Claims getClaims(String token);

    boolean canCheckOrder(FoodOrder fo, String walletNumber);

    int isValid(FoodOrder fo);
    boolean isPending(FoodOrder fo);
}
