package me.pyradian.ojackpayment;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

//@EntityScan(
//        basePackageClasses = {Application.class, Jsr310Converters.class}
//)
@SpringBootApplication
@EnableMongoAuditing
public class OjackPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjackPaymentApplication.class, args);
    }

}
