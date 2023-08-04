package chivalrous.budgetbuddy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BudgetGroupStoreTypeResponse {

	private String storeType;
	private Double totalPrice;

}
