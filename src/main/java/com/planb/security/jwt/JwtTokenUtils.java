package com.planb.security.jwt;

import java.text.ParseException;
import java.time.Instant;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.planb.common.conf.DateConfig;

/**
 * Header.Payload.Signature 不要在JWT的payload或header中放置敏感信息，除非它们是加密的。
 * Base64({'alg': "HS256",'typ': "JWT"}) Base64({exp})
 * header >>> Authorization: Bearerheader >>> Authorization: Bearer
 * 如果token是在授权头（Authorization header）中发送的，那么跨源资源共享(CORS)将不会成为问题，因为它不使用cookie
 * @author mark
 *
 */
@Component
public class JwtTokenUtils {
	private static long TOKEN_EXPIRES_SECORDS = 60 * 5;
    private static String TOKEN_SIGN_KEY = "XY0123456789abcdefghij0123456789";
    // private static String USER_ROLE_KEY = "userRole";
    
    public static String createToken(Map<String, Object> claimMap) {
    	Jwt jwt = Jwt.withTokenValue("token").header("typ", "JWT")
    			.claims(map -> {
    				map.putAll(claimMap);
    			})
    			.expiresAt(Instant.now().plusSeconds(TOKEN_EXPIRES_SECORDS))
    			.build();
    	String token;
    	try {
        	String payLoadJson = DateConfig.instantObjectMapper().writeValueAsString(jwt.getClaims());
    		JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(payLoadJson));
    		jwsObject.sign(new MACSigner(TOKEN_SIGN_KEY.getBytes()));
            token =  jwsObject.serialize();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    	
        return token;
    }
    
    public static JWTClaimsSet getClaimsFromToken(String token) throws ParseException {
		SignedJWT sjwt = SignedJWT.parse(token);
		return sjwt.getJWTClaimsSet();
    }
    
    public static boolean validateToken(String token, UserDetails userDetails) {
    	try {
    		JWSVerifier jwsVerifier = new MACVerifier(TOKEN_SIGN_KEY.getBytes());
    		SignedJWT sjwt = SignedJWT.parse(token);
    		boolean verify = sjwt.verify(jwsVerifier);
    		if (verify == false) {
    			return false;
    		}
		} catch (Exception e) {
			return false;
		}
    	return true;
    }
}
