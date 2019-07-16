package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleRepositoryTest {
        String title = "제목";
        String coverUrl = "링크";
        String contents = "내용";
        Article testArticle;
        @Autowired
        private ArticleRepository articleRepository;

        @BeforeEach
        void setUp() {
                articleRepository.deleteAll();
                testArticle = new Article(title, coverUrl, contents);
        }

        @Test
        void write() {
                articleRepository.write(testArticle);
                assertThat(
                    articleRepository.findAll().isEmpty()
                ).isFalse();
        }

        @Test
        void find1() {
                articleRepository.write(testArticle);
                assertThat(
                    articleRepository.find(articleRepository.getLastArticleId())
                ).isEqualTo(testArticle);
        }

        @Test
        void find2() {
                articleRepository.write(testArticle);
                assertThrows(IllegalArgumentException.class, () -> {
                        articleRepository.find(articleRepository.getLastArticleId() + 1);
                });
        }

        @Test
        void edit() {
                articleRepository.write(testArticle);
                articleRepository.edit(new Article(), articleRepository.getLastArticleId());
                assertThat(
                    articleRepository.find(articleRepository.getLastArticleId()).getCoverUrl()
                ).isEqualTo("");
        }

        @Test
        void delete() {
                articleRepository.write(testArticle);
                articleRepository.delete(articleRepository.getLastArticleId());
                assertThat(
                    articleRepository.findAll().size()
                ).isZero();
        }
}