package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.CommentService;

import static org.mockito.BDDMockito.given;
import static techcourse.myblog.web.AuthControllerTest.로그인_세션_ID;
import static techcourse.myblog.web.UserControllerTest.회원_등록;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {
    private static final String TEST_LOGIN_ID = "pkch@woowa.com";
    private static final String TEST_LOGIN_PASSWORD = "qwerqwer";
    private static final String TEST_REQUEST_URI = "/articles/1/comments";
    private static final String TEST_REDIRECT_URI = ".*/articles/1";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CommentService commentService;
    private String jSessionId;

    @BeforeEach
    void setUp() {
        UserDto testUserDto = new UserDto();
        testUserDto.setName("pkch");
        testUserDto.setEmail(TEST_LOGIN_ID);
        testUserDto.setPassword(TEST_LOGIN_PASSWORD);
        회원_등록(webTestClient, testUserDto);
        jSessionId = 로그인_세션_ID(webTestClient, TEST_LOGIN_ID, TEST_LOGIN_PASSWORD);
    }

    @Test
    void commentCreateTest() {
        given(commentService.create(1L, null, "hello")).willReturn(new Comment());

        webTestClient.post().uri(TEST_REQUEST_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("cookie", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", TEST_REDIRECT_URI);
    }

    @Test
    void commentUpdateTest() {
        webTestClient.put().uri(TEST_REQUEST_URI + "/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("cookie", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", TEST_REDIRECT_URI);
    }

    @Test
    void commentDeleteTest() {
        webTestClient.delete().uri(TEST_REQUEST_URI + "/1")
                .header("cookie", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", TEST_REDIRECT_URI);
    }
}
