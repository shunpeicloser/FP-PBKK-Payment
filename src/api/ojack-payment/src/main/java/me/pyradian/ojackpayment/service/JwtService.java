package me.pyradian.ojackpayment.service;

import io.jsonwebtoken.Claims;

public interface JwtService {
    String createToken(String subject, long ttlMillis);

    String getSubject(String authToken);

    Claims getBody(String authToken);
}
