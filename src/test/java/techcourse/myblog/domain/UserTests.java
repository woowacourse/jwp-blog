package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserTests {
    Validator validator = Validation.byDefaultProvider().configure().buildValidatorFactory().getValidator();

    @Test
    void 올바른_User_생성_테스트() {
        String email = "aa@naver.com";
        String name = "zino";
        String password = "password";
        assertDoesNotThrow(() -> new User(email, name, password));
    }

    @Test
    void 올바르지_않은_email_User_생성() {
        String email = "aa";
        String name = "zino";
        String password = "password";

        assertThat(validator.validate(new User(email, name, password)).isEmpty()).isFalse();
    }

    @Test
    void 올바르지_않은_name_User_생성() {
        String email = "aa@naver.com";
        String name = "z";
        String password = "password";

        assertThat(validator.validate(new User(email, name, password)).isEmpty()).isFalse();
    }

    @Test
    void 올바르지_않은_password_User_생성() {
        String email = "aa@naver.com";
        String name = "zino";
        String password = "pass";

        assertThat(validator.validate(new User(email, name, password)).isEmpty()).isFalse();
    }

    @Test
    void 유저수정() {
        User firstUser = new User("kangmin789@naver.com", "kangmin", "asdASD12!@");
        User secondUser = new User("kangmin789@naver.com", "updated", "asdASD12");
        firstUser.modify(secondUser);
        assertThat(firstUser.getName()).isEqualTo(secondUser.getName());
        assertThat(firstUser.getPassword()).isEqualTo(secondUser.getPassword());
    }

    @Test
    void 패스워드일치() {
        User user = new User("kangmin789@naver.com", "kangmin", "asdASD12!@");
        assertThat(user.isMatchPassword("asdASD12!@")).isTrue();
        assertThat(user.isNotMatchEmail("kangmin@naver.com")).isTrue();
        assertThat(user.isMatchEmail("kangmin789@naver.com")).isTrue();
        assertThat(user.isNotMatchName("kangmin789")).isTrue();
    }
}
