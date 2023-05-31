package mc.budgetbuddy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class Settings {

	@Value("${mc.budgetbuddy.firebase.account-key-path}")
	private String firebaseAccountKeyPath;

}
