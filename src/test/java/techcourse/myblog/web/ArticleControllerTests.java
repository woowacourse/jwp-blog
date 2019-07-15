package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@ExtendWith(SpringExtension.class)
//반복적인 테스트를 하기 위해서 RANDOM_PORT를 사용한다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void writeForm_test() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void save_test() {
        webTestClient.post().uri("/articles")
                .body(BodyInserters.fromFormData("title", "title")
                        .with("coverUrl", "coverUrl").with("contents", "contents"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void update_test() {
        articleRepository.save(new Article("title", "url", "content"));
        webTestClient.put().uri("/articles/0")
                .body(BodyInserters.fromFormData("title", "title")
                        .with("coverUrl", "coverUrl").with("contents", "contents"))
                .exchange()
                .expectStatus().is3xxRedirection();
    }
    //delete에 대한 test도 추가하자.
    //취향 차이지만 repo에 바로 넣지 말고 webtestCLient를 활용해서 추가하는건 어떨까?

    //리다이렉션 테스트는 리다이렉션의 헤더에 값이 내가 원했던 주소의 값이 들어가는걸 테스트 해보는건 어떨까?
}
