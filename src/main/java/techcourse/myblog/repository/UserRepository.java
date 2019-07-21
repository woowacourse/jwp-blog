package techcourse.myblog.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import techcourse.myblog.domain.User;

import javax.transaction.Transactional;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);

    @Transactional
    @Modifying
    void deleteByEmail(String email);
}
