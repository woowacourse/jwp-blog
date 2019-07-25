package techcourse.myblog.web.common;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersSpec;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTestTemplate {
    @Autowired
    private WebTestClient webTestClient;

    protected StatusAssertions httpRequest(RequestHeadersSpec requestHeadersSpec) {
        return requestHeadersSpec
                .exchange()
                .expectStatus();
    }

    protected StatusAssertions httpRequest(HttpMethod method, String uri) {
        return httpRequest(makeRequestSpec(method, uri));
    }

    protected StatusAssertions httpRequest(HttpMethod method, String uri,
                                           MultiValueMap<String, String> data) {
        return httpRequest(makeRequestSpec(method, uri, data));
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

    protected String requestAndGetBody(HttpMethod method, String redirectUrl) {
        return new String(httpRequest(method, redirectUrl)
                .isOk()
                .expectBody()
                .returnResult()
                .getResponseBody());
    }

    protected String requestAndGetRedirectUrl(HttpMethod method, String path, MultiValueMap<String, String> data) {
        return httpRequest(method, path, data)
                .isFound()
                .expectBody()
                .returnResult()
                .getResponseHeaders()
                .get("Location").get(0);
    }
}
