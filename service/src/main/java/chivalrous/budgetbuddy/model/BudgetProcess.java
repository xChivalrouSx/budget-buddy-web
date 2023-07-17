package chivalrous.budgetbuddy.model;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.constant.RegexPattern;
import chivalrous.budgetbuddy.dto.request.BudgetDocumentImportRequest;
import chivalrous.budgetbuddy.exception.BbServiceException;
import chivalrous.budgetbuddy.util.DateUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetProcess {

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

	public static BudgetProcess fromWorldCardExcelStringList(List<String> excelStringList, BudgetDocumentImportRequest budgetDocumentImportRequest, User user) {
		BudgetProcess budgetProcess = new BudgetProcess();
		String periodSeperator = budgetDocumentImportRequest.getMonth() < 10 ? "-0" : "-";
		String period = budgetDocumentImportRequest.getYear() + periodSeperator + budgetDocumentImportRequest.getMonth();
		int periodInt = Integer.parseInt(period.replace("-", ""));

		if (excelStringList.get(0).trim().equals("-")) {
			budgetProcess.setDate(
					DateUtil.stringDateToDate("dd/MM/yyyy", "05/" + budgetDocumentImportRequest.getMonth() + "/" + budgetDocumentImportRequest.getYear()));
		} else {
			budgetProcess.setDate(DateUtil.stringDateToDate("dd/MM/yyyy", excelStringList.get(0)));
		}

		boolean isReturn = false;
		double priceField;
		Pattern regexPatternForPrice = Pattern.compile(RegexPattern.PRICE_TYPE.getPattern());
		Matcher regexMatcherForPrice = regexPatternForPrice.matcher(excelStringList.get(3));
		if (regexMatcherForPrice.find()) {
			isReturn = !regexMatcherForPrice.group(1).equals("");
			priceField = clearStringAndParseDouble(regexMatcherForPrice.group(2).replace(".", ""), ",", ".");
			priceField = isReturn ? priceField * -1 : priceField;
		} else {
			throw new BbServiceException(ErrorMessage.DOCUMENT_FORMAT_NOT_VALID);
		}

		Pattern regexPattern = Pattern.compile(RegexPattern.BUDGET_STORE_TYPE.getPattern());
		Matcher regexMatcher = regexPattern.matcher(excelStringList.get(1));
		if (regexMatcher.find()) {
			budgetProcess.setStoreName(regexMatcher.group(1));
			budgetProcess.setTotalInstallment(Integer.parseInt(regexMatcher.group(8)));
			budgetProcess.setPaidInstallment(Integer.parseInt(regexMatcher.group(7)));
			budgetProcess.setRemainingInstallment(budgetProcess.getTotalInstallment() - budgetProcess.getPaidInstallment());
			budgetProcess.setPrice(clearStringAndParseDouble(regexMatcher.group(2), ",", "."));
		} else {
			budgetProcess.setStoreName(excelStringList.get(1));
			budgetProcess.setTotalInstallment(1);
			budgetProcess.setPaidInstallment(1);
			budgetProcess.setRemainingInstallment(0);
			budgetProcess.setPrice(priceField);
		}

		budgetProcess.setStoreType(excelStringList.get(2));
		budgetProcess.setPriceForInstallment(priceField);

		if (excelStringList.size() == 7) {
			budgetProcess.setCardType(excelStringList.get(4).startsWith("Dijital") ? "Dijital Kart" : "Fiziksel Kart");
			budgetProcess.setGiftPoint(excelStringList.get(5).equals("-")
					? 0.0
					: BudgetProcess.clearStringAndParseDouble(excelStringList.get(5), ".", ""));
		} else if (excelStringList.size() == 8) {
			budgetProcess.setCardType(excelStringList.get(5).startsWith("Dijital") ? "Dijital Kart" : "Fiziksel Kart");
			budgetProcess.setGiftPoint(excelStringList.get(6).equals("-")
					? 0.0
					: BudgetProcess.clearStringAndParseDouble(excelStringList.get(6), ".", ""));
		} else {
			throw new BbServiceException(ErrorMessage.DOCUMENT_FORMAT_NOT_VALID);
		}

		budgetProcess.setPeriod(period);
		budgetProcess.setPeriodInt(periodInt);
		String textForId = budgetProcess.getPeriodInt() + budgetProcess.getDate().toString() + budgetProcess.getStoreName() + budgetProcess.getPrice();
		budgetProcess.setId(DigestUtils.md5Hex(textForId).toUpperCase());
		budgetProcess.setUserId(user.getId());

		return budgetProcess;
	}

	private static Double clearStringAndParseDouble(String value, String replaceValue, String replacementValue) {
		if ("".equals(value)) {
			return 0.0;
		}
		return Double.valueOf(value.replace(replaceValue, replacementValue));
	}

}
