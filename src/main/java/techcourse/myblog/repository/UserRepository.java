package techcourse.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Long countByEmail(String email);
    User findUserByEmailAndPassword(String email, String password);
}
