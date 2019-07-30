package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.repository.UserRepository;

@Component
public class DataLoader implements ApplicationRunner {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public DataLoader(final UserRepository userRepository, final ArticleRepository articleRepository, final CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    public void run(ApplicationArguments args) {
        User user = new User("user", "user@mail.net", "aSdF12#$");
        userRepository.save(user);

        Article article = new Article("asdf", "coverUrl", "qwerty", user);
        Comment comment = new Comment(user, article, "zxcv");
        articleRepository.save(article);
        commentRepository.save(comment);
    }
}
