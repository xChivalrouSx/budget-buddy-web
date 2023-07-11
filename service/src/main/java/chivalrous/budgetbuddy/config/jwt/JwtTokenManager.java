package chivalrous.budgetbuddy.config.jwt;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenManager {

	private final JwtSettings jwtSettings;
	private final UserDetailService userDetailService;

	public String generateToken(String username) {
		Date currentDate = new Date();
		Date expiredDate = new Date(currentDate.getTime() + (jwtSettings.getJwtValidityAsHours() * 1000));

		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(currentDate)
				.setExpiration(expiredDate)
				.signWith(SignatureAlgorithm.HS256, jwtSettings.getJwtSecretKey())
				.compact();
	}

	public boolean isTokenValid(String token) {
		return token != null && getClaims(token).getExpiration().after(new Date());
	}

	public UserDetails getTokenUser(String token) {
		String username = token != null ? getClaims(token).getSubject() : "";
		return userDetailService.loadUserByUsername(username);
	}

	public Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(jwtSettings.getJwtSecretKey()).parseClaimsJws(token).getBody();
	}

}
