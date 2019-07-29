package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.CommentDto;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private UserService userService;
    private ArticleService articleService;

    public CommentService(CommentRepository commentRepository, UserService userService, ArticleService articleService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleService = articleService;
    }

    public Comment save(Long userId, Long articleId, CommentDto commentDto) {
        User user = userService.findById(userId);
        Article article = articleService.findById(articleId);
        Comment comment = commentDto.toEntity(user, article);
        return commentRepository.save(comment);
    }
}
