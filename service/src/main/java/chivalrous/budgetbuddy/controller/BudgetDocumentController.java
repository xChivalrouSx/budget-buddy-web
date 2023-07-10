package chivalrous.budgetbuddy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import chivalrous.budgetbuddy.dto.request.BudgetDocumentImportRequestDto;
import chivalrous.budgetbuddy.service.BudgetDocumentService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BudgetDocumentController {

	private final BudgetDocumentService budgetDocumentService;

	@PostMapping("/budget-process/import")
	public ResponseEntity<String> getBudgetFromFile(@ModelAttribute BudgetDocumentImportRequestDto budgetDocumentImportRequestDto) {
		budgetDocumentService.getBudgetFromFile(budgetDocumentImportRequestDto);
		return ResponseEntity.ok("All data imported.");
	}

}
