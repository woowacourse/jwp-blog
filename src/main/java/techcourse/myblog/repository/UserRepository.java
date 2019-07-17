package techcourse.myblog.repository;

import org.springframework.data.repository.CrudRepository;
import techcourse.myblog.domain.Article;

public interface UserRepository extends CrudRepository<User,Long> {
}
