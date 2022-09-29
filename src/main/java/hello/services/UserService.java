package hello.services;

import hello.entity.User;

public interface UserService {
	User saveUser(User user);

	User findByUsername(String username);

	User createUser(String email, String username);
}
