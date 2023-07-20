package chivalrous.budgetbuddy.constant;

import chivalrous.budgetbuddy.exception.BbBadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BudgetBank {
	YAPI_KREDI("Yapı Kredi", "yapi-kredi"),
	EN_PARA("Enpara", "enpara");

	private final String name;
	private final String inputValue;

	public static BudgetBank getBudgetBankFromType(String bankType) {
		for (BudgetBank budgetDocumentType : BudgetBank.values()) {
			if (budgetDocumentType.getInputValue().equals(bankType)) {
				return budgetDocumentType;
			}
		}
		throw new BbBadRequestException(ErrorMessage.BUDGET_BANK_NOT_FOUND);
	}
}
