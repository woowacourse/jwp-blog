package techcourse.myblog.domain.repository;

import org.springframework.data.repository.CrudRepository;
import techcourse.myblog.domain.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {}