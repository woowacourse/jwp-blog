package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.persistence.ArticleRepository;
import techcourse.myblog.persistence.CommentRepository;
import techcourse.myblog.persistence.UserRepository;

@Profile("test")
@Component
public class TestDataInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public TestDataInitializer(UserRepository userRepository, ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        User user = new User("abcdeFGHI", "abcdEFGH123!@#", "abc@hi.com");
        userRepository.save(user);
        userRepository.save(new User("moomin", "mooMIN123!@#", "moomin@naver.com"));
        Article article = new Article("testTitle", "testContents", "https://www.incimages.com/uploaded_files/image/970x450/getty_509107562_2000133320009280346_351827.jpg");
        article.setAuthor(user);
        articleRepository.save(article);
        commentRepository.save(new Comment("second", user, article));
        commentRepository.save(new Comment("third", user, article));
        commentRepository.save(new Comment("first", user, article));
        commentRepository.save(new Comment("FourthComment", user, article));
        commentRepository.save(new Comment("FifthComment", user, article));
    }
}
