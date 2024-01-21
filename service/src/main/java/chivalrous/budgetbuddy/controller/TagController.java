package chivalrous.budgetbuddy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import chivalrous.budgetbuddy.dto.request.TagAutoRequest;
import chivalrous.budgetbuddy.dto.response.SuccessResponse;
import chivalrous.budgetbuddy.service.TagService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TagController {

	private final TagService tagService;

	@PostMapping("/tag/auto")
	public ResponseEntity<SuccessResponse> addAutoTag(@RequestBody TagAutoRequest tagAutoRequest) {
		tagService.addAutoTag(tagAutoRequest);
		return ResponseEntity.ok().body(new SuccessResponse("Auto tag added."));
	}

}
