package techcourse.myblog.user.domain;

import org.springframework.data.repository.CrudRepository;
import techcourse.myblog.user.domain.vo.Email;

import java.util.Optional;

public interface UserRepository extends CrudRepository<techcourse.myblog.user.domain.User, Long> {
    Optional<User> findByEmail(Email email);
}