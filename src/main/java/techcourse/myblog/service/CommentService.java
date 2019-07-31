package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentException;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.repository.CommentRepository;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ArticleService articleService;

    public CommentService(CommentRepository commentRepository, UserService userService, ArticleService articleService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleService = articleService;
    }

    public Comment addComment(Long articleId, String email, CommentDto commentDto) {
        Article article = articleService.findArticle(articleId);
        User author = userService.getUserByEmail(email);
        Comment comment = commentDto.toEntity(article, author);
        return commentRepository.save(comment);
    }

    public void deleteComment(long commentId, String email) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException::new);
        if (!comment.isAuthor(email)) {
            throw new CommentException("당신은 죽을수도 있습니다.");
        }
        commentRepository.deleteById(commentId);
    }

    public void updateComment(long commentId, String email, CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException::new);
        if (!comment.isAuthor(email)) {
            throw new CommentException("FBI WARNING");
        }
        comment.updateContents(commentDto.getContents(), userService.getUserByEmail(email));
    }
}
