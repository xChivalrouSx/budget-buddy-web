package chivalrous.budgetbuddy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BudgetsRequest {

	private int year;
	private int month;

}
