package me.pyradian.ojackpayment.controller;

import io.jsonwebtoken.Claims;
import me.pyradian.ojackpayment.aop.TokenAuth;
import me.pyradian.ojackpayment.exception.BadRequestException;
import me.pyradian.ojackpayment.exception.NotFoundException;
import me.pyradian.ojackpayment.exception.UnauthorizedException;
import me.pyradian.ojackpayment.model.FoodOrder;
import me.pyradian.ojackpayment.model.Wallet;
import me.pyradian.ojackpayment.service.FoodOrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(FoodOrderController.BASE_URL)
public class FoodOrderController {
    public static final String BASE_URL = "/api/v1/transaction/foodorder";

    private FoodOrderService foodOrderService;

    public FoodOrderController(FoodOrderService foodOrderService) {
        this.foodOrderService = foodOrderService;
    }

    @TokenAuth(strict = false)
    @GetMapping
    public List<FoodOrder> getAllFoodOrder(@RequestHeader("Authorization") String token) {
        Claims claims = foodOrderService.getClaims(token);

        if (claims.get("rol").equals("ADMIN"))
            return foodOrderService.getFoodOrderRepository().findAll();

        return foodOrderService.getFoodOrderRepository().findByCustomerWalletNumber(claims.getSubject());
    }

    @TokenAuth
    @PostMapping
    public ResponseEntity<FoodOrder> requestFoodOrder(@RequestBody FoodOrder fo) {
        this.foodOrderService.isValid(fo);


        HttpHeaders headers = new HttpHeaders();
        headers.set("Location", FoodOrderController.BASE_URL + "/" + fo.getTransactionId());
        Wallet customerWallet = foodOrderService.getWalletRepository().findByWalletNumber(fo.getCustomerWalletNumber());
        customerWallet.setBalance(customerWallet.getBalance() - fo.getFoodOrderBill());
        foodOrderService.getFoodOrderRepository().insert(fo);
        foodOrderService.getWalletRepository().save(customerWallet);

        return new ResponseEntity<>(fo, headers, HttpStatus.CREATED);
    }

    @TokenAuth(strict = false)
    @GetMapping("/{foodOrderId}")
    public FoodOrder getFoodOrder(@PathVariable("foodOrderId") String foodOrderId,
                                  @RequestHeader("Authorization") String token) {
        Claims claims = foodOrderService.getClaims(token);

        FoodOrder fo = foodOrderService.getFoodOrderRepository().findByTransactionId(foodOrderId);
        if (fo == null)
            throw new NotFoundException("Food Order with ID " + foodOrderId + " is non-existent");

        // admin can see all trx
        if (claims.get("rol").equals("ADMIN")) {
            return fo;
        }

        if (!foodOrderService.canCheckOrder(fo, claims.getSubject()))
            throw new UnauthorizedException("You cannot view this transaction");

        return fo;
    }

    @TokenAuth(auth_role = "USER", account_type = "driver")
    @PatchMapping("/confirm/{foodOrderId}")
    public FoodOrder confirmFoodOrder(@PathVariable("foodOrderId") String foodOrderId,
                                      @RequestHeader("Authorization") String token) {
        Claims claims = foodOrderService.getClaims(token);
        FoodOrder fo = foodOrderService.getFoodOrderRepository().findByTransactionId(foodOrderId);

        // non-existent order
        if (fo == null)
            throw new NotFoundException("Food Order with ID " + foodOrderId + " is non-existent");

        // attempt to confirm a canceled or confirmed order
        if (!this.foodOrderService.isPending(fo))
            throw new BadRequestException("Food Order with ID " + foodOrderId + " is already confirmed/canceled");

        // only driver in foodorder can confirm
        if (!foodOrderService.canCheckOrder(fo, claims.getSubject()))
            throw new UnauthorizedException("The driver is not in charge delivering the food, thus cannot confirm this order");

        // fetch driver and restaurant wallet, then add fund
        Wallet driverWallet = foodOrderService.getWalletRepository().findByWalletNumber(fo.getDriverWalletNumber());
        Wallet restaurantWallet = foodOrderService.getWalletRepository().findByWalletNumber(fo.getRestaurantWalletNumber());
        driverWallet.setBalance(driverWallet.getBalance() + fo.getDriverAmount());
        restaurantWallet.setBalance(restaurantWallet.getBalance() + fo.getRestaurantAmount());
        foodOrderService.getWalletRepository().save(driverWallet);
        foodOrderService.getWalletRepository().save(restaurantWallet);

        fo.setStatus("confirmed");
        foodOrderService.getFoodOrderRepository().save(fo);

        return fo;
    }

    @TokenAuth(auth_role = "USER", account_type = "customer,restaurant")
    @PatchMapping("/cancel/{foodOrderId}")
    public FoodOrder cancelFoodOrder(@PathVariable("foodOrderId") String foodOrderId,
                                     @RequestHeader("Authorization") String token) {
        Claims claims = foodOrderService.getClaims(token);
        FoodOrder fo = foodOrderService.getFoodOrderRepository().findByTransactionId(foodOrderId);

        // non-existent order
        if (fo == null)
            throw new NotFoundException("Food Order with ID " + foodOrderId + " is non-existent");

        // attempt to cancel a canceled or confirmed order
        if (!this.foodOrderService.isPending(fo))
            throw new BadRequestException("Food Order with ID " + foodOrderId + " is already confirmed/cancel");

        // only customer or restaurnat in foodorder can cancel
        if (!foodOrderService.canCheckOrder(fo, claims.getSubject()))
            throw new UnauthorizedException("Current wallet number is not in this order, thus cannot cancel the order");

        // fetch customer wallet and then refund
        Wallet customerWallet = foodOrderService.getWalletRepository().findByWalletNumber(fo.getCustomerWalletNumber());
        customerWallet.setBalance(customerWallet.getBalance() + fo.getFoodOrderBill());
        foodOrderService.getWalletRepository().save(customerWallet);

        fo.setStatus("canceled");
        foodOrderService.getFoodOrderRepository().save(fo);

        return fo;
    }
}
