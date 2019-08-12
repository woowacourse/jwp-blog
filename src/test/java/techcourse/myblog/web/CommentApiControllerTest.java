package techcourse.myblog.web;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.CommentRequest;

import java.util.Objects;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
class CommentApiControllerTest {
    private static int FLAG_NO = 1;
    private User user;

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    private String cookie;

    private Long articleId;
    private Long commentId;
    private String commentContents;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .filter(documentationConfiguration(restDocumentation)
//                        .snippets().withTemplateFormat(TemplateFormats.markdown())
                ).build();

        // 회원가입
        user = new User("Jason", FLAG_NO++ + "jason@woowahan.com", "Jason12!@");
        webTestClient.post().uri("/users")
                .body(fromFormData("name", user.getName())
                        .with("email", user.getEmail())
                        .with("password", user.getPassword())
                        .with("reconfirmPassword", user.getPassword()))
                .exchange()
                .expectStatus()
                .isFound();
        commentContents = "test Contents";

        cookie = getCookie(user.getEmail());

        articleId = creatArticleAndReturnArticleId(cookie);
        commentId = creatArticleAndReturnCommentId();
    }

    private Long creatArticleAndReturnArticleId(String cookie) {
        return Long.parseLong(webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", "titleTest")
                        .with("coverUrl", "https://avatars2.githubusercontent.com/u/17171575?s=460&v=4")
                        .with("contents", "contentsTest"))
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isFound()
                .returnResult(String.class)
                .getResponseHeaders()
                .getLocation().getPath().split("/")[2]);
    }

    private Long creatArticleAndReturnCommentId() {
        CommentRequest commentRequest = new CommentRequest(articleId, commentContents);
        // 댓글작성
        byte[] responseBody = webTestClient.post().uri("/api/comments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header("Cookie", cookie)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.contents").isEqualTo(commentContents)
                .jsonPath("$.article.id").isEqualTo(articleId)
                .jsonPath("$.commenter.email").isEqualTo(user.getEmail())
                .returnResult()
                .getResponseBody();

        JsonElement result = new JsonParser().parse(new String(Objects.requireNonNull(responseBody)));
        return result.getAsJsonObject().get("id").getAsLong();
    }

    @Test
    void 특정_article의_댓글_가져오기() {
        댓글_작성();
        webTestClient.get().uri("/api/articles/{articleId}/comments", articleId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("comment/get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("articleId").description("조회할 article ID")
                        ),
                        responseFields(
                                fieldWithPath("comments[].id").description("댓글 고유 아이디"),
                                fieldWithPath("comments[].contents").description("댓글 내용"),
                                fieldWithPath("comments[].createdDate").description("댓글 생성 날짜"),
                                fieldWithPath("comments[].commenter.name").description("댓글 작성자의 이름"),
                                fieldWithPath("comments[].commenter.email").description("댓글 작성자의 이메일")
                        )));
    }

    @Test
    void 댓글_작성() {
        CommentRequest commentRequest = new CommentRequest(articleId, commentContents);

        webTestClient.post().uri("/api/comments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header("Cookie", cookie)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.contents").isEqualTo(commentContents)
                .jsonPath("$.article.id").isEqualTo(articleId)
                .jsonPath("$.commenter.email").isEqualTo(user.getEmail())
                .consumeWith(document("comment/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        getRequestFields(),
                        getResponseFields()));
    }

    private RequestFieldsSnippet getRequestFields() {
        return requestFields(
                fieldWithPath("articleId").description("댓글이 소속된 아리클의 고유 아이디"),
                fieldWithPath("contents").description("댓글이 소속된 아리클의 고유 아이디")
        );
    }

    private ResponseFieldsSnippet getResponseFields() {
        return responseFields(
                fieldWithPath("id").description("댓글 고유 아이디"),
                fieldWithPath("contents").description("댓글 내용"),
                fieldWithPath("createdDate").description("댓글 생성 날짜"),
                fieldWithPath("modifiedDate").description("댓글 수정 날짜"),
                fieldWithPath("commenter.id").description("댓글 작성자의 고유 아이디"),
                fieldWithPath("commenter.name").description("댓글 작성자의 이름"),
                fieldWithPath("commenter.email").description("댓글 작성자의 이메일"),
                fieldWithPath("commenter.image").description("댓글 작성자의 프사^^"),
                fieldWithPath("commenter.imageEncodeBase64").description("댓글 작성자의 프사를 Base64로 인코딩한 거~"),
                fieldWithPath("article.id").description("댓글이 소속된 아리클의 고유 아이디"),
                fieldWithPath("article.title").description("댓글이 소속된 아리클의 제목"),
                fieldWithPath("article.coverUrl").description("댓글이 소속된 아리클의 배경화면"),
                fieldWithPath("article.contents").description("댓글이 소속된 아리클의 내용"),
                fieldWithPath("article.createdDate").description("댓글이 소속된 아리클의 생성 날짜"),
                fieldWithPath("article.modifiedDate").description("댓글이 소속된 아리클의 수정 날짜")
        );
    }

    @Test
    void 댓글작성자_댓글수정() {
        CommentRequest commentRequest = new CommentRequest(articleId, "미스타꼬");

        webTestClient.put().uri("/api/comments/{commentId}", commentId)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.contents").isEqualTo(commentRequest.getContents())
                .consumeWith(document("comment/put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        getRequestFields(),
                        getResponseFields()
                ));
    }

    @Test
    void 댓글수정_작성자가_아닐_때_Index로_이동() {
        CommentRequest commentRequest = new CommentRequest(articleId, "updated comment");

        webTestClient.put().uri("/api/comments/{commentId}", commentId)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", ".*/;.*");
    }

    @Test
    void 댓글작성자_댓글삭제() {
        webTestClient.delete().uri("/api/comments/{commentId}", commentId)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(document("comment/delete",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("삭제할 comment의 ID")
                        )));
    }

    @Test
    void 작성자가_아닐_때_댓글삭제() {
        webTestClient.delete().uri("/api/comments/{commentId}", commentId)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/;.*");

    }

    private String getCookie(String email) {
        return webTestClient.post().uri("/login")
                .body(fromFormData("email", email)
                        .with("password", user.getPassword()))
                .exchange()
                .expectStatus()
                .isFound()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }
}
