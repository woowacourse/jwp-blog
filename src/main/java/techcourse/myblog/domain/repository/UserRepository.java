package techcourse.myblog.domain.repository;

import org.springframework.data.repository.CrudRepository;
import techcourse.myblog.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    void deleteByEmail(String email);
}
