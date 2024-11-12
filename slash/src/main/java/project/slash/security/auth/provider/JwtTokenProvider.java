package project.slash.security.auth.provider;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import project.slash.security.auth.dto.JwtTokenDto;
import project.slash.security.auth.custom.CustomUserDetails;

@Slf4j
@Component
public class JwtTokenProvider {

	private final Key key;

	public JwtTokenProvider(@Value("${jwt.secret}") String secretkey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretkey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public JwtTokenDto generateToken(Authentication authentication) {

		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String userId = userDetails.getUsername();
		String role = userDetails.getRole();

		long now = (new Date()).getTime();

		Date accessTokenExpiresIn = new Date(now + 86400000);
		Date issuedAt = new Date(now);
		String accessToken = Jwts.builder()
			.claim("id", userId)
			.claim("auth", role)
			.setIssuedAt(issuedAt)
			.setExpiration(accessTokenExpiresIn)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();

		return JwtTokenDto.builder()
			.grantType("Bearer")
			.accessToken(accessToken)
			.build();

	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);

		if (claims.get("auth") == null) {
			throw new IllegalArgumentException("권한 정보가 없는 토큰입니다.");
		}

		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get("auth").toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.toList();

		return new UsernamePasswordAuthenticationToken(claims.get("id"), "", authorities);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
			throw new IllegalArgumentException("Invalid JWT Token", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token", e);
			throw new IllegalArgumentException("Expired JWT Token", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token", e);
			throw new IllegalArgumentException("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.", e);
			throw new IllegalArgumentException("JWT claims string is empty.", e);
		}
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
			return bearerToken.substring(7);
		}
		return null;
	}

}
