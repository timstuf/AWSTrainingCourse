package hello.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.amazonaws.xray.spring.aop.XRayEnabled;

import hello.entity.User;

@Repository
@XRayEnabled
public interface UserRepository extends CrudRepository<User, UUID> {
	Optional<User> findByUsername(String username);
}
