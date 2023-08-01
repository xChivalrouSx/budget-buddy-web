package chivalrous.budgetbuddy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.dto.request.BudgetDocumentImportRequest;
import chivalrous.budgetbuddy.exception.BbServiceException;
import chivalrous.budgetbuddy.model.Budget;
import chivalrous.budgetbuddy.model.User;
import chivalrous.budgetbuddy.repository.BudgetDocumentRepository;
import chivalrous.budgetbuddy.util.DateUtil;
import jxl.Sheet;
import jxl.Workbook;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BudgetDocumentService {

	private final BudgetDocumentRepository budgetDocumentRepository;
	private final UserService userService;

	public void getBudgetFromFile(BudgetDocumentImportRequest budgetDocumentImportRequest) {
		try {
			Map<Integer, List<String>> masterData = new HashMap<>();
			Map<Integer, List<String>> data = new HashMap<>();
			Workbook workbook = Workbook.getWorkbook(budgetDocumentImportRequest.getFile().getInputStream());
			Sheet sheet = workbook.getSheet(0);

			boolean isBudgetRowsStarted = false;
			int rows = sheet.getRows();
			int columns = sheet.getColumns();
			for (int i = 0; i < rows; i++) {
				if (!isBudgetRowsStarted) {
					masterData.put(i, new ArrayList<>());
				} else {
					data.put(i, new ArrayList<>());
				}

				for (int j = 0; j < columns; j++) {
					String content = sheet.getCell(j, i).getContents();
					if (!isBudgetRowsStarted && content.contains("İşlem Tarihi")) {
						isBudgetRowsStarted = true;
						break;
					} else if (isBudgetRowsStarted) {
						data.get(i).add(content);
					} else {
						masterData.get(i).add(content);
					}
				}
			}
			workbook.close();

			User currentUser = userService.getAuthenticatedUser();
			data.values().removeIf(x -> x.get(0).equals("") && x.get(1).equals(""));

			int index = 0;
			List<Budget> budgetData = new ArrayList<>();
			for (List<String> dataItems : data.values()) {
				budgetData.add(Budget.fromWorldCardExcelStringList(index++, dataItems, budgetDocumentImportRequest, currentUser));
			}

			budgetDocumentRepository.deleteBudgetsWithPeriod(DateUtil.getBudgetPeriod(
					budgetDocumentImportRequest.getYear(), budgetDocumentImportRequest.getMonth()));
			budgetDocumentRepository.saveBudgets(budgetData);
		} catch (Exception e) {
			throw new BbServiceException(ErrorMessage.FILE_COULD_NOT_READ, e);
		}

	}

}
