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
            User luffy = userRepository.save(
                    new User("luffy", "luffy@luffy.com", "12345678")
            );

            User cony = userRepository.save(
                    new User("cony", "cony@cony.com", "12345678")
            );

            Article article = articleRepository.save(
                    new Article("제목", "", "내용", luffy)
            );

            commentRepository.save(
                    new Comment("contents1", luffy, article)
            );

            commentRepository.save(
                    new Comment("contents2", luffy, article)
            );
        });
    }
}
