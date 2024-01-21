package chivalrous.budgetbuddy.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TagAutoRequest {

	private String tag;
	private String storeType;
	private List<String> storeNameKeywords;

}
