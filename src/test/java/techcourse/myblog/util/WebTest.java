package techcourse.myblog.util;

import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;

import static techcourse.myblog.user.UserDataForTest.EMPTY_COOKIE;

public class WebTest {

    public static WebTestClient.ResponseSpec executeGetTest(
            WebTestClient webTestClient, String uri, String cookie) {

        return webTestClient.get()
                .uri(uri)
                .header("Cookie", cookie)
                .exchange();
    }

    public static WebTestClient.ResponseSpec executePostTest(
            WebTestClient webTestClient, String uri, String cookie, BodyInserter<?, ? super ClientHttpRequest> inserter) {

        return webTestClient.post()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(inserter)
                .exchange();
    }

    public static WebTestClient.ResponseSpec executePutTest(
            WebTestClient webTestClient, String uri, String cookie, BodyInserter<?, ? super ClientHttpRequest> inserter) {

        return webTestClient.put()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(inserter)
                .exchange();
    }

    public static WebTestClient.ResponseSpec executeDeleteTest(
            WebTestClient webTestClient, String uri, String cookie) {

        return webTestClient.delete()
                .uri(uri)
                .header("Cookie", cookie)
                .exchange();
    }

    public static WebTestClient.RequestBodySpec executePostTestWithJson(
            WebTestClient webTestClient, String uri, String cookie) {

        return webTestClient.post().uri( uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8);
    }
}
