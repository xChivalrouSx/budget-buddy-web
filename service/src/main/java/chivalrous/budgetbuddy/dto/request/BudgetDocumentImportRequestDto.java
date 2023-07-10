package chivalrous.budgetbuddy.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BudgetDocumentImportRequestDto {

	private MultipartFile file;
	private int year;
	private int month;

}
