package techcourse.myblog.repository;

import java.util.List;

import techcourse.myblog.domain.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByArticleId(Long articleId);
}

