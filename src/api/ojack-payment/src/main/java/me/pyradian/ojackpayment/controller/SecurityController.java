package me.pyradian.ojackpayment.controller;

import io.jsonwebtoken.Claims;
import me.pyradian.ojackpayment.aop.TokenAuth;
import me.pyradian.ojackpayment.service.JwtService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security")
public class SecurityController {
    private JwtService jwtService;

    public SecurityController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping
    public String getSubject(@RequestHeader(name = "Authorization") String authToken) {
        return this.jwtService.getSubject(authToken);
    }

    @TokenAuth(auth_role = "ADMIN")
    @GetMapping("/generate")
    public String createToken(@RequestParam("subject") String subject) {
        String token = this.jwtService.createToken(subject, 100*60*60);

        return token;
    }

    @GetMapping("/getbody")
    public Claims getBody(@RequestHeader(name = "Authorization") String authToken) {
        return this.jwtService.getBody(authToken);
    }
}
