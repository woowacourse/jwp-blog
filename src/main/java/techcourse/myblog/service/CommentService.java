package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.controller.dto.CommentDto;
import techcourse.myblog.domain.*;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public void save(long userId, CommentDto commentDto) {
        Comment comment = getCommentOrElseThrow(userId, commentDto);
        commentRepository.save(comment);
    }

    private Comment getCommentOrElseThrow(long userId, CommentDto commentDto) {
        Article article = articleRepository.findById(commentDto.getArticleId()).orElseThrow(IllegalArgumentException::new);
        User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
        return commentDto.toComment(user, article);
    }

    public void delete(long userId, CommentDto commentDto) {
        Comment comment = getCommentOrElseThrow(userId, commentDto);
        commentRepository.delete(comment);
    }

    public Comment findCommentByIdOrElseThrow(long id) {
        return commentRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public Comment update(User user, CommentDto commentDto) {
        Comment comment = getCommentOrElseThrow(user.getId(), commentDto);
        commentRepository.save(comment);
        return comment;
    }
}
