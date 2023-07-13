package chivalrous.budgetbuddy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import chivalrous.budgetbuddy.config.jwt.JwtTokenManager;
import chivalrous.budgetbuddy.dto.request.AuthRequest;
import chivalrous.budgetbuddy.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final JwtTokenManager jwtTokenManager;
	private final AuthenticationManager authenticationManager;

	@PostMapping("/authenticate")
	public ResponseEntity<AuthResponse> authenticateUserAndGetToken(@RequestBody AuthRequest authRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		return ResponseEntity.ok().body(new AuthResponse(authRequest.getUsername(), jwtTokenManager.generateToken(authRequest.getUsername())));
	}

}
