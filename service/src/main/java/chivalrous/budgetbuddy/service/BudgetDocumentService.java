package chivalrous.budgetbuddy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.dto.request.BudgetDocumentImportRequest;
import chivalrous.budgetbuddy.exception.BbServiceException;
import chivalrous.budgetbuddy.model.BudgetProcess;
import chivalrous.budgetbuddy.repository.BudgetDocumentRepository;
import jxl.Sheet;
import jxl.Workbook;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BudgetDocumentService {

	private final BudgetDocumentRepository budgetDocumentRepository;

	public void getBudgetFromFile(BudgetDocumentImportRequest budgetDocumentImportRequest) {
		try {
			Map<Integer, List<String>> data = new HashMap<>();
			Workbook workbook = Workbook.getWorkbook(budgetDocumentImportRequest.getFile().getInputStream());
			Sheet sheet = workbook.getSheet(0);

			int rows = sheet.getRows();
			int columns = sheet.getColumns();
			for (int i = 0; i < rows; i++) {
				data.put(i, new ArrayList<>());
				for (int j = 0; j < columns; j++) {
					data.get(i).add(sheet.getCell(j, i).getContents());
				}
			}
			workbook.close();

			data.values().removeIf(x -> x.get(0).equals("") && x.get(1).equals(""));
			List<BudgetProcess> budgetData = data.values().stream()
					.map(x -> BudgetProcess.fromWorldCardExcelStringList(x, budgetDocumentImportRequest))
					.collect(Collectors.toList());

			budgetDocumentRepository.saveBudgets(budgetData);
		} catch (Exception e) {
			throw new BbServiceException(ErrorMessage.FILE_COULD_NOT_READ, e);
		}

	}

}
