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
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ArticleService articleService;

    public CommentService(CommentRepository commentRepository, UserService userService, ArticleService articleService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleService = articleService;
    }

    @Transactional
    public Comment addComment(Long articleId, String email, CommentDto commentDto) {
        Article article = articleService.findArticle(articleId);
        User author = userService.getUserByEmail(email);
        Comment comment = commentDto.toEntity(author, article);
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(long commentId, String email) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException::new);
        if (!comment.isAuthor(email)) {
            throw new CommentException("당신은 죽을수도 있습니다.");
        }
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public Comment updateComment(long commentId, String email, CommentDto commentDto) {
        Comment comment = commentRepository.getOne(commentId);
        if (!comment.isAuthor(email)) {
            throw new CommentException("FBI WARNING");
        }
        return comment.updateContent(commentDto.getContents());
    }
}
