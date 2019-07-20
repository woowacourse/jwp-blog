package techcourse.myblog.repository;

import org.springframework.data.repository.CrudRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserEmail;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(UserEmail email);
}
