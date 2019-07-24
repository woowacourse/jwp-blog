package techcourse.myblog.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.domain.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByEmail(String email);

    User findUserByEmail(String email);

    User findUserById(long id);

    boolean existsByEmail(String email);
}
