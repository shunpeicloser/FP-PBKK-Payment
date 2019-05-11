package me.pyradian.ojackpayment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping
public class TestController {
    // this controller is test purpose only

    @ResponseBody
    @RequestMapping
    public Map<String, Object> index() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status_code", 200);
        map.put("message", "$$$ money money money dollar dollar dollar");
        map.put("ps", "this is ojack payment service. greetings.");
        map.put("manual", "make sure to read the manual @ https://github.com/shunpeicloser/FP-PBKK-Payment");

        return map;
    }
}
