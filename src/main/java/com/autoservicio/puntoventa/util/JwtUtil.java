package com.autoservicio.puntoventa.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

@Service
public class JwtUtil {
	
	private SecretKey SECRETKEY=MacProvider.generateKey();
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public <T>T extractClaim(String token,Function<Claims,T> claimsResolver){
		final Claims claims=extractAllClaims(token);
		
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody();
	}
	
	private Boolean isTokenExpired(String token) {
		
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
		Map<String,Object> claims=new HashMap<>();
		return  createToken(claims,userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String username) {
		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+100*60*120*10))
				.signWith(SignatureAlgorithm.HS512, SECRETKEY).compact();
	}
	
	public Boolean validateToken(String token,UserDetails userDetails) {
		final String username=extractUsername(token);
		if(userDetails!=null) {
			return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		}
		else {
			return false;
		}
	}
}
