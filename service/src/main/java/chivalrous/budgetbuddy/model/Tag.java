package chivalrous.budgetbuddy.model;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import chivalrous.budgetbuddy.dto.request.TagAutoRequest;
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
public class Tag {

	private String id;
	private String userId;
	private String tag;
	private String storeType;
	private List<String> storeNameKeywords;

	public static Tag fromTagAutoRequest(TagAutoRequest tagAutoRequest, String userId) {
		String tag = tagAutoRequest.getTag();
		return Tag.builder()
				.id(DigestUtils.md5Hex(tag).toUpperCase())
				.userId(userId)
				.tag(tag)
				.storeType(tagAutoRequest.getStoreType())
				.storeNameKeywords(tagAutoRequest.getStoreNameKeywords())
				.build();
	}
}
