package techcourse.myblog.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String email);
}
