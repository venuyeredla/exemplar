package com.exemplar.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AppController {
    @RequestMapping
    public String getAppInfo(){
    return "Exemplar Application";
    }
    @RequestMapping("appName")
    public String getAppName(){
        return "Exemplar Application";
    }

}
