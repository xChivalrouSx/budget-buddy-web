package chivalrous.budgetbuddy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BudgetSummaryResponse {

	private String period;
	private Double totalIncome;
	private Double totalPrice;
	private Double totalPriceWithoutInstallment;
	private Double totalPriceWithInstallment;
	private Double totalPriceEndingInstallment;
	private Double totalPriceStartingInstallment;

}
