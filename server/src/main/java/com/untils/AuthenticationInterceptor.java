package com.untils;

import com.enums.StateCode;
import com.alibaba.fastjson.JSONObject;
import com.annotation.PassToken;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        String uuid = httpServletRequest.getHeader("ROBOTCODE");
//        System.out.println("token"+token);
//        System.out.println("ROBOTCODE"+uuid);

        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            return passToken.required();
        }else {
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json; charset=utf-8");
            JSONObject json = new JSONObject();

            if (token == null) {
                json.put("code", StateCode.NO_TOKEN.getValue());
                httpServletResponse.getWriter().append(json.toJSONString());
                return false;
            }
            //验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(uuid)).withIssuer("auth").build();
            try {
                jwtVerifier.verify(token);
                return true;
            } catch (JWTVerificationException e) {
                json.put("code",StateCode.TOKEN_ERR.getValue());
                httpServletResponse.getWriter().append(json.toJSONString());
        }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}
