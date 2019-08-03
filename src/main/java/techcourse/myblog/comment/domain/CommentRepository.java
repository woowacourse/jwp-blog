package techcourse.myblog.comment.domain;

import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    Iterable<Comment> findAllByArticleId(long articleId);
}
