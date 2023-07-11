package chivalrous.budgetbuddy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import chivalrous.budgetbuddy.dto.request.UserCreateRequest;
import chivalrous.budgetbuddy.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/user")
	public ResponseEntity<String> createUser(@RequestBody UserCreateRequest userCreateRequest) {
		userService.createUser(userCreateRequest);
		return ResponseEntity.ok("User created.");
	}

}
