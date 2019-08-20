package techcourse.myblog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.ArticleRequest;
import techcourse.myblog.service.dto.UserRequest;

@Component
@Profile("test")
public class Runner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(Runner.class);

    private final UserService userService;
    private final ArticleService articleService;

    public Runner(UserService userService, ArticleService articleService) {
        this.userService = userService;
        this.articleService = articleService;

        log.debug("Runner is created");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("begin");

        UserRequest userRequest = new UserRequest();
        userRequest.setName("CU");
        userRequest.setEmail("root@gmail.com");
        userRequest.setPassword("Password!1");
        userRequest.setReconfirmPassword("Password!1");
        User user = userService.saveUser(userRequest);

        ArticleRequest articleRequest = new ArticleRequest("title", "dd", "Dzz");

        articleService.save(articleRequest, user);
    }
}
