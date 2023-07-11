package chivalrous.budgetbuddy.config.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class JwtSettings {

	@Value("${chivalrous.budgetbuddy.jwt.secret-key}")
	private String jwtSecretKey;

	@Value("${chivalrous.budgetbuddy.jwt.validity-as-hours}")
	private Long JwtValidityAsHours;

}
