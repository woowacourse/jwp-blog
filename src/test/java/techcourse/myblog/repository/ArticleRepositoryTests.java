package techcourse.myblog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleRepositoryTests {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void Article_User_ManyToOne_매핑_확인_Author_User_일치하는_경우() {
        User user = new User("권민철", "test@test.com", "12345678");
        User persistUser = testEntityManager.persist(user);

        Article article = new Article("제목", "coverUrl", "내용", persistUser);
        testEntityManager.persist(article);

        testEntityManager.flush();
        testEntityManager.clear();

        articleRepository.findById(article.getId()).ifPresent(savedArticle -> {
            assertEquals(savedArticle.getAuthor(), persistUser);
        });
    }
}