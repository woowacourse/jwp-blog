package techcourse.myblog.web;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersSpec;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTestTemplate {
    @Autowired
    private WebTestClient webTestClient;

    protected StatusAssertions requestExpect(RequestHeadersSpec requestHeadersSpec) {
        return requestHeadersSpec
                .exchange()
                .expectStatus();
    }

    protected StatusAssertions requestExpect(HttpMethod method, String uri) {
        return requestExpect(makeRequestSpec(method, uri));
    }

    protected StatusAssertions requestExpect(HttpMethod method, String uri,
                                             MultiValueMap<String, String> data) {
        return requestExpect(makeRequestSpec(method, uri, data));
    }

    protected WebTestClient.RequestBodySpec makeRequestSpec(HttpMethod method, String uri) {
        return webTestClient.method(method).uri(uri);
    }

    protected RequestHeadersSpec<?> makeRequestSpec(HttpMethod method, String uri,
                                                    MultiValueMap<String, String> data) {
        return makeRequestSpec(method, uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data));
    }

    protected void bodyCheck(WebTestClient.ResponseSpec responseSpec, List<String> contents) {
        responseSpec
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    contents.forEach(c -> assertThat(body).contains(c));
                });
    }

    protected String getRedirectedUri(EntityExchangeResult<byte[]> response) {
        return response.getResponseHeaders().get("Location").get(0);
    }

}
