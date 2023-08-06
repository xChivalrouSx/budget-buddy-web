package chivalrous.budgetbuddy.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BudgetDetailResponse {

	List<BudgetResponse> budgets;
	List<BudgetGroupStoreTypeResponse> priceAsBudgetStoreType;

}
