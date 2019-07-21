package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void index() {
        httpRequestAndExpectStatus(GET, "/", OK);
    }

    private WebTestClient.ResponseSpec httpRequestAndExpectStatus(HttpMethod method, String uri, HttpStatus status) {
        return httpRequestAndExpectStatus(method, uri, null, status);
    }

    private WebTestClient.ResponseSpec httpRequestAndExpectStatus(HttpMethod method, String uri,
                                                                  BodyInserters.FormInserter<String> form, HttpStatus status) {
        return webTestClient.method(method)
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .exchange()
                .expectStatus().isEqualTo(status);
    }
}