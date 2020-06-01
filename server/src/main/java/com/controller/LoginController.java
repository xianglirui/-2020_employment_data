package com.controller;

import com.annotation.PassToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

@RestController
public class LoginController {
    @Autowired
    private TokenService tokenService;

    /**
     * 返回登录页面随机值
     * @return
     */
    @PassToken
    @GetMapping("/getRandom")
    public String getRandom(){
        int r = new Random().nextInt(80) + 20;
        return r+"";
    }

    @PassToken
    @GetMapping("/getUserInfo")
    public String getUserInfo(@RequestParam String code) throws JsonProcessingException {
        System.out.println("uuid:"+code);
        return new ObjectMapper().writeValueAsString(tokenService.getToken(code));
    }

}
