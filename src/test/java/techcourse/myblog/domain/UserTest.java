package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.SignUpInputException;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Long testId = 1L;
    private String testName = "pkch";
    private String inValidName = "s";
    private String testEmail = "pkch@woowa.com";
    private String inValidEmail = "pkchwoowa.com";
    private String testPassword = "!234Qwer";
    private String inValidPassword = "1234";

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(testId)
                .name(testName)
                .email(testEmail)
                .password(testPassword)
                .build();
    }

    @Test
    void inValidNameTest() {
        assertThrows(SignUpInputException.class,
                () -> User.builder()
                        .id(testId)
                        .name(inValidName)
                        .email(testEmail)
                        .password(testPassword)
                        .build());
    }

    @Test
    void inValidEmailTest() {
        assertThrows(SignUpInputException.class,
                () -> User.builder()
                        .id(testId)
                        .name(testName)
                        .email(inValidEmail)
                        .password(testPassword)
                        .build());
    }

    @Test
    void inValidPasswordTest() {
        assertThrows(SignUpInputException.class,
                () -> User.builder()
                        .id(testId)
                        .name(testName)
                        .email(testEmail)
                        .password(inValidPassword)
                        .build());
    }

    @Test
    void 인증_이메일과_비밀번호가_일치할_때_테스트() {
        assertTrue(user.authenticate("pkch@woowa.com", testPassword));
    }

    @Test
    void 인증_비밀번호가_다를_때_테스트() {
        assertFalse(user.authenticate("pkch@woowa.com", "1@34Qwer"));
    }

    @Test
    void 인증_이메일이_다를_때_테스트() {
        assertFalse(user.authenticate("park@woowa.com", testPassword));
    }
}