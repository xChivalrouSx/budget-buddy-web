package chivalrous.budgetbuddy.model;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import chivalrous.budgetbuddy.dto.request.BudgetDocumentImportRequest;
import chivalrous.budgetbuddy.util.DateUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetProcess {

	private String id;
	private Date date;
	private String storeName;
	private String storeType;
	private Double price;
	private Double giftPoint;
	private String cardType;
	private int totalInstallment;
	private int paidInstallment;
	private Double priceForInstallment;
	private List<String> tags;
	private String period;
	private int periodInt;

	public static BudgetProcess fromWorldCardExcelStringList(List<String> excelStringList, BudgetDocumentImportRequest budgetDocumentImportRequest) {
		BudgetProcess budgetProcess = new BudgetProcess();
		String periodSeperator = budgetDocumentImportRequest.getMonth() < 10 ? "-0" : "-";
		String period = budgetDocumentImportRequest.getYear() + periodSeperator + budgetDocumentImportRequest.getMonth();
		int periodInt = Integer.parseInt(period.replace("-", ""));

		budgetProcess.setDate(DateUtil.stringDateToDate("dd/MM/yyyy", excelStringList.get(0)));
		budgetProcess.setStoreType(excelStringList.get(2));
		budgetProcess.setCardType(excelStringList.get(5).startsWith("Dijital") ? "Dijital Kart" : "Fiziksel Kart");
		budgetProcess.setGiftPoint(excelStringList.get(6).equals("-")
				? 0.0
				: BudgetProcess.clearStringAndParseDouble(excelStringList.get(6), ".", ""));

		int isReturn = excelStringList.get(1).contains("iadenin") ? -1 : 1;
		String[] storeNameParts = excelStringList.get(1).split(" TR ");
		String installmentDetail = "1/1";
		String totalPrice = "";
		if (storeNameParts.length > 1) {
			totalPrice = storeNameParts[1].split("TL")[0].trim();
			String[] tmpInstallmentDetail = storeNameParts[1].replace("iadenin", "_")
					.replace("işlemin", "_").replace("taksidi", "").split("_");
			if (tmpInstallmentDetail.length > 1) {
				installmentDetail = tmpInstallmentDetail[1].trim();
			}
		}
		budgetProcess.setStoreName(storeNameParts[0]);
		budgetProcess.setTotalInstallment(Integer.parseInt(installmentDetail.split("/")[1]));
		budgetProcess.setPaidInstallment(Integer.parseInt(installmentDetail.split("/")[0]));
		budgetProcess.setPriceForInstallment(BudgetProcess.clearStringAndParseDouble(excelStringList.get(3), ",", ""));
		budgetProcess.setPrice((totalPrice.isEmpty()
				? BudgetProcess.clearStringAndParseDouble(excelStringList.get(3), ",", "")
				: BudgetProcess.clearStringAndParseDouble(totalPrice, ",", ".")) * isReturn);

		String textForId = budgetProcess.getDate().toString() + budgetProcess.getStoreName() + budgetProcess.getPrice();
		budgetProcess.setId(DigestUtils.md5Hex(textForId).toUpperCase());
		budgetProcess.setPeriod(period);
		budgetProcess.setPeriodInt(periodInt);

		return budgetProcess;
	}

	private static Double clearStringAndParseDouble(String value, String replaceValue, String replacementValue) {
		if ("".equals(value)) {
			return 0.0;
		}
		return Double.valueOf(value.replace(replaceValue, replacementValue));
	}

}
