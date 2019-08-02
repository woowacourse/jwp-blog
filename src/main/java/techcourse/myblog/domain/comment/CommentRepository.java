package techcourse.myblog.domain.comment;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByArticleId(Long articleId);

    void deleteByArticleId(Long articleId);
}
