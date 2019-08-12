package techcourse.myblog.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
