package chivalrous.budgetbuddy.service;

import org.springframework.stereotype.Service;

import chivalrous.budgetbuddy.dto.request.TagAutoRequest;
import chivalrous.budgetbuddy.model.Tag;
import chivalrous.budgetbuddy.model.User;
import chivalrous.budgetbuddy.repository.TagRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {

	private final TagRepository tagRepository;
	private final UserService userService;

	public void addAutoTag(TagAutoRequest tagAutoRequest) {
		User user = userService.getAuthenticatedUser();
		Tag tmpTag = Tag.fromTagAutoRequest(tagAutoRequest, user.getId());

		Tag tagDbResult = tagRepository.getTagWithUser(tmpTag.getTag(), user.getId());
		if (tagDbResult != null) {
			tmpTag.getStoreNameKeywords().addAll(tagDbResult.getStoreNameKeywords());
		}
		tagRepository.saveOrUpdateTag(tmpTag);
	}

}
