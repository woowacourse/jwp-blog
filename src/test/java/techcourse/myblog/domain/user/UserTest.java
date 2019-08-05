package techcourse.myblog.domain.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {
    String rightName = "donut";
    String wrongName = "lfksjn slkgbsdkj asKLb zsdkjg;eg";
    String rightEmail = "donut@woowa.com";
    String wrongEmail = "donut.com@asd";
    String rightPassword = "qwer1234!";
    String wrongPassword = "666";

    @Test
    void validationNameTest() {
        assertThatThrownBy(() -> new User(wrongName, rightEmail, rightPassword));
        assertThat(new User(rightName, rightEmail, rightPassword));
    }

    @Test
    void validationEmailTest() {
        assertThatThrownBy(() -> new User(rightName, wrongEmail, rightPassword));
        assertThat(new User(rightName, rightEmail, rightPassword));
    }

    @Test
    void validationPasswordTest() {
        assertThatThrownBy(() -> new User(rightName, rightEmail, wrongPassword));
        assertThat(new User(rightName, rightEmail, rightPassword));
    }

    @Test
    void authenticationTest() {
        assertThat(new User(rightName, rightEmail, rightPassword).authenticate(rightPassword)).isTrue();
    }
}