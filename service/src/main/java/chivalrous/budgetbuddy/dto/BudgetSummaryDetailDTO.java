package chivalrous.budgetbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BudgetSummaryDetailDTO {

	private String title;
	private Double value;
	private boolean showDefault;
	private int order;

}
