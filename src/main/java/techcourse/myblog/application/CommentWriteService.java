package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.CommentRequestDto;
import techcourse.myblog.application.dto.CommentResponseDto;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.user.User;

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

    public CommentResponseDto save(CommentRequestDto commentRequestDto, User user, Article article) {
        return buildCommentResponseDto(commentRepository.save(CommentAssembler.toEntity(commentRequestDto, user, article)));
    }

    public CommentResponseDto modify(Long commentId, CommentRequestDto commentRequestDto, User user, Article article) {
        return buildCommentResponseDto(commentReadService.findById(commentId).update(CommentAssembler.toEntity(commentRequestDto, user, article)));
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
