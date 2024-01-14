package chivalrous.budgetbuddy.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SingleImportDTO {

	private Date date;
	private Double price;
	private String description;
	private List<String> tags;

}
