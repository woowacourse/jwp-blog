package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.CommentNotFoundException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.dto.CommentRequestDto;

import static techcourse.myblog.web.dto.CommentAssembler.convertToEntity;

@Service
@Transactional
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

    public Comment save(final CommentRequestDto commentRequestDto, final Long userId, final Long articleId) {
        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findById(articleId)
            .orElseThrow(ArticleNotFoundException::new);
        Comment comment = convertToEntity(commentRequestDto, user, article);
        return commentRepository.save(comment);
    }

    public Comment update(final CommentRequestDto commentRequestDto, final Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(CommentNotFoundException::new);
        comment.update(commentRequestDto.getContents());
        return comment;
    }

    public void delete(final Long id) {
        commentRepository.findById(id)
            .orElseThrow(CommentNotFoundException::new);
        commentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Comment findById(final Long id) {
        return commentRepository.findById(id)
            .orElseThrow(CommentNotFoundException::new);
    }
}
