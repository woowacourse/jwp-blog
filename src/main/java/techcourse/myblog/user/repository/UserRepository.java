package techcourse.myblog.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.user.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}