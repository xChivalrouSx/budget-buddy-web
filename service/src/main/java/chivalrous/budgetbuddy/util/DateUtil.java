package chivalrous.budgetbuddy.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.exception.BudgetBuddyServiceException;

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
			throw new BudgetBuddyServiceException(ErrorMessage.DATE_CANNOT_PARSE, e);
		}
	}

}
