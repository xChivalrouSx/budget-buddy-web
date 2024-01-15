package chivalrous.budgetbuddy.dto.request;

import chivalrous.budgetbuddy.dto.SingleImportDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BudgetSingleImportRequest {

	private SingleImportDTO singleImportDTO;
	private String bank;
	private int year;
	private int month;

}
