package chivalrous.budgetbuddy.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import chivalrous.budgetbuddy.config.Settings;
import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.dto.request.UserCreateRequest;
import chivalrous.budgetbuddy.exception.BbAuthException;
import chivalrous.budgetbuddy.exception.BbUserCouldNotCreate;
import chivalrous.budgetbuddy.model.User;
import chivalrous.budgetbuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final Settings settings;

	public void createUser(UserCreateRequest userCreateRequest) {
		if (!settings.isUserCreationEnabled()) {
			throw new BbAuthException(ErrorMessage.USER_CREATION_DISABLE);
		}
		if (getUser(userCreateRequest.getUsername()) != null) {
			throw new BbUserCouldNotCreate(ErrorMessage.USER_COULD_NOT_CREATE);
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

	public User getAuthenticatedUser() {
		return userRepository.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
	}

}
