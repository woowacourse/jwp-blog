package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private User user;
    private Long testId = 1L;
    private String testName = "pkch";
    private String testEmail = "pkch@woowa.com";
    private String testPassword = "qwerqwer";

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
    void 생성_테스트() {
        User testUser = User.builder()
                .id(testId)
                .name(testName)
                .email(testEmail)
                .password(testPassword)
                .build();
        assertThat(user).isEqualTo(testUser);
    }

    @Test
    void 인증_이메일과_비밀번호가_일치할_때_테스트() {
        assertTrue(user.authenticate("pkch@woowa.com", "qwerqwer"));
    }

    @Test
    void 인증_비밀번호가_다를_때_테스트() {
        assertFalse(user.authenticate("pkch@woowa.com", "12345678"));
    }

    @Test
    void 인증_이메일이_다를_때_테스트() {
        assertFalse(user.authenticate("park@woowa.com", "qwerqwer"));
    }
}