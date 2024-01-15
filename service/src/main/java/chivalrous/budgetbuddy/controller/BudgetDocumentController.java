package chivalrous.budgetbuddy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import chivalrous.budgetbuddy.dto.request.BudgetDocumentImportRequest;
import chivalrous.budgetbuddy.dto.request.BudgetSingleImportRequest;
import chivalrous.budgetbuddy.dto.response.SuccessResponse;
import chivalrous.budgetbuddy.service.BudgetDocumentService;
import chivalrous.budgetbuddy.util.DateUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BudgetDocumentController {

	private final BudgetDocumentService budgetDocumentService;

	@PostMapping("/budget-document/import")
	public ResponseEntity<SuccessResponse> getBudgetFromFile(@ModelAttribute BudgetDocumentImportRequest budgetDocumentImportRequest) {
		DateUtil.checkYearAndMonthForPeriod(budgetDocumentImportRequest.getYear(), budgetDocumentImportRequest.getMonth());
		budgetDocumentService.getBudgetFromFile(budgetDocumentImportRequest);
		return ResponseEntity.ok().body(new SuccessResponse("All data imported."));
	}

	@PostMapping("/budget-document/single-import")
	public ResponseEntity<SuccessResponse> getBudget(@RequestBody BudgetSingleImportRequest budgetDocumentSingleImportRequest) {
		DateUtil.checkYearAndMonthForPeriod(budgetDocumentSingleImportRequest.getYear(), budgetDocumentSingleImportRequest.getMonth());
		budgetDocumentService.getBudget(budgetDocumentSingleImportRequest);
		return ResponseEntity.ok().body(new SuccessResponse("Data imported."));
	}

}
