package techcourse.myblog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.repository.UserRepository;

@SpringBootApplication
public class MyBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBlogApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(UserRepository userRepository, ArticleRepository articleRepository, CommentRepository commentRepository) {
        return (args -> {
            User user = userRepository.save(
                    new User("luffy", "luffy@luffy.com", "@Password12")
            );

            Article article = articleRepository.save(
                    new Article("제목", "", "내용", user)
            );

            commentRepository.save(
                    new Comment("contents", user, article)
            );
        });
    }
}
