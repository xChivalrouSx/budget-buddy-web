package chivalrous.budgetbuddy.dto.response;

import java.util.List;

import chivalrous.budgetbuddy.dto.BudgetSummaryDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BudgetSummaryResponse {

	private String period;
	private List<BudgetSummaryDetailDTO> summaryDetailList;

}
