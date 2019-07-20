package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import static org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec;
import static org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

public class ControllerTestTemplate {
    private static final String LOCATION = "location";

    @Autowired
    private WebTestClient webTestClient;

    protected RequestBodySpec request(HttpMethod method, String uri) {
        return webTestClient.method(method)
                .uri(uri);
    }

    protected ResponseSpec response(HttpMethod method, String uri) {
        return request(method, uri)
                .exchange();
    }

    protected ResponseSpec responseWithData(HttpMethod method, String uri, MultiValueMap<String, String> data) {
        return request(method, uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data))
                .exchange();
    }

    protected ResponseSpec checkStatus(HttpMethod method, String uri, HttpStatus status) {
        return response(method, uri)
                .expectStatus().isEqualTo(status);
    }

    protected ResponseSpec checkStatusWithData(ResponseSpec responseSpec, HttpStatus status) {
        return responseSpec
                .expectStatus().isEqualTo(status);
    }

    protected ResponseSpec checkStatusAndHeaderLocation(ResponseSpec responseSpec, HttpStatus status, String matcher) {
        return checkStatusWithData(responseSpec, status)
                .expectHeader().valueMatches(LOCATION, matcher);
    }
}
