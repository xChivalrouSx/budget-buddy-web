package chivalrous.budgetbuddy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import chivalrous.budgetbuddy.dto.request.BudgetsRequest;
import chivalrous.budgetbuddy.dto.response.BudgetResponse;
import chivalrous.budgetbuddy.dto.response.BudgetSummaryResponse;
import chivalrous.budgetbuddy.service.BudgetService;
import chivalrous.budgetbuddy.util.DateUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BudgetController {

	private final BudgetService budgetService;

	@GetMapping("/budgets")
	public ResponseEntity<List<BudgetResponse>> getBudgetsByPeriod(@RequestBody BudgetsRequest budgetsRequest) {
		DateUtil.checkYearAndMonthForPeriod(budgetsRequest.getYear(), budgetsRequest.getMonth());

		String period = DateUtil.getBudgetPeriod(budgetsRequest.getYear(), budgetsRequest.getMonth());
		List<BudgetResponse> budgetResponseList = budgetService.getBudgetsByPeriod(period);
		return ResponseEntity.ok().body(budgetResponseList);
	}

	@GetMapping("/budget-summary")
	public ResponseEntity<BudgetSummaryResponse> getBudgetSummaryByPeriod(@RequestBody BudgetsRequest budgetsRequest) {
		DateUtil.checkYearAndMonthForPeriod(budgetsRequest.getYear(), budgetsRequest.getMonth());

		String period = DateUtil.getBudgetPeriod(budgetsRequest.getYear(), budgetsRequest.getMonth());
		BudgetSummaryResponse budgetSummaryResponse = budgetService.getBudgetSummaryByPeriod(period);
		return ResponseEntity.ok().body(budgetSummaryResponse);
	}

	@GetMapping("/budget-summary-all")
	public ResponseEntity<List<BudgetSummaryResponse>> getAllBudgetSummary() {
		List<BudgetSummaryResponse> budgetSummaryListResponse = budgetService.getAllBudgetSummary();
		return ResponseEntity.ok().body(budgetSummaryListResponse);
	}

}
