package techcourse.myblog.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.domain.User;

public interface LoginRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);
}
