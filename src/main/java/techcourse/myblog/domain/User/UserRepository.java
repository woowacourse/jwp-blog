package techcourse.myblog.domain.User;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);
}
