package chivalrous.budgetbuddy.dto.response;

import java.util.List;

import chivalrous.budgetbuddy.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagAutoResponse {

	private String id;
	private String tag;
	private String storeType;
	private List<String> storeNameKeywords;

	public static TagAutoResponse fromTag(Tag tag) {
		return TagAutoResponse.builder()
				.id(tag.getId())
				.tag(tag.getTag())
				.storeType(tag.getStoreType())
				.storeNameKeywords(tag.getStoreNameKeywords())
				.build();
	}

}
