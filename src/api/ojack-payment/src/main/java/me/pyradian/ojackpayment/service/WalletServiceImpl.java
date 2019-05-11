package me.pyradian.ojackpayment.service;

import io.jsonwebtoken.Claims;
import me.pyradian.ojackpayment.exception.BadRequestException;
import me.pyradian.ojackpayment.exception.ConflictException;
import me.pyradian.ojackpayment.model.Wallet;
import me.pyradian.ojackpayment.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {

    private WalletRepository walletRepository;
    private JwtService jwtService;

    public WalletServiceImpl(WalletRepository walletRepository, JwtService jwtService) {
        this.walletRepository = walletRepository;
        this.jwtService = jwtService;
    }

    public int isValidWallet(Wallet w) {

        // incomplete field
        if (w.getWalletNumber() == null || w.getType() == null || !validType(w.getType()))
            throw new BadRequestException("Some or all fields are empty");

        // wallet number already exists
        if (this.walletRepository.findByWalletNumber(w.getWalletNumber()) != null)
            throw new ConflictException("Wallet number is already registered");

        // invalid type
        if (!validType(w.getType()))
            throw new BadRequestException("Invalid wallet type");

        return 0;
    }

    public WalletRepository repository() {
        return this.walletRepository;
    }

    public Claims getClaims(String token) {
        return jwtService.getBody(token);
    }

    private boolean validType(String type) {
        return type.equals("driver") || type.equals("customer") || type.equals("restaurant");
    }
}
