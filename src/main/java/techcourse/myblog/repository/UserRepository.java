package techcourse.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}