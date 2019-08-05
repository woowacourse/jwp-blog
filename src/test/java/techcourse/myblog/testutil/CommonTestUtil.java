package techcourse.myblog.testutil;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;

public class CommonTestUtil {

    private final WebTestClient webTestClient;

    public CommonTestUtil(final WebTestClient webTestClient){
        this.webTestClient=webTestClient;
    }

    public WebTestClient.ResponseSpec responseSpec(String method, String uri, BodyInserter bodyInserter, String jSessionId){
        if(method.equals("post")){
            return postResponseSpec(uri,bodyInserter,jSessionId);
        }
        return putResponseSpec(uri,bodyInserter,jSessionId);
    }

    private WebTestClient.ResponseSpec putResponseSpec(String uri, BodyInserter bodyInserter, String jSessionId) {
        return webTestClient.put().uri(uri)
                .body(bodyInserter)
                .cookie("JSESSIONID",jSessionId)
                .exchange();
    }

    private WebTestClient.ResponseSpec postResponseSpec(String uri, BodyInserter bodyInserter, String jSessionId) {
        return webTestClient.post().uri(uri)
                .body(bodyInserter)
                .cookie("JSESSIONID",jSessionId)
                .exchange();
    }
}
