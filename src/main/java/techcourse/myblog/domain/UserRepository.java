package techcourse.myblog.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import techcourse.myblog.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);
}