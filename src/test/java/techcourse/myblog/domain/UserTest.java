package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("abc", "A!1bcdefg", "abc@abc.com");
    }

    @Test
    public void updateNameAndEmail() {
        user.updateNameAndEmail("cba", "cba@cba.com");
        assertThat(user.getName()).isEqualTo("cba");
        assertThat(user.getEmail()).isEqualTo("cba@cba.com");
    }

    @Test
    public void isMatchPassword() {
        assertThat(user.isMatchPassword("A!1bcdefg")).isTrue();
    }
}