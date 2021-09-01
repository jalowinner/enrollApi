package com.dingren.enrolldemo.JwtConfig;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import com.dingren.enrolldemo.MyUserDetails;
import com.dingren.enrolldemo.MyUserDetailsService;

import io.jsonwebtoken.*;
@Configuration
public class JwtTokenProvider implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8609009046825136528L;
	
	private String secretKey = "demo";
	
	private long validDuration = 1000 * 60 * 60;
	@PostConstruct
	void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	public String createToken(String username, String role) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("auth", role);
		Date now = new Date();
		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(new Date(now.getTime()+validDuration)).signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	public Authentication getAuthentication(String username) {
		UserDetails details = myUserDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(details.getUsername(), details.getPassword(),details.getAuthorities());
	}
	
	public Claims getClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}
	
}
