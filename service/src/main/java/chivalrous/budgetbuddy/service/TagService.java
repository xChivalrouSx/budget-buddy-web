package chivalrous.budgetbuddy.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import chivalrous.budgetbuddy.dto.request.TagAutoRequest;
import chivalrous.budgetbuddy.dto.response.TagAutoResponse;
import chivalrous.budgetbuddy.model.Tag;
import chivalrous.budgetbuddy.model.User;
import chivalrous.budgetbuddy.repository.TagRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {

	private final TagRepository tagRepository;
	private final UserService userService;

	public List<TagAutoResponse> getAutoTags() {
		User user = userService.getAuthenticatedUser();
		return getTags(user.getId()).stream().map(TagAutoResponse::fromTag).collect(Collectors.toList());
	}

	public void addAutoTag(TagAutoRequest tagAutoRequest) {
		User user = userService.getAuthenticatedUser();
		Tag tmpTag = Tag.fromTagAutoRequest(tagAutoRequest, user.getId());

		Tag tagDbResult = tagRepository.findByUserAndTag(tmpTag.getTag(), user.getId());
		if (tagDbResult != null) {
			tmpTag.getStoreNameKeywords().addAll(tagDbResult.getStoreNameKeywords());
		}

		tmpTag.setStoreNameKeywords(tmpTag.getStoreNameKeywords().stream().distinct().collect(Collectors.toList()));
		tagRepository.saveOrUpdateTag(tmpTag);
	}

	public void deleteAutoTag(String tag) {
		tagRepository.deleteTag(tag);
	}

	public void deleteAutoTagKeyword(String tag, String keyword) {
		tagRepository.deleteTagKeyword(tag, keyword);
	}

	public List<Tag> getTags(String userId) {
		return tagRepository.findByUser(userId);
	}
}
