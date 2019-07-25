package techcourse.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Long countByEmail(String email);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByEmailAndPassword(String email, String password);
}
