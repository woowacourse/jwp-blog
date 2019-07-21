package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import techcourse.myblog.dto.ArticleRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ArticleRepoTest {

    @Autowired
    ArticleRepo articleRepo;

    @Test
    void saveTest() {
        ArticleRequestDto dto = new ArticleRequestDto();
        dto.setTitle("a");
        Article a = Article.of(dto);

        Article newArticle = articleRepo.save(a);
        assertThat(newArticle.getTitle()).isEqualTo("a");
        assertThat(newArticle.getId()).isEqualTo(1);
    }
//
//    @Test
//    void name() {
//        webTestClient.post().uri("articles")
//                .body(fromFormData("title","title"))
//                .exchange()
//                .expectStatus().isFound();
//
//    }
}
