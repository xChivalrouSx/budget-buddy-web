package chivalrous.budgetbuddy.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegexPattern {
	DATE_START("^(\\d{1,2}\\/\\d{1,2}\\/\\d{4})"),
	ENPARA_WITH_RETURN("(\\d{1,2}\\/\\d{1,2}\\/\\d{4}) (.+) \\(iade\\) ([\\d+]{0,1})[\\/]{0,1}([\\d+]{0,1})(.+) TL"),
	ENPARA_WITH_INSTALLMENT("(\\d{1,2}\\/\\d{1,2}\\/\\d{4}) (.+) \\((?:İşlem tutarı:|)(?: |)(.+)(?: |)TL\\)(?: |)(\\d+)\\/(\\d+)(?: |)(.+)(?: |)TL"),
	ENPARA_WITH_NO_INSTALLMENT("(\\d{1,2}\\/\\d{1,2}\\/\\d{4}) (.+\\w) (.+) TL"),
	BUDGET_STORE_TYPE("(.+) ([\\d,]+)( |)TL'lik( |)(işlemin|iadenin)( |)(\\d+)\\/(\\d+)( |)taksidi"),
	PRICE_TYPE("^([+-]|)([\\d,.]+)( |)(\\w*)");

	private final String pattern;
}
