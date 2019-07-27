package techcourse.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import techcourse.myblog.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByArticleId(Long articleId);
}
