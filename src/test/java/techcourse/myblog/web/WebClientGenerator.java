package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebClientGenerator {
    @Autowired
    private WebTestClient webTestClient;

    protected WebTestClient.ResponseSpec requestAndExpectStatus(HttpMethod method, String uri, HttpStatus status) {
        return requestAndExpectStatus(method, uri, new LinkedMultiValueMap<>(), status);
    }

    protected WebTestClient.ResponseSpec requestAndExpectStatus(HttpMethod method, String uri,
                                                                MultiValueMap<String, String> data, HttpStatus status) {
        return webTestClient.method(method)
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data))
                .exchange()
                .expectStatus()
                .isEqualTo(status);
    }

    protected WebTestClient.RequestHeadersSpec<?> request(HttpMethod method, String uri, MultiValueMap<String, String> data) {
        return webTestClient.method(method)
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data));
    }
}
