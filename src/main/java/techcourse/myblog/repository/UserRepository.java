package techcourse.myblog.repository;

import org.springframework.data.repository.CrudRepository;
import techcourse.myblog.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
