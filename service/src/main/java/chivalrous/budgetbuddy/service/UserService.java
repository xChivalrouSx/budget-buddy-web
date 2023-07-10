package chivalrous.budgetbuddy.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import chivalrous.budgetbuddy.dto.request.UserCreateRequestDto;
import chivalrous.budgetbuddy.model.User;
import chivalrous.budgetbuddy.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public void createUser(UserCreateRequestDto userCreateRequestDto) {
		userRepository.createUser(User.builder()
				.id(DigestUtils.md5Hex(userCreateRequestDto.getUsername()).toUpperCase())
				.username(userCreateRequestDto.getUsername())
				.password(DigestUtils.md5Hex(userCreateRequestDto.getPassword()).toUpperCase())
				.build());
	}
}
