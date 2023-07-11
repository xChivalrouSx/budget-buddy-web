package chivalrous.budgetbuddy.config.jwt;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import chivalrous.budgetbuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		chivalrous.budgetbuddy.model.User currentUser = userRepository.getUser(username);
		return new User(currentUser.getUsername(), currentUser.getPassword(), new ArrayList<>());
	}

}
