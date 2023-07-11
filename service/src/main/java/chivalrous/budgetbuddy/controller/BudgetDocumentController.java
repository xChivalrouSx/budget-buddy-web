package chivalrous.budgetbuddy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import chivalrous.budgetbuddy.dto.request.BudgetDocumentImportRequest;
import chivalrous.budgetbuddy.service.BudgetDocumentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BudgetDocumentController {

	private final BudgetDocumentService budgetDocumentService;

	@PostMapping("/budget-process/import")
	public ResponseEntity<String> getBudgetFromFile(@ModelAttribute BudgetDocumentImportRequest budgetDocumentImportRequest) {
		budgetDocumentService.getBudgetFromFile(budgetDocumentImportRequest);
		return ResponseEntity.ok("All data imported.");
	}

}
