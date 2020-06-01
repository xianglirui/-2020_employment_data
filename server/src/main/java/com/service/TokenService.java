package com.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class TokenService {
    public String getToken(String uid) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60* 60 * 1000;//一小时有效时间
        Date end = new Date(currentTime);
        String token;

        token = JWT.create().withIssuer("auth").withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(uid));
        return token;
    }
}
