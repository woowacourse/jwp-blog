package techcourse.core.test;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

public class TsWebClientTemplate {
    private final WebTestClient webTestClient;
    private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    private TsWebClientTemplate(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public TsWebClientTemplate addParam(String key, String value) {
        this.params.add(key, value);
        return this;
    }

    public WebTestClient.ResponseSpec formPost(String url) {
        return webTestClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(params))
                .exchange();
    }

    public static TsWebClientTemplate urlEncodedForm(WebTestClient webTestClient) {
        return new TsWebClientTemplate(webTestClient);
    }
}
