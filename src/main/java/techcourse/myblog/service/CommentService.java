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

    public Comment addComment(long articleId, User author, CommentDto commentDto) {
        Article article = articleService.findArticle(articleId);
        User user = userService.getUserByEmail(author.getEmail());
        Comment comment = commentDto.toEntity(article, user);
        return commentRepository.save(comment);
    }

    public void deleteComment(long commentId, User author) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException::new);
        if (!comment.isAuthor(author)) {
            throw new CommentException("당신은 죽을수도 있습니다.");
        }
        commentRepository.deleteById(commentId);
    }

    public void updateComment(long commentId, User user, CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException::new);
        comment.updateContents(commentDto.getContents(), user);
    }
}
