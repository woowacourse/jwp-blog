package techcourse.myblog.repo;

import org.springframework.data.repository.CrudRepository;
import techcourse.myblog.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByEmail(String email);
}
