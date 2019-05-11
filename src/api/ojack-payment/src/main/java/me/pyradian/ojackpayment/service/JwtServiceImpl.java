package me.pyradian.ojackpayment.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService{
    public static final String secretKey = "EmYTCuhxT3$FpXUVXDj*f0e4LbcpDAk^apes1eAuUtUrC3V%1XPSGD^2KPj*u^L&";


    @Override
    public String createToken(String subject, long ttlMillis) {
        if (ttlMillis <= 0) {
            throw new RuntimeException("Exp time must be greater than 0 :["+ttlMillis+"] ");
        }

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .signWith(signatureAlgorithm, signingKey);

        builder.setExpiration(new Date(nowMillis + ttlMillis));

        return builder.compact();
    }

    @Override
    public String getSubject(String authToken) {
        String token = authToken.split(" ")[1];
        System.out.println(token);
        Claims claims = Jwts.parser()
                        .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                        .parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    @Override
    public Claims getBody(String authToken) {
        String token = authToken.split(" ")[1];
//        System.out.println(token);
        Claims claims;
//        try {
            claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(token).getBody();
//        } catch (Exception e) {
//            throw new UnauthorizedException("Invalid Token");
//        }

        return claims;
    }
}
