package chivalrous.budgetbuddy.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.dto.request.UserCreateRequest;
import chivalrous.budgetbuddy.exception.BbServiceException;
import chivalrous.budgetbuddy.model.User;
import chivalrous.budgetbuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	public void createUser(UserCreateRequest userCreateRequest) {
		if (getUser(userCreateRequest.getUsername()) != null) {
			throw new BbServiceException(ErrorMessage.USER_COULD_NOT_CREATE);
		}

		userRepository.createUser(User.builder()
				.id(DigestUtils.md5Hex(userCreateRequest.getUsername()).toUpperCase())
				.username(userCreateRequest.getUsername())
				.password(passwordEncoder.encode(userCreateRequest.getPassword()))
				.build());
	}

	public User getUser(String username) {
		return userRepository.getUser(username);
	}

}
