package me.pyradian.ojackpayment.aop;

import io.jsonwebtoken.Claims;
import me.pyradian.ojackpayment.exception.UnauthorizedException;
import me.pyradian.ojackpayment.service.JwtService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class TokenAuthAspect {

    private JwtService jwtService;

    public TokenAuthAspect(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Before("@annotation(tokenAuth)")
    public void tokenAuth(TokenAuth tokenAuth) throws Throwable {
        ServletRequestAttributes requestAttributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String authHeader = request.getHeader("Authorization");
        System.out.println(authHeader);
        Claims claims = jwtService.getBody(authHeader);

        // if not strict, ADMIN and USER can have the access
        if (tokenAuth.strict() == false) {
            return;
        }

        // get token auth_role
        if (!tokenAuth.auth_role().equals(claims.get("rol"))) {
            throw new UnauthorizedException("Unauthorized bro!");
        }

        // get token account_type if not admin
        if ( tokenAuth.auth_role().equals("USER") && !tokenAuth.account_type().equals(claims.get("atp"))) {
            throw new UnauthorizedException("Unauthorized bro!");
        }
    }
}
