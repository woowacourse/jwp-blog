package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.CommentResponseDto;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;

import static techcourse.myblog.application.CommentAssembler.buildCommentResponseDto;

@Service
@Transactional
public class CommentWriteService {
    private final CommentRepository commentRepository;
    private final CommentReadService commentReadService;

    public CommentWriteService(CommentRepository commentRepository, CommentReadService commentReadService) {
        this.commentRepository = commentRepository;
        this.commentReadService = commentReadService;
    }

    public CommentResponseDto save(Comment comment) {
        return buildCommentResponseDto(commentRepository.save(comment));
    }

    public CommentResponseDto modify(Long commentId, Comment comment) {
        return buildCommentResponseDto(commentReadService.findById(commentId).update(comment));
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
