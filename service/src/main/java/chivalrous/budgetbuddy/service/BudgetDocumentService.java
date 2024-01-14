package chivalrous.budgetbuddy.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import chivalrous.budgetbuddy.constant.BudgetBank;
import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.constant.RegexPattern;
import chivalrous.budgetbuddy.dto.request.BudgetDocumentImportRequest;
import chivalrous.budgetbuddy.dto.request.BudgetDocumentSingleImportRequest;
import chivalrous.budgetbuddy.exception.BbServiceException;
import chivalrous.budgetbuddy.model.Budget;
import chivalrous.budgetbuddy.model.User;
import chivalrous.budgetbuddy.repository.BudgetDocumentRepository;
import chivalrous.budgetbuddy.util.DateUtil;
import io.github.jonathanlink.PDFLayoutTextStripper;
import jxl.Sheet;
import jxl.Workbook;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BudgetDocumentService {

	private final BudgetDocumentRepository budgetDocumentRepository;
	private final UserService userService;

	public void getBudget(BudgetDocumentSingleImportRequest budgetDocumentSingleImportRequest) {
		User currentUser = userService.getAuthenticatedUser();
		Budget budget = Budget.fromSingleImportDTO(budgetDocumentSingleImportRequest, currentUser);
		budgetDocumentRepository.saveBudget(budget);
	}

	public void getBudgetFromFile(BudgetDocumentImportRequest budgetDocumentImportRequest) {
		List<Budget> budgetData = new ArrayList<>();

		boolean isEnpara = BudgetBank.EN_PARA.getInputValue().equals(budgetDocumentImportRequest.getBank());
		boolean isPdf = budgetDocumentImportRequest.getFile().getOriginalFilename().endsWith(".pdf");
		if (isEnpara && isPdf) {
			budgetData = getBudgetFromEnparaPDF(budgetDocumentImportRequest);
		} else if (isEnpara) {
			// Soon
		} else if (!isEnpara && isPdf) {
			// Soon
		} else {
			budgetData = getBudgetFromYapiKrediExcel(budgetDocumentImportRequest);
		}

		budgetDocumentRepository.deleteBudgetsWithPeriodAndBank(
				DateUtil.getBudgetPeriod(budgetDocumentImportRequest.getYear(), budgetDocumentImportRequest.getMonth()),
				BudgetBank.getBudgetBankFromType(budgetDocumentImportRequest.getBank()).getName());

		budgetDocumentRepository.saveBudgets(budgetData);
	}

	public List<Budget> getBudgetFromYapiKrediExcel(BudgetDocumentImportRequest budgetDocumentImportRequest) {
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
			return budgetData;
		} catch (Exception e) {
			throw new BbServiceException(ErrorMessage.FILE_COULD_NOT_READ, e);
		}

	}

	public List<Budget> getBudgetFromEnparaPDF(BudgetDocumentImportRequest budgetDocumentImportRequest) {
		List<String> lines = getLinesFromEnparaPdf(budgetDocumentImportRequest.getFile());
		User currentUser = userService.getAuthenticatedUser();

		int index = 0;
		List<Budget> budgetData = new ArrayList<>();
		for (String line : lines) {
			budgetData.add(Budget.fromEnparaPdfStringList(index++, line, budgetDocumentImportRequest, currentUser));
		}
		return budgetData;
	}

	public List<String> getLinesFromEnparaPdf(MultipartFile file) {
		List<String> lines = new ArrayList<>();
		List<String> lastStepLines = new ArrayList<>();
		String pdfText;
		try {
			PDDocument document = PDDocument.load(file.getInputStream());
			PDFTextStripper textStripper = new PDFLayoutTextStripper();
			pdfText = textStripper.getText(document);
			pdfText = pdfText.replaceAll("(?<![\\wİıÖöÜüŞşÇçĞğ]) (?![\\wİıÖöÜüŞşÇçĞğ])", "");
			pdfText = pdfText.replaceAll(" +", " ");

			pdfText = pdfText.replaceAll("", "İ");
			pdfText = pdfText.replaceAll("", "İ");
			pdfText = pdfText.replaceAll("", "ş");
			pdfText = pdfText.replaceAll("", "ç");
			pdfText = pdfText.replaceAll("", "ç");
			pdfText = pdfText.replaceAll("", "ş");
			pdfText = pdfText.replaceAll("", "ö");
			pdfText = pdfText.replaceAll("", "ü");
			pdfText = pdfText.replaceAll("", "ö");
			pdfText = pdfText.replaceAll("", "ü");

			String[] lines_ = pdfText.split("\\r?\\n");
			lines = Arrays.asList(lines_).stream()
					.filter(p -> !p.trim().equals("") && !p.startsWith("(Faiz oranı"))
					.map(p -> p.trim())
					.collect(Collectors.toList());

			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i);

				Pattern regexPatternForDate = Pattern.compile(RegexPattern.DATE_START.getPattern());
				Matcher regexMatcherForDate = regexPatternForDate.matcher(line);
				if (regexMatcherForDate.find()) {
					if (!line.trim().endsWith("TL")) {
						int nextLineIndex = i;
						while (true) {
							nextLineIndex++;
							if (nextLineIndex == lines.size()) {
								break;
							}

							line += "Ş" + lines.get(nextLineIndex);
							if (line.trim().endsWith("TL")) {
								break;
							}
						}
						i = nextLineIndex;
					}
					lastStepLines.add(line);
				}
			}

			document.close();
		} catch (IOException e) {
			e.printStackTrace();
			lastStepLines = new ArrayList<>();
		}

		return lastStepLines;
	}
}
