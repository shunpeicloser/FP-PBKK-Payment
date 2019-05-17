package me.pyradian.ojackpayment.service;

import io.jsonwebtoken.Claims;

public interface JwtService {
    Claims getBody(String authToken);
}
