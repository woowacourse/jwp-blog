package techcourse.myblog.repository;

import techcourse.myblog.domain.comment.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}

