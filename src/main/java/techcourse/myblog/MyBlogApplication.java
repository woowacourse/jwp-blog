package techcourse.myblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyBlogApplication {

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled","true");
        System.setProperty("spring.devtools.livereload.enabled","true");
        SpringApplication.run(MyBlogApplication.class, args);
    }
}
