package chivalrous.budgetbuddy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import chivalrous.budgetbuddy.dto.request.UserCreateRequestDto;
import chivalrous.budgetbuddy.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/user")
	public ResponseEntity<String> createUser(@RequestBody UserCreateRequestDto userCreateRequestDto) {
		userService.createUser(userCreateRequestDto);
		return ResponseEntity.ok("User created.");
	}
}
