package com.planb;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.planb.security.jwt.JwtTokenUtil;


@RestController
@EnableAutoConfiguration
@ComponentScan({"com.planb"})
public class RunApplication {
	
	@RequestMapping("/home")
    String home() {
        return "Hello 333World!--home011:";
    }

    @RequestMapping("/")
    String index() {
    	
    	Map<String, Object> claimMap = new HashMap<String, Object>();
    	claimMap.put("userId", "123");
    	String token = JwtTokenUtil.createToken(claimMap);
    	
    	JwtTokenUtil.getUserNameFromToken(token);
    	
    	
        return "Hello 333World!--008:" + token;
    }

    public static void main(String[] args) {

        SpringApplication.run(RunApplication.class, args);
    }

}