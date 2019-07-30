package techcourse.myblog.service.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.CommentNotFoundException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.presentation.ArticleRepository;
import techcourse.myblog.presentation.CommentRepository;
import techcourse.myblog.presentation.UserRepository;
import techcourse.myblog.service.dto.comment.CommentRequestDto;
import techcourse.myblog.service.dto.comment.CommentResponseDto;

import static techcourse.myblog.service.comment.CommentAssembler.convertToDto;
import static techcourse.myblog.service.comment.CommentAssembler.convertToEntity;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public CommentResponseDto save(final CommentRequestDto commentRequestDto, final Long userId, final Long articleId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
        Comment comment = convertToEntity(commentRequestDto, user, article);
        Comment persistComment = commentRepository.save(comment);
        return convertToDto(persistComment);
    }

    public CommentResponseDto update(final CommentRequestDto commentRequestDto, final Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.update(commentRequestDto.getComment());
        return convertToDto(comment);
    }

    public void delete(final Long id) {
        commentRepository.findById(id)
                .orElseThrow(CommentNotFoundException::new);
        commentRepository.deleteById(id);
    }

    public CommentResponseDto findById(final Long id) {
        return convertToDto(commentRepository.findById(id)
                .orElseThrow(CommentNotFoundException::new));
    }
}
