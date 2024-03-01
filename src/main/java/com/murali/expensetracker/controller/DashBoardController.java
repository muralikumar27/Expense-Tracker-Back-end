package com.murali.expensetracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Added this controller to test the working of JWT
 * validation and authorization flow. This endpoint will be
 * modified for handling the real dashboard request*/
@RestController
public class DashBoardController {

    @GetMapping("/dummy")
    public String dummy(){
        return "dummy dashboard controller after login";
    }

    @GetMapping("/expenses")
    public String expense(){
        return "dummy expense controller after login";
    }

}
