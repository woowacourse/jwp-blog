package techcourse.myblog.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.exception.UserArgumentException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static techcourse.myblog.service.UserServiceTest.VALID_PASSWORD;

class UserFactoryTest {
    @Test
    @DisplayName("이름이 2자 미만인 경우에 예외를 던져준다.")
    void underValidNameLength() {
        UserDto userDto = new UserDto("a", "email1@woowa.com",
                VALID_PASSWORD, VALID_PASSWORD);

        assertThatThrownBy(() -> UserFactory.generateUser(userDto))
                .isInstanceOf(UserArgumentException.class);
    }

    @Test
    @DisplayName("이름이 10자 초과인 경우에 예외를 던져준다.")
    void exceedValidNameLength() {
        UserDto userDto = new UserDto("abcdefghijk", "email2@woowa.com",
                VALID_PASSWORD, VALID_PASSWORD);

        assertThatThrownBy(() -> UserFactory.generateUser(userDto))
                .isInstanceOf(UserArgumentException.class);
    }

    @Test
    @DisplayName("이름이 숫자를 포함하는 경우에 예외를 던져준다.")
    void includeNumberInName() {
        UserDto userDto = new UserDto("abcde1", "email3@woowa.com",
                VALID_PASSWORD, VALID_PASSWORD);

        assertThatThrownBy(() -> UserFactory.generateUser(userDto))
                .isInstanceOf(UserArgumentException.class);
    }

    @Test
    @DisplayName("이름이 특수문자를 포함하는 경우에 예외를 던져준다.")
    void includeSpecialCharacterInName() {
        UserDto userDto = new UserDto("abcde!@", "email4@woowa.com",
                VALID_PASSWORD, VALID_PASSWORD);

        assertThatThrownBy(() -> UserFactory.generateUser(userDto))
                .isInstanceOf(UserArgumentException.class);
    }

    @Test
    @DisplayName("password가 8자 미만인 경우 예외를 던져준다.")
    void underValidPasswordLength() {
        UserDto userDto = new UserDto("abcde", "email5@woowa.com",
                "passwor", "passwor");

        assertThatThrownBy(() -> UserFactory.generateUser(userDto))
                .isInstanceOf(UserArgumentException.class);
    }

    @Test
    @DisplayName("password에 소문자가 포함되지 않으면 예외를 던져준다.")
    void checkUndercaseInPassword() {
        UserDto userDto = new UserDto("abcde", "email6@woowa.com",
                "PASSWORD1!", "PASSWORD1!");

        assertThatThrownBy(() -> UserFactory.generateUser(userDto))
                .isInstanceOf(UserArgumentException.class);
    }

    @Test
    @DisplayName("password에 대문자가 포함되지 않으면 예외를 던져준다.")
    void checkUppercaseInPassword() {
        UserDto userDto = new UserDto("abcde", "email7@woowa.com",
                "password1!", "password1!");

        assertThatThrownBy(() -> UserFactory.generateUser(userDto))
                .isInstanceOf(UserArgumentException.class);
    }

    @Test
    @DisplayName("password에 숫자가 포함되지 않으면 예외를 던져준다.")
    void checkNumberInPassword() {
        UserDto userDto = new UserDto("abcde", "email8@woowa.com",
                "passWORD!", "passWORD!");

        assertThatThrownBy(() -> UserFactory.generateUser(userDto))
                .isInstanceOf(UserArgumentException.class);
    }

    @Test
    @DisplayName("password에 특수문자가 포함되지 않으면 예외를 던져준다.")
    void checkSpecialCharacterInPassword() {
        UserDto userDto = new UserDto("abcde", "email9@woowa.com",
                "passWORD1", "passWORD1");

        assertThatThrownBy(() -> UserFactory.generateUser(userDto))
                .isInstanceOf(UserArgumentException.class);
    }

    @Test
    @DisplayName("password에 한글이 포함되면 예외를 던져준다.")
    void checkPasswordDoesNotContainsKorean() {
        UserDto userDto = new UserDto("abcde", "email10@woowa.com",
                "passWORD가1!", "passWORD가1!");

        assertThatThrownBy(() -> UserFactory.generateUser(userDto))
                .isInstanceOf(UserArgumentException.class);
    }
}