package techcourse.myblog.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleId(Long articleId);

    Optional<Comment> findByIdAndWriter(Long commentId, User user);
}
