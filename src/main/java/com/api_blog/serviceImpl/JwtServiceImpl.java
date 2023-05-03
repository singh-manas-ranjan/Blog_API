package com.api_blog.serviceImpl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.api_blog.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtServiceImpl implements JwtService{
	
	@Override
	public String generateToken(String username) {
		Map<String,Object> claims = new HashMap<>();
		return createToken(username,claims);
	}

	private String createToken(String username,Map<String,Object> claims) {
		
		return Jwts.builder()
			.setClaims(claims)
			.setSubject(username)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis()+60*60*1000))
			.signWith(getSignKey(),SignatureAlgorithm.HS512)
			.compact();

	}
	
	private final String SECRET = "7234753778214125442A472D4B6150645267556B58703273357638792F423F4528482B4D6251655468566D597133743677397A24432646294A404E635266556A";
	
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64URL.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	private Claims extractAllClaims(String token) {
		try {
		return Jwts.parserBuilder()
			.setSigningKey(getSignKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			throw new BadCredentialsException("",ex);
		}catch(ExpiredJwtException ex) {
			throw ex;
		}
	}
	
	private <T>T extractClaims(String token, Function<Claims, T> claimsResolver){
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	@Override
	public String extractUsername(String token) {
		return extractClaims(token, Claims::getSubject);
	}
	
	@Override
	public Date extractExpiration(String token) {
		return extractClaims(token, Claims::getExpiration);
	}
	
	@Override
	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	@Override
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && ! isTokenExpired(token));
	}
}
