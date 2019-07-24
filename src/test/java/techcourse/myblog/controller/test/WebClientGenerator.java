package techcourse.myblog.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebClientGenerator {
    @Autowired
    private WebTestClient webTestClient;

    protected WebTestClient.ResponseSpec responseSpec(HttpMethod method, String uri) {
        return responseSpec(method, uri, new LinkedMultiValueMap<>());
    }

    protected WebTestClient.ResponseSpec responseSpec(HttpMethod method, String uri,
                                                      MultiValueMap<String, String> data) {
        return webTestClient.method(method)
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data))
                .exchange();
    }

    protected WebTestClient.RequestHeadersSpec<?> requestCookie(HttpMethod method, String uri, MultiValueMap<String, String> data) {
        return webTestClient.method(method)
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data));
    }

    protected String responseBody(WebTestClient.ResponseSpec responseSpec) {
        return responseSpec
                .expectBody()
                .returnResult()
                .toString();
    }
    protected String getRedirectedUri(EntityExchangeResult<byte[]> response) {
        return response.getResponseHeaders().get("Location").get(0);
    }
}
