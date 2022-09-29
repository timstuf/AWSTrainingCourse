package hello.services;

import org.springframework.stereotype.Service;

import hello.entity.User;
import hello.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
	public User saveUser(User user) {
		user = userRepository.save(user);
		log.info("Successfully saved user {}", user);
		return user;
	}

	@Override
	public User findByUsername(String username) {
		log.info("Finding by username = {}", username);
		User user = userRepository.findByUsername(username).orElseThrow();
		log.info("User found.");
		return user;
	}

	@Override
	public User createUser(String email, String username) {
		User user = new User();
		user.setEmail(email);
		user.setUsername(username);
		user = saveUser(user);
		log.info("User created: {}", user);
		return user;
	}
}
