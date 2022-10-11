package hello.services;

import org.springframework.stereotype.Service;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.spring.aop.XRayEnabled;

import hello.entity.User;
import hello.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@XRayEnabled
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
	public User saveUser(User user) {
		try(Subsegment subsegment = AWSXRay.beginSubsegment("RDS-SaveUser")) {
			user = userRepository.save(user);
		}
		log.info("Successfully saved user {}", user);
		return user;
	}

	@Override
	public User findByUsername(String username) {
		log.info("Finding by username = {}", username);
		User user;
		try(Subsegment subsegment = AWSXRay.beginSubsegment("RDS-FindByUsername")) {
			user = userRepository.findByUsername(username).orElseThrow();
		}
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
