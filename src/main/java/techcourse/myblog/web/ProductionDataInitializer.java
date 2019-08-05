package techcourse.myblog.web;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import techcourse.myblog.domain.User;
import techcourse.myblog.persistence.ArticleRepository;
import techcourse.myblog.persistence.CommentRepository;
import techcourse.myblog.persistence.UserRepository;

@Profile("prod")
@Component
public class ProductionDataInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public ProductionDataInitializer(UserRepository userRepository, ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        userRepository.save(new User("moomin", "qweQWE123!@#", "moomin@naver.com"));
    }
}
