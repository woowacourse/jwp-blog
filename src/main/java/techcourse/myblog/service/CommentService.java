package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.web.dto.CommentDto;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public Comment save(CommentDto commentDto) {
        return commentRepository.save(commentDto.create());
    }

    public Comment update(Long commentId, CommentDto commentDto) {
        Comment selectedComment = findById(commentId);
        return commentRepository.save(selectedComment.update(commentDto));
    }

    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}