package techcourse.myblog.user;

import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;

public class UserDataForTest {
    public static final String EMPTY_COOKIE = "";

    public static final String USER_EMAIL = "email@gmail.com";
    public static final String USER_PASSWORD = "password1234!";
    public static final String USER_NAME = "name";

    public static final String NEW_USER_EMAIL = "newemail@gmail.com";
    public static final String NEW_USER_PASSWORD = "password1234!";
    public static final String NEW_USER_NAME = "newName";

    public static final String UPDATED_USER_NAME = "updated";

    public static final BodyInserter<?, ? super ClientHttpRequest> NEW_USER_BODY =
            BodyInserters
                    .fromFormData("email", UserDataForTest.NEW_USER_EMAIL)
                    .with("password", UserDataForTest.NEW_USER_PASSWORD)
                    .with("name", UserDataForTest.NEW_USER_NAME);

    public static final BodyInserter<?, ? super ClientHttpRequest> LOGIN_BODY = BodyInserters
            .fromFormData("email", UserDataForTest.USER_EMAIL)
            .with("password", UserDataForTest.USER_PASSWORD);
}
