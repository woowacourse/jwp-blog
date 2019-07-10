package techcourse.myblog;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloWorldControllerTests {

    //테스트를 하면 수동으로 하는 것에 비해 시간이 많이 단축된다.
    //webtestclient 를 검색해서 이것저것 해보자.
    //ex) webtestclient post

    @Autowired
    //얘가 하는 역할은 서버를 먼저 띄운다. (?)
    private WebTestClient webTestClient;

    @Test
    void passParamWithGet() {
        String blogName = "helloWrold";
        webTestClient.get().uri("/helloworld?blogName=" + blogName)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(new String(response.getResponseBody())).isEqualTo(blogName));

    }

    //아래를 String 대신에 Map을 보내보자
    //Map 보다는 Map과 같은 클래스를 만들어서 보내는 것을 지향한다
    @Test
    void passParamWithPost() {
        String blogName = "helloWrold";

        webTestClient.post()
                .uri("/helloworld")
                .body(Mono.just(blogName), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(new String(response.getResponseBody())).isEqualTo(blogName));

    }
}
