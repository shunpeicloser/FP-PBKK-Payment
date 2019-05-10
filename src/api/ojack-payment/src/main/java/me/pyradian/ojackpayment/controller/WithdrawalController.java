package me.pyradian.ojackpayment.controller;

import io.jsonwebtoken.Claims;
import me.pyradian.ojackpayment.aop.TokenAuth;
import me.pyradian.ojackpayment.exception.BadRequestException;
import me.pyradian.ojackpayment.exception.NotFoundException;
import me.pyradian.ojackpayment.model.Wallet;
import me.pyradian.ojackpayment.model.Withdrawal;
import me.pyradian.ojackpayment.repository.WalletRepository;
import me.pyradian.ojackpayment.repository.WithdrawalRepository;
import me.pyradian.ojackpayment.service.WithdrawalService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(WithdrawalController.BASE_URL)
public class WithdrawalController {
    public static final String BASE_URL = "/api/v1/transaction/withdrawal";

    private WithdrawalService withdrawalService;

    public WithdrawalController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    @TokenAuth(strict = false)
    @GetMapping
    public List<Withdrawal> getAllWithdrawal(@RequestHeader("Authorization") String token) {
        Claims claims = withdrawalService.getClaims(token);
        if (claims.get("rol").equals("ADMIN"))
            return withdrawalService.getWithdrawalRepository().findAll();
        return withdrawalService.getWithdrawalRepository().findByWalletNumber(claims.getSubject());
    }

    @TokenAuth(auth_role = "USER", account_type = "driver,restaurant")
    @PostMapping
    public ResponseEntity<Withdrawal> requestWithdrawal(@RequestBody Withdrawal wd,
                                                        @RequestHeader("Authorization") String token) {
        Claims claims = withdrawalService.getClaims(token);
        wd.setWalletNumber(claims.getSubject());
        this.withdrawalService.isValid(wd);

        Wallet w = withdrawalService.getWalletRepository().findByWalletNumber(wd.getWalletNumber());
        w.setBalance(w.getBalance()-wd.getAmount());

        withdrawalService.getWithdrawalRepository().insert(wd);
        withdrawalService.getWalletRepository().save(w);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Location", WithdrawalController.BASE_URL + "/" + wd.getTransactionId());
        return new ResponseEntity<>(wd, headers, HttpStatus.CREATED);
    }

    @TokenAuth
    @GetMapping("/{withdrawalId}")
    public Withdrawal getWithdrawalDetail(@PathVariable("withdrawalId") String withdrawalId) {
        Withdrawal wd = withdrawalService.getWithdrawalRepository().findByTransactionId(withdrawalId);

        if (wd == null)
            throw new NotFoundException("Withdrawal with ID " + withdrawalId + " is non-existent");

        return wd;
    }

    @TokenAuth
    @PatchMapping("/confirm/{withdrawalId}")
    public Withdrawal confirmWithdrawal(@PathVariable("withdrawalId") String withdrawalId) {
        Withdrawal wd = withdrawalService.getWithdrawalRepository().findByTransactionId(withdrawalId);

        if (wd == null)
            throw new NotFoundException("Withdrawal with ID " + withdrawalId + " is non-existent");

        if (!this.withdrawalService.isPending(wd))
            throw new BadRequestException("Withdrawal with ID " + withdrawalId + " is already confirmed/canceled");

        Wallet w = withdrawalService.getWalletRepository().findByWalletNumber(wd.getWalletNumber());
        w.setBalance(w.getBalance()-wd.getAmount());
        wd.setStatus("confirmed");

        withdrawalService.getWalletRepository().save(w);
        withdrawalService.getWithdrawalRepository().save(wd);

        return wd;
    }

    @TokenAuth
    @PatchMapping("/cancel/{withdrawalId}")
    public Withdrawal canceledWithdrawal(@PathVariable("withdrawalId") String withdrawalId) {
        Withdrawal wd = withdrawalService.getWithdrawalRepository().findByTransactionId(withdrawalId);

        if (wd == null)
            throw new NotFoundException("Withdrawal with ID " + withdrawalId + " is non-existent");

        if (!this.withdrawalService.isPending(wd))
            throw new BadRequestException("Withdrawal with ID " + withdrawalId + " is already confirmed/canceled");

        Wallet w = withdrawalService.getWalletRepository().findByWalletNumber(wd.getWalletNumber());
        w.setBalance(w.getBalance() + wd.getAmount()); // refund wallet balance
        wd.setStatus("canceled");

        withdrawalService.getWithdrawalRepository().save(wd);
        withdrawalService.getWalletRepository().save(w);

        return wd;
    }
}
