package me.pyradian.ojackpayment.controller;

import com.mongodb.lang.Nullable;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.jsonwebtoken.Claims;
import me.pyradian.ojackpayment.aop.TokenAuth;
import me.pyradian.ojackpayment.exception.BadRequestException;
import me.pyradian.ojackpayment.exception.ConflictException;
import me.pyradian.ojackpayment.exception.NotFoundException;
import me.pyradian.ojackpayment.model.QWallet;
import me.pyradian.ojackpayment.model.Wallet;
import me.pyradian.ojackpayment.repository.WalletRepository;
import me.pyradian.ojackpayment.service.WalletService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(WalletController.BASE_URL)
public class WalletController {
    public static final String BASE_URL = "/api/v1/wallet";

    private WalletService walletService;

    public WalletController(WalletRepository walletRepository, WalletService walletService) {
        this.walletService = walletService;
    }

    @TokenAuth(strict = false)
    @GetMapping
    public Object getWallet(@Nullable @RequestParam("type") String type,
                                     @Nullable @RequestParam("balance_range") String balanceRange,
                                     @RequestHeader("Authorization") String token) {
        Claims claims = walletService.getClaims(token);

        // for admin
        if (claims.get("rol").equals("ADMIN")) {
            QWallet wallet = new QWallet("wallet");
            BooleanBuilder builder = new BooleanBuilder();
            if (type != null)
                builder.and(wallet.type.eq(type));
            if (balanceRange != null) {
                int low, high;
                String[] val = balanceRange.split(",");
                System.out.println(val[0] + " " + val[1]);
                val[0] = (val[0].equals("")) ? "0" : val[0];
                val[1] = (val[0].equals("")) ? "9999999999" : val[1];
                low = Integer.parseInt(val[0]);
                high = Integer.parseInt(val[1]);
                builder.and(wallet.balance.between(low, high));
            }

            return (builder.getValue() == null ? walletService.repository().findAll() :
                    (List<Wallet>) walletService.repository().findAll(builder.getValue()));
        }

        Wallet w = walletService.repository().findByWalletNumber(claims.getSubject());
        return w;
    }

    @TokenAuth
    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody Wallet w) {
        int res = this.walletService.isValidWallet(w);

        if (res == -1) // incomplete field
            throw new BadRequestException("Some or all fields are empty");
        else if (res == -2) // wallet number already exists
            throw new ConflictException("Wallet number is already registered");
        else if (res == -3) // invalid wallet type
            throw new BadRequestException("Invalid wallet type");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Location", WalletController.BASE_URL + "/" + w.getWalletNumber());
        walletService.repository().insert(w);
        return new ResponseEntity<>(w, headers, HttpStatus.CREATED);
    }

    @TokenAuth
    @GetMapping("/{walletNumber}")
    public Wallet getWalletDetail(@PathVariable("walletNumber") String walletNumber) {
        Wallet w = walletService.repository().findByWalletNumber(walletNumber);

        if (w == null)
            throw new NotFoundException("Wallet number " + walletNumber + " is non-existent");

        return w;
    }

}
