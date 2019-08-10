package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.CommentResponseDto;
import techcourse.myblog.application.exception.NotFoundCommentException;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CommentReadService {
    private final CommentRepository commentRepository;

    public CommentReadService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment findById(Long commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(NotFoundCommentException::new);
    }

    public List<CommentResponseDto> findByArticleId(Long articleId) {
        return Collections.unmodifiableList(
                commentRepository.findByArticleId(articleId).stream()
                        .map(CommentAssembler::buildCommentResponseDto)
                        .collect(Collectors.toList())
        );
    }
}
