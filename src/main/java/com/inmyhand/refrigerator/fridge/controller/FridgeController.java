package com.inmyhand.refrigerator.fridge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


//TODO
@Controller
public class FridgeController {

    @GetMapping("/fridge")
    public String fridge() {
        return "/app/fridge/fridge_main";
    }


}
