package chivalrous.budgetbuddy.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegexPattern {
	BUDGET_STORE_TYPE("(.+) ([\\d,]+)( |)TL'lik( |)(işlemin|iadenin)( |)(\\d+)\\/(\\d+)( |)taksidi"),
	PRICE_TYPE("^([+-]|)([\\d,.]+)( |)(\\w*)");

	private final String pattern;
}
