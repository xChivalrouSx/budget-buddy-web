package chivalrous.budgetbuddy.util;

import java.text.DecimalFormat;

import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.exception.BbServiceException;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PriceUtil {

	public static Double formatPrice(Double price) {
		return formatPrice(price, 2);
	}

	public static Double formatPrice(Double price, int decimalPlaces) {
		if (decimalPlaces < 1) {
			throw new BbServiceException(ErrorMessage.INVALID_DECIMAL_FORMAT);
		}

		StringBuilder pattern = new StringBuilder("#.");
		for (int i = 0; i < decimalPlaces; i++) {
			pattern.append("#");
		}
		DecimalFormat df = new DecimalFormat(pattern.toString());
		return Double.valueOf(df.format(price));
	}

}
