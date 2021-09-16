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


@RestController
@EnableAutoConfiguration
@ComponentScan({"com.planb"})
public class RunApplication {
	
	private static final String ISS_CLAIM = "iss";

	private static final String SUB_CLAIM = "sub";

	private static final String AUD_CLAIM = "aud";

	private static final String EXP_CLAIM = "exp";

	private static final String NBF_CLAIM = "nbf";

	private static final String IAT_CLAIM = "iat";

	private static final String JTI_CLAIM = "jti";

	private static final String ISS_VALUE = "https://provider.com";

	private static final String SUB_VALUE = "subject1";

	private static final List<String> AUD_VALUE = Arrays.asList("aud1", "aud2");

	private static final long EXP_VALUE = Instant.now().plusSeconds(60).toEpochMilli();

	private static final long NBF_VALUE = Instant.now().plusSeconds(5).toEpochMilli();

	private static final long IAT_VALUE = Instant.now().toEpochMilli();

	private static final String JTI_VALUE = "jwt-id-1";

	private static final Map<String, Object> HEADERS;

	private static final Map<String, Object> CLAIMS;
	
	private static final String JWT_TOKEN_VALUE = "jwt-token-value";
	static {
		HEADERS = new HashMap<>();
		HEADERS.put("alg", JwsAlgorithms.RS256);
		CLAIMS = new HashMap<>();
		CLAIMS.put(ISS_CLAIM, ISS_VALUE);
		CLAIMS.put(SUB_CLAIM, SUB_VALUE);
		CLAIMS.put(AUD_CLAIM, AUD_VALUE);
		CLAIMS.put(EXP_CLAIM, EXP_VALUE);
		CLAIMS.put(NBF_CLAIM, NBF_VALUE);
		CLAIMS.put(IAT_CLAIM, IAT_VALUE);
		CLAIMS.put(JTI_CLAIM, JTI_VALUE);
	}
	

    @RequestMapping("/")
    String home() throws Exception {
    	Jwt j = new Jwt(JWT_TOKEN_VALUE, Instant.ofEpochMilli(IAT_VALUE), Instant.ofEpochMilli(EXP_VALUE), HEADERS, CLAIMS);
    	System.out.println("cccccccc:");
    	
    	JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256),
                new Payload("{\"a\":123}"));
		
		//We need a 256-bit key for HS256 which must be pre-shared
		byte[] sharedKey = new byte[32];
		new SecureRandom().nextBytes(sharedKey);
		//Apply the HMAC to the JWS object
		jwsObject.sign(new MACSigner("xxxxxxxxxxx-00001-1102xxxxxxxxxxx-00001-110221".getBytes()));
		
		//Output in URL-safe format
		System.out.println(jwsObject.serialize());
        String s =  jwsObject.serialize();
		
		JWSVerifier jwsVerifier = new MACVerifier("xxxxxxxxxxx-00001-1102xxxxxxxxxxx-00001-1102".getBytes());
		
		SignedJWT sjwt = SignedJWT.parse(s);
		boolean b = sjwt.verify(jwsVerifier);
		
		
		System.out.println("sjwt.......111:" + b);
		System.out.println("sjwt.......222:" + sjwt.getJWTClaimsSet());
		
		

    	Jwt jwt = Jwt.withTokenValue("eyJhbGciOiJIUzI1NiJ9.eyJhIjoxMjN9.YTPU6U18N55i31wwKAOC4_68gVopwxI9M0xxckwXV34")
    		    .header("alg", "none")
    		    .claim("sub", "user")
    		    .claim("scope", "read").build();
    	
    	
    	
    	System.out.println("xxxxxxxx:" + jwt.getSubject());
    	
    	
    	
    	
        return "Hello 333World!--006";
    }

    public static void main(String[] args) {

        SpringApplication.run(RunApplication.class, args);
    }

}