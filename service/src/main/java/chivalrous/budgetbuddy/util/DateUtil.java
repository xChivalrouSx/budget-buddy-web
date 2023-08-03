package chivalrous.budgetbuddy.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.exception.BbBadRequestException;
import chivalrous.budgetbuddy.exception.BbServiceException;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DateUtil {

	public static Date stringDateToDate(String date) {
		return stringDateToDate("yyyy-MM-dd HH:mm:ss", date);
	}

	public static Date stringDateToDate(String dateFormat, String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			sdf.setLenient(false);
			return sdf.parse(date);
		} catch (java.text.ParseException e) {
			throw new BbServiceException(ErrorMessage.DATE_CANNOT_PARSE, e);
		}
	}

	public static void checkYearAndMonthForPeriod(int year, int month) {
		if (year < 2000 || year > 9999) {
			throw new BbBadRequestException(ErrorMessage.INVALID_YEAR);
		}
		if (month < 1 || month > 12) {
			throw new BbBadRequestException(ErrorMessage.INVALID_MONTH);
		}
	}

	public static String getBudgetPeriod(int year, int month) {
		return String.format("%d-%02d", year, month);
	}

	public static int getBudgetPeriodAsInt(int year, int month) {
		return Integer.parseInt(getBudgetPeriod(year, month).replace("-", ""));
	}

	public static String getPreviousBudgetPeriod(int year, int month) {
		int previousMonth = month == 1 ? 12 : month - 1;
		int previousYear = month == 1 ? year - 1 : year;
		return String.format("%d-%02d", previousYear, previousMonth);
	}

	public static String getPreviousBudgetPeriod(String period) {
		int year = Integer.parseInt(period.split("-")[0]);
		int month = Integer.parseInt(period.split("-")[1]);
		return getPreviousBudgetPeriod(year, month);
	}

}
