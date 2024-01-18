package chivalrous.budgetbuddy.model;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

import chivalrous.budgetbuddy.constant.BudgetBank;
import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.constant.RegexPattern;
import chivalrous.budgetbuddy.dto.SingleImportDTO;
import chivalrous.budgetbuddy.dto.request.BudgetDocumentImportRequest;
import chivalrous.budgetbuddy.dto.request.BudgetSingleImportRequest;
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
	private boolean income;

	public static Budget fromSingleImportDTO(BudgetSingleImportRequest budgetDocumentSingleImportRequest, User user) {
		Budget budget = new Budget();
		SingleImportDTO singleImportDTO = budgetDocumentSingleImportRequest.getSingleImportDTO();

		budget.setDate(singleImportDTO.getDate());
		budget.setStoreName(singleImportDTO.getDescription());
		budget.setTags(singleImportDTO.getTags());

		budget.setIncome(budgetDocumentSingleImportRequest.isIncome());
		budget.setPrice(singleImportDTO.getPrice());
		budget.setPriceForInstallment(singleImportDTO.getPrice());

		budget.setTotalInstallment(1);
		budget.setPaidInstallment(1);
		budget.setRemainingInstallment(budget.getTotalInstallment() - budget.getPaidInstallment());

		setBudgetPeriodValues(budget, budgetDocumentSingleImportRequest.getYear(), budgetDocumentSingleImportRequest.getMonth());

		String textForId = budget.getPeriodInt() + budget.getDate().toString() + budget.getStoreName() + budget.getPrice();
		setBudgetId(budget, textForId);

		budget.setUserId(user.getId());
		budget.setBank(budgetDocumentSingleImportRequest.getBank().trim());

		return budget;
	}

	public static Budget fromEnparaPdfStringList(int index, String budgetLine, BudgetDocumentImportRequest budgetDocumentImportRequest, User user) {
		Budget budget = new Budget();
		budget.setIncome(false);

		Pattern regexPattern = Pattern.compile(RegexPattern.ENPARA_WITH_INSTALLMENT.getPattern());
		Matcher regexMatcher = regexPattern.matcher(budgetLine);
		if (regexMatcher.find()) {
			budget.setDate(DateUtil.stringDateToDate("dd/MM/yyyy", regexMatcher.group(1)));
			budget.setStoreName(regexMatcher.group(2));
			budget.setPrice(clearStringAndParseDouble(regexMatcher.group(3).replace(".", ""), ",", "."));
			budget.setTotalInstallment(Integer.parseInt(regexMatcher.group(5)));
			budget.setPaidInstallment(Integer.parseInt(regexMatcher.group(4)));
			budget.setRemainingInstallment(budget.getTotalInstallment() - budget.getPaidInstallment());
			budget.setPriceForInstallment(clearStringAndParseDouble(regexMatcher.group(6).replace(".", ""), ",", "."));
		} else {
			regexPattern = Pattern.compile(RegexPattern.ENPARA_WITH_RETURN.getPattern());
			regexMatcher = regexPattern.matcher(budgetLine);
			if (regexMatcher.find()) {
				budget.setDate(DateUtil.stringDateToDate("dd/MM/yyyy", regexMatcher.group(1)));
				budget.setStoreName(regexMatcher.group(2));

				String totalInstallment = regexMatcher.group(4);
				String paidInstallment = regexMatcher.group(3);
				budget.setTotalInstallment(totalInstallment != null ? Integer.parseInt(totalInstallment) : 1);
				budget.setPaidInstallment(paidInstallment != null ? Integer.parseInt(paidInstallment) : 1);
				budget.setRemainingInstallment(budget.getTotalInstallment() - budget.getPaidInstallment());

				budget.setPriceForInstallment(clearStringAndParseDouble(regexMatcher.group(5).replace(".", "").replace("-", "").trim(), ",", ".") * -1);
				budget.setPrice(budget.getPriceForInstallment() * budget.getTotalInstallment());
			} else {

				regexPattern = Pattern.compile(RegexPattern.ENPARA_WITH_NO_INSTALLMENT.getPattern());
				regexMatcher = regexPattern.matcher(budgetLine);
				if (regexMatcher.find()) {
					budget.setDate(DateUtil.stringDateToDate("dd/MM/yyyy", regexMatcher.group(1)));
					budget.setStoreName(regexMatcher.group(2));

					double tmpPrice = clearStringAndParseDouble(regexMatcher.group(3).replace(".", "").replace("-", ""), ",", ".");
					tmpPrice = budget.getStoreName().contains("deme - Enpara.com Cepubesi") ? tmpPrice * -1 : tmpPrice;

					budget.setPrice(tmpPrice);
					budget.setTotalInstallment(1);
					budget.setPaidInstallment(1);
					budget.setRemainingInstallment(0);
					budget.setPriceForInstallment(budget.getPrice());
				} else {
					return null;
				}
			}
		}

		setBudgetGeneralInfo(index, user.getId(), budget, budgetDocumentImportRequest);
		return budget;
	}

	public static Budget fromWorldCardExcelStringList(int index, List<String> excelStringList,
			BudgetDocumentImportRequest budgetDocumentImportRequest, User user) {
		Budget budget = new Budget();
		budget.setIncome(false);

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

		setBudgetGeneralInfo(index, user.getId(), budget, budgetDocumentImportRequest);
		return budget;
	}

	private static void setBudgetGeneralInfo(int index, String userId, Budget budget, BudgetDocumentImportRequest budgetDocumentImportRequest) {
		setBudgetPeriodValues(budget, budgetDocumentImportRequest.getYear(), budgetDocumentImportRequest.getMonth());

		String textForId = index + "-" + budget.getPeriodInt() + budget.getDate().toString() + budget.getStoreName() + budget.getPrice();
		setBudgetId(budget, textForId);

		budget.setUserId(userId);
		budget.setBank(BudgetBank.getBudgetBankFromType(budgetDocumentImportRequest.getBank()).getName());
	}

	private static void setBudgetId(Budget budget, String stringIdForHex) {
		budget.setId(DigestUtils.md5Hex(stringIdForHex).toUpperCase());
	}

	private static void setBudgetPeriodValues(Budget budget, int year, int month) {
		String period = DateUtil.getBudgetPeriod(year, month);
		budget.setPeriod(period);
		int periodInt = DateUtil.getBudgetPeriodAsInt(year, month);
		budget.setPeriodInt(periodInt);
	}

	private static Double clearStringAndParseDouble(String value, String replaceValue, String replacementValue) {
		if ("".equals(value)) {
			return 0.0;
		}
		return Double.valueOf(value.replace(replaceValue, replacementValue));
	}

}
