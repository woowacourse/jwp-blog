package techcourse.myblog.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import techcourse.myblog.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}