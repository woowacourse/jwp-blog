package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.CommentRepository;
import techcourse.myblog.dto.CommentDto;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> findByArticleId(Long articleId) {
        return Collections.unmodifiableList(commentRepository.findByArticleId(articleId));
    }

    public void save(CommentDto commentDto) {
        commentRepository.save(commentDto.toComment());
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    public Comment findByIdAndWriter(Long commentId, User user) {
        return commentRepository.findByIdAndWriter(commentId, user)
                .orElseThrow(() -> new MismatchAuthorException("작성자가 아닙니다."));
    }
}
