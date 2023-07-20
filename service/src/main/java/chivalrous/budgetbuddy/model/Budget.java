package chivalrous.budgetbuddy.model;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

import chivalrous.budgetbuddy.constant.BudgetBank;
import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.constant.RegexPattern;
import chivalrous.budgetbuddy.dto.request.BudgetDocumentImportRequest;
import chivalrous.budgetbuddy.exception.BbServiceException;
import chivalrous.budgetbuddy.util.DateUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Budget {

	private String id;
	private String userId;
	private Date date;
	private String storeName;
	private String storeType;
	private Double price;
	private Double giftPoint;
	private String cardType;
	private int totalInstallment;
	private int paidInstallment;
	private int remainingInstallment;
	private Double priceForInstallment;
	private List<String> tags;
	private String period;
	private int periodInt;
	private String bank;

	public static Budget fromWorldCardExcelStringList(List<String> excelStringList, BudgetDocumentImportRequest budgetDocumentImportRequest, User user) {
		Budget budget = new Budget();

		if (excelStringList.get(0).trim().equals("-")) {
			budget.setDate(
					DateUtil.stringDateToDate("dd/MM/yyyy", "05/" + budgetDocumentImportRequest.getMonth() + "/" + budgetDocumentImportRequest.getYear()));
		} else {
			budget.setDate(DateUtil.stringDateToDate("dd/MM/yyyy", excelStringList.get(0)));
		}

		boolean isReturn = false;
		double priceField;
		Pattern regexPatternForPrice = Pattern.compile(RegexPattern.PRICE_TYPE.getPattern());
		Matcher regexMatcherForPrice = regexPatternForPrice.matcher(excelStringList.get(3));
		if (regexMatcherForPrice.find()) {
			isReturn = !regexMatcherForPrice.group(1).equals("");
			if (excelStringList.size() == 7) {
				priceField = clearStringAndParseDouble(regexMatcherForPrice.group(2).replace(".", ""), ",", ".");
			} else if (excelStringList.size() == 8) {
				priceField = clearStringAndParseDouble(regexMatcherForPrice.group(2), ",", "");
			} else {
				throw new BbServiceException(ErrorMessage.DOCUMENT_FORMAT_NOT_VALID);
			}
			priceField = isReturn ? priceField * -1 : priceField;
		} else {
			throw new BbServiceException(ErrorMessage.DOCUMENT_FORMAT_NOT_VALID);
		}

		Pattern regexPattern = Pattern.compile(RegexPattern.BUDGET_STORE_TYPE.getPattern());
		Matcher regexMatcher = regexPattern.matcher(excelStringList.get(1));
		if (regexMatcher.find()) {
			budget.setStoreName(regexMatcher.group(1));
			budget.setTotalInstallment(Integer.parseInt(regexMatcher.group(8)));
			budget.setPaidInstallment(Integer.parseInt(regexMatcher.group(7)));
			budget.setRemainingInstallment(budget.getTotalInstallment() - budget.getPaidInstallment());
			budget.setPrice(clearStringAndParseDouble(regexMatcher.group(2), ",", "."));
		} else {
			budget.setStoreName(excelStringList.get(1));
			budget.setTotalInstallment(1);
			budget.setPaidInstallment(1);
			budget.setRemainingInstallment(0);
			budget.setPrice(priceField);
		}

		budget.setStoreType(excelStringList.get(2));
		budget.setPriceForInstallment(priceField);

		if (excelStringList.size() == 7) {
			budget.setCardType(excelStringList.get(4).startsWith("Dijital") ? "Dijital Kart" : "Fiziksel Kart");
			budget.setGiftPoint(excelStringList.get(5).equals("-")
					? 0.0
					: Budget.clearStringAndParseDouble(excelStringList.get(5), ".", ""));
		} else if (excelStringList.size() == 8) {
			budget.setCardType(excelStringList.get(5).startsWith("Dijital") ? "Dijital Kart" : "Fiziksel Kart");
			budget.setGiftPoint(excelStringList.get(6).equals("-")
					? 0.0
					: Budget.clearStringAndParseDouble(excelStringList.get(6), ".", ""));
		} else {
			throw new BbServiceException(ErrorMessage.DOCUMENT_FORMAT_NOT_VALID);
		}

		String period = DateUtil.getBudgetPeriod(budgetDocumentImportRequest.getYear(), budgetDocumentImportRequest.getMonth());
		budget.setPeriod(period);
		int periodInt = DateUtil.getBudgetPeriodAsInt(budgetDocumentImportRequest.getYear(), budgetDocumentImportRequest.getMonth());
		budget.setPeriodInt(periodInt);

		String textForId = budget.getPeriodInt() + budget.getDate().toString() + budget.getStoreName() + budget.getPrice();
		budget.setId(DigestUtils.md5Hex(textForId).toUpperCase());
		budget.setUserId(user.getId());
		budget.setBank(BudgetBank.getBudgetBankFromType(budgetDocumentImportRequest.getBank()).getName());

		return budget;
	}

	private static Double clearStringAndParseDouble(String value, String replaceValue, String replacementValue) {
		if ("".equals(value)) {
			return 0.0;
		}
		return Double.valueOf(value.replace(replaceValue, replacementValue));
	}

}
