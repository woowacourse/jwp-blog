package techcourse.myblog.repository;

import techcourse.myblog.comment.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}

