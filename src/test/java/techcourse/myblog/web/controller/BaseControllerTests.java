package techcourse.myblog.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

public abstract class BaseControllerTests {
    protected static final String EMAIL_KEY = "email";
    protected static final String USER_NAME_KEY = "name";
    protected static final String PASSWORD_KEY = "password";
    protected static final String CONFIRM_PASSWORD_KEY = "confirmPassword";
    protected static final String JSESSIONID = "JSESSIONID";

    @Autowired
    WebTestClient webTestClient;

    private String id;
    private final String email = "email@gmail.com";
    private final String name = "name";
    private final String password = "P@ssw0rd";

    /**
     * 회원 가입
     *
     * @param name
     * @param email
     * @param password
     * @return @Id
     */
    protected String addUser(String name, String email, String password) {
        id = webTestClient.post().uri("/users")
                .body(fromFormData(USER_NAME_KEY, name)
                        .with(EMAIL_KEY, email)
                        .with(PASSWORD_KEY, password)
                        .with(CONFIRM_PASSWORD_KEY, password))
                .exchange()
                .returnResult(String.class)
                .getResponseBody().blockFirst();
        return id;
    }

    /**
     * addUser 생략하고 JSessionID 얻을 때 사용
     *
     * @return JSessionId
     */
    protected String getJSessionId() {
        addUser(name, email, password);

        return getJSessionId(email, password);
    }

    /**
     * 직접 addUser 하고 JSessionId 얻을 때 사용
     *
     * @param email
     * @param password
     * @return JSessionId
     */
    protected String getJSessionId(String email, String password) {
        return webTestClient.post().uri("/users/login")
                .body(fromFormData(EMAIL_KEY, email)
                        .with(PASSWORD_KEY, password))
                .exchange()
                .expectBody()
                .returnResult()
                .getResponseCookies().get(JSESSIONID).get(0).getValue();
    }

    protected void deleteUser(String id, String email, String password) {
        webTestClient.delete().uri("/users/{id}", id)
                .cookie(JSESSIONID, getJSessionId(email, password))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/");
    }

    protected void deleteUser() {
        deleteUser(id, email, password);
    }
}
