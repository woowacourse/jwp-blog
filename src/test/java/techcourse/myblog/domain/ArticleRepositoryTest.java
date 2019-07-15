package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleRepositoryTest {
    String title = "제목";
    String coverUrl = "링크";
    String contents = "내용";

    @Autowired
    private ArticleRepository articleRepository;

    Article testArticle;

    @BeforeEach
    void setUp() {
        testArticle = new Article(title, coverUrl, contents);
    }

    @Test
    void write() {
        Article persistentArticle = articleRepository.write(testArticle);
        assertThat(persistentArticle).isEqualTo(testArticle);
        Article retrievedArticle = articleRepository.find(persistentArticle.getNumber());
        assertThat(retrievedArticle).isEqualTo(testArticle);
        assertThat(articleRepository.findAll().isEmpty()).isFalse();
    }

    @Test
    void find() {
        Article persistentArticle = articleRepository.write(testArticle);
        assertThat(articleRepository.find(persistentArticle.getNumber())).isEqualTo(testArticle);
    }

    @Test
    void edit() {
        String newTitle = "새 제목";
        String newCoverUrl = "새 링크";
        String newContents = "새 내용";
        Article persistentArticle = articleRepository.write(testArticle);
        articleRepository.edit(new Article(newTitle, newCoverUrl, newContents), persistentArticle.getNumber());
        Article editedArticle = articleRepository.find(persistentArticle.getNumber());
        assertThat(
            editedArticle.getTitle() == newTitle
                && editedArticle.getCoverUrl() == newCoverUrl
                && editedArticle.getContents() == newContents
        );
    }
}