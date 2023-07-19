package chivalrous.budgetbuddy.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BbCollection {
	BUDGET("budget"),
	USER("user");

	private final String name;
}
