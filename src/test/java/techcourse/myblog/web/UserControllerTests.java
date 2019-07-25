package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTests {
    private static final Logger log = LoggerFactory.getLogger(UserControllerTests.class);

    private static final String LOCATION = "location";
    private static final String ROOT_REGEX_WITH_JSESSION_ID = "[^/]*//[^/]*/;[^/]*"; // 일단.. JSESSION_ID 가 존재할 때만을 위함
    private static final String ROOT_REGEX = "[^/]*//[^/]*/"; // 일단.. JSESSION_ID 가 존재할 때만을 위함

    private final String validName = "미스터코";
    private final String validPassword = "123123123";

    // static 아니면... 각자 복사본이 적용되는 듯...
    // 무엇 때문일까?
    private static final AtomicLong atomicLong = new AtomicLong(200l);

    @Autowired
    private WebTestClient webTestClient;

    // 현재는 추가한 id 를 알아내기 위한 용도
    @Autowired
    private UserService userService;

    @Test
    public void 회원가입페이지_이동() {
        webTestClient.get()
                .uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 회원추가() {
        String validEmail = newValidEmail();
        signupUser(validName, validEmail, validPassword)
                .expectStatus()
                .isFound();

        // 일단은 등록된 유저가 존재하는지 /users 에서 검색함 (... 유저가 많아질 경우 좋은 방법은 아님)
        // 유저의 입장에서 확인 할 수 있는 방법이 무엇이 있을까?
        assertThat(hasUser(validEmail)).isTrue();
    }

    @Test
    public void 로그인() {
        String validEmail = newValidEmail();
        signupUser(validName, validEmail, validPassword);

        webTestClient.post()
                .uri("/login")
                .body(BodyInserters
                        .fromFormData("email", validEmail)
                        .with("password", validPassword))
                .exchange()
                .expectStatus()
                .isFound();

        webTestClient.get()
                .uri("/users/1/mypage-edit")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 로그인_된_상태에서_로그인() {
        String validEmail = newValidEmail();
        signupUser(validName, validEmail, validPassword);

        webTestClient.post()
                .uri("/login")
                .header("Authorization", "Basic "
                        + Base64Utils.encodeToString((String.format("%s:%s", validEmail, validPassword).getBytes(UTF_8))))
                .body(BodyInserters
                        .fromFormData("email", validEmail)
                        .with("password", validPassword))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void 로그인_잘못된_비밀번호() {
        String validEmail = newValidEmail();
        signupUser(validName, validEmail, validPassword);

        webTestClient.post()
                .uri("/login")
                .body(BodyInserters
                        .fromFormData("email", validEmail)
                        .with("password", "wrongPassword"))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void 로그인_존재하지_않는_이메일() {
        String validEmail = newValidEmail();
        signupUser(validName, validEmail, validPassword);

        webTestClient.post()
                .uri("/login")
                .body(BodyInserters
                        .fromFormData("email", "wrong@email.com")
                        .with("password", validPassword))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void 로그인_세션이_등록되었는지() {
//        회원추가();
//        String email = "test@test.com";
//        String password = "123123123";
//
//        String[] data = new String[]{""};
//
//        ResponseSpec rs = webTestClient.post()
//                .uri("/login")
//                .body(BodyInserters
//                        .fromFormData("email", email)
//                        .with("password", password))
//                .exchange()
//                .expectStatus()
//                .isFound();
//
//        rs.expectHeader()
//                .value("set-cookie", (content) -> {
//                    data[0] = content;
//                });
//
//        System.out.println(data[0]);
    }

    @Test
    public void 로그인_실패_세션이_안_등록되었는지() {
//        // ?
//        회원로그아웃();
//
//        회원추가();
//        String email = "test@test.com";
//        String wrongPassword = "wrongPassword";
//
//        String[] data = new String[]{""};
//
//        ResponseSpec rs = webTestClient.post()
//                .uri("/login")
//                .body(BodyInserters
//                        .fromFormData("email", email)
//                        .with("password", wrongPassword))
//                .exchange();
//
//        rs.expectHeader()
//                .value("set-cookie", (content) -> {
//                    data[0] = content;
//
//                    // session id 로 ...만들어내서 내용을 비교해보아야하나??...
//                });
//
//        System.out.println("session exist?: " + data[0]);
    }

    @Test
    public void 회원로그아웃_세션_가졌니() {
        String[] data = new String[]{""};

        webTestClient.get()
                .uri("/logout")
                .exchange()
                .expectStatus().isFound()
                .expectHeader()
                .value("set-cookie", (content) -> {
                    data[0] = content;
                });

        System.out.println("session?: " + data[0]);
    }

    @Test
    public void 로그아웃_이후_메인페이지로이동(){
        log.debug("로그아웃 실행..!!");

        ResponseSpec rs = webTestClient.get()
                .uri("/logout")
                .exchange()
                .expectStatus().isFound();

        rs.expectHeader().valueMatches(LOCATION, )
    }

    @Test
    public void 회원조회페이지_이동() {
        signupUser(validName, newValidEmail(), validPassword);

        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 회원조회페이지_여러명추가_여러명등록되었는지() {
        List<String> validEmails = Arrays.asList(newValidEmail(), newValidEmail());

        validEmails.stream()
                .forEach(email -> signupUser(validName, email, validPassword));

        ResponseSpec rs = webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk();


        assertResponse(rs, response -> {
            String body = new String(response.getResponseBody());

            validEmails.stream()
                    .forEach(email -> assertThat(body.contains(email)).isTrue());
        });
    }

    @Test
    public void 유저수정페이지GET_로그인아닌상태_로그인페이지로리다이렉트() {
        String validEmail = newValidEmail();
        signupUser(validName, validEmail, validPassword);

        long id = userService.findIdBy(validEmail, validPassword).get();

        ResponseSpec rs = webTestClient.get()
                .uri(String.format("users/%d/mypage-edit", id))
                .exchange()
                .expectStatus()
                .isFound();


        rs.expectHeader().value(LOCATION, location -> {
            assertThat(location.contains("login"));
        });
    }

    @Test
    public void 유저수정페이지GET_로그인상태_isOk() {
        String validEmail = newValidEmail();
        signupUser(validName, validEmail, validPassword);

        long id = userService.findIdBy(validEmail, validPassword).get();


        ResponseSpec rs = webTestClient.get()
                .uri(String.format("users/%d/mypage-edit", id))
                .header("Authorization", "Basic "
                        + Base64Utils.encodeToString((String.format("%s:%s", validEmail, validPassword).getBytes(UTF_8))))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void 유저수정페이지PUT_로그인아닌상태_로그인페이지로리다이렉트() {
        String validEmail = newValidEmail();
        signupUser(validName, validEmail, validPassword);

        long id = userService.findIdBy(validEmail, validPassword).get();

        ResponseSpec rs = webTestClient.put()
                .uri(String.format("users/%d/mypage-edit", id))
                .exchange()
                .expectStatus()
                .isFound();


        rs.expectHeader().value(LOCATION, location -> {
            assertThat(location.contains("login"));
        });
    }

    @Test
    public void 유저수정페이지PUT_로그인상태_수정할유저와다른유저일때() {
        String validEmail = newValidEmail();
        signupUser(validName, validEmail, validPassword);

        long notLoggedInId = userService.findIdBy(validEmail, validPassword).get() + 1;

        ResponseSpec rs = webTestClient.put()
                .uri(String.format("users/%d/mypage-edit", notLoggedInId))
                .header("Authorization", "Basic "
                        + Base64Utils.encodeToString((String.format("%s:%s", validEmail, validPassword).getBytes(UTF_8))))
                .exchange()
                .expectStatus()
                .isFound();


        rs.expectHeader().valueMatches(LOCATION, ROOT_REGEX_WITH_JSESSION_ID);
    }

    // TODO: 테스트할때만 죽는다... ㅠ
    // org.springframework.transaction.TransactionSystemException: Could not commit JPA transaction; nested exception is javax.persistence.RollbackException: Error while committing the transaction
    @Test
    public void 유저수정페이지PUT_로그인상태_유저정보수정() {
        String validEmail = newValidEmail();
        signupUser(validName, validEmail, validPassword);
        String newName = "new name";

        long id = userService.findIdBy(validEmail, validPassword).get();

        String uri = String.format("users/%d/mypage-edit", id);
        ResponseSpec rs = webTestClient.put()
                .uri(String.format(uri))
                .header("Authorization", "Basic "
                        + Base64Utils.encodeToString((String.format("%s:%s", validEmail, validPassword).getBytes(UTF_8))))
                .body(BodyInserters
                        .fromFormData("name", newName))
                .exchange()
                .expectStatus()
                .isFound();


        rs.expectHeader().value(LOCATION, location ->
                location.contains(String.format("users/%d/mypage", id)));
    }

    @Test
    public void 유저수정페이지DELETE_로그인아닌상태_로그인페이지로리다이렉트() {
        String validEmail = newValidEmail();
        signupUser(validName, validEmail, validPassword);

        long id = userService.findIdBy(validEmail, validPassword).get();

        ResponseSpec rs = webTestClient.delete()
                .uri(String.format("users/%d/mypage-edit", id))
                .exchange()
                .expectStatus()
                .isFound();


        rs.expectHeader().value(LOCATION, location -> {
            assertThat(location.contains("login"));
        });
    }

    @Test
    public void 유저수정페이지DELETE_로그인상태_수정할유저와다른유저일때() {
        String validEmail = newValidEmail();
        signupUser(validName, validEmail, validPassword);

        long notLoggedInId = userService.findIdBy(validEmail, validPassword).get() + 1;

        ResponseSpec rs = webTestClient.delete()
                .uri(String.format("users/%d/mypage-edit", notLoggedInId))
                .header("Authorization", "Basic "
                        + Base64Utils.encodeToString((String.format("%s:%s", validEmail, validPassword).getBytes(UTF_8))))
                .exchange()
                .expectStatus()
                .isFound();

        rs.expectHeader().valueMatches(LOCATION, ROOT_REGEX_WITH_JSESSION_ID);
    }

    @Test
    public void 유저수정페이지DELETE_로그인상태_삭제() {
        String validEmail = newValidEmail();
        signupUser(validName, validEmail, validPassword);

        long notLoggedInId = userService.findIdBy(validEmail, validPassword).get() + 1;

        ResponseSpec rs = webTestClient.delete()
                .uri(String.format("users/%d/mypage-edit", notLoggedInId))
                .header("Authorization", "Basic "
                        + Base64Utils.encodeToString((String.format("%s:%s", validEmail, validPassword).getBytes(UTF_8))))
                .exchange()
                .expectStatus()
                .isFound();

        rs.expectHeader().valueMatches(LOCATION, ROOT_REGEX_WITH_JSESSION_ID);

        // TODO: 삭제를 어떻게 확인 할 까? (지금은 다시 로그인을 했을 때 실패하는 것으로 하려고 함)
        // 그러면 당연히.. 로그아웃도 되어있어야겠지
    }

    private boolean hasUser(String email) {
        boolean[] result = new boolean[]{false};

        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());

                    result[0] = body.contains(email);
                });

        return result[0];
    }

    private ResponseSpec signupUser(String name, String email, String password) {
        return webTestClient.post()
                .uri("/users")
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange();
    }

    private long newUniqueLong() {
        while (true) {
            long currentValue = atomicLong.get();
            long newValue = currentValue + 1;
            if (atomicLong.compareAndSet(currentValue, newValue)) {
                return currentValue;
            }
        }
    }

    private String newValidEmail() {
        String email = String.format("valid%d@test.com", newUniqueLong());
        log.info("email: {}", email);

        return email;
    }

    private void assertResponse(ResponseSpec rs, Consumer<EntityExchangeResult<byte[]>> assertBody) {
        rs.expectBody()
                .consumeWith(assertBody);
    }
}