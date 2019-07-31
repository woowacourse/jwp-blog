package techcourse.myblog.repository;

import org.springframework.data.repository.CrudRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.userinfo.UserEmail;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(UserEmail email);

    boolean existsByEmail(UserEmail email);
}
