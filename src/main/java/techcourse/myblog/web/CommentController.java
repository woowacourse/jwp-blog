package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentDto;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.user.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class CommentController {

    private CommentRepository commentRepository;
    private ArticleRepository articleRepository;
    private UserService userService;

    public CommentController(CommentRepository commentRepository, ArticleRepository articleRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userService = userService;
    }

    @PostMapping("/comments/{articleId}")
    public String save(CommentDto commentDto, @PathVariable long articleId, HttpSession session) {
        Object userId = session.getAttribute("userId");
        Optional<Article> article = articleRepository.findById(articleId);

        if (article.isPresent() && userId != null) {
            UserDto userDto = userService.findByUserId((long) userId);
            commentRepository.save(commentDto.toEntity(userDto.toEntity(), article.get()));
        }

        return "redirect:/articles/" + articleId;
    }

    @Transactional
    @PutMapping("/comments/{articleId}/{commentId}")
    public String update(CommentDto commentDto, @PathVariable long articleId, @PathVariable long commentId, HttpSession session) {
        Object userId = session.getAttribute("userId");

        if (userId != null) {
            Comment comment = commentRepository.findById(commentId).get();
            checkAuthor(commentId, (long) userId);
            comment.update(commentDto.toEntity());
        }
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/comments/{articleId}/{commentId}")
    public String delete(@PathVariable long articleId, @PathVariable long commentId, HttpSession session) {
        Object userId = session.getAttribute("userId");

        if (userId != null) {
            checkAuthor(commentId, (long) userId);
            commentRepository.deleteById(commentId);
        }
        return "redirect:/articles/" + articleId;
    }

    private void checkAuthor(long commentId, long userId) {
        commentRepository.findById(commentId).ifPresent(article -> {
            if (article.getId() != userId) {
                throw new IllegalArgumentException("허가되지 않은 사용자입니다.");
            }
        });
    }
}
