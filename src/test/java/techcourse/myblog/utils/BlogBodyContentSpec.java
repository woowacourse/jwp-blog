package techcourse.myblog.utils;

import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

public class BlogBodyContentSpec {
    private WebTestClient.BodyContentSpec bodyContentSpec;

    private BlogBodyContentSpec(WebTestClient.BodyContentSpec bodyContentSpec) {
        this.bodyContentSpec = bodyContentSpec;
    }

    public static BlogBodyContentSpec assertThatBodyOf(WebTestClient.ResponseSpec responseSpec) {
        WebTestClient.BodyContentSpec bodyContentSpec = responseSpec.expectBody();
        return new BlogBodyContentSpec(bodyContentSpec);
    }

    public void contains(String... contents) {
        bodyContentSpec.consumeWith(response -> {
            String body = Utils.getResponseBody(response.getResponseBody());
            assertThat(body).contains(contents);
        });
    }
}
