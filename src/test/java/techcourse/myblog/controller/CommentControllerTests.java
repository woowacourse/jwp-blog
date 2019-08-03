package techcourse.myblog.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.util.MultiValueMap;
import techcourse.myblog.MyblogApplicationTests;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentControllerTests extends MyblogApplicationTests {
    String cookie;

    @BeforeEach
    void setUp() {
        cookie = getLoginCookie(USER_EMAIL,USER_PASSWORD);
    }

    @Test
    @DisplayName("올바른 유저일 떄 코멘트 수정 페이지 접근한 뒤 원래 코멘트가 잘 나와 있고 수정까지 잘 되는 지 테스트")
    void comment_edit() {
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(COMMENT_CONTENTS)).isTrue();
        };

        getRequestWithCookieExpectStatus(HttpMethod.GET, "/comment/1/edit", cookie)
                .isOk()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);

        String updatedComment = "updatedComment";
        Consumer<EntityExchangeResult<byte[]>> secondEntityExchangeResultConsumer = (response) -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(updatedComment)).isTrue();
        };

        MultiValueMap<String, String> updateCommentMap = getCustomCommentDtoMap(updatedComment, 1);
        StatusAssertions statusAssertions = getResponseSpecWithCookieWithBody(HttpMethod.PUT, "/comment", cookie, updateCommentMap);
        String redirectUrl = getRedirectUrl(statusAssertions);
        getRequestWithCookieExpectStatus(HttpMethod.GET, redirectUrl, cookie)
                .isOk()
                .expectBody()
                .consumeWith(secondEntityExchangeResultConsumer);
    }

    @Test
    @DisplayName("올바른 유저일 때 코멘트 삭제 하고 잘 리다이렉트 되는 지 테스트")
    void delete_comment() {
        StatusAssertions statusAssertion = getRequestWithCookieExpectStatus(HttpMethod.DELETE, "/comment", cookie);
        String redirectUrl = getRedirectUrl(statusAssertion);
        getRequestWithCookieExpectStatus(HttpMethod.GET, redirectUrl, cookie)
                .isOk();
    }

}
