package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void replace_이름_SnsInfo_변경() {
        Long id = 10l;
        String name = "name";
        String email = "email";
        String password = "password";
        User user = User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .build();
        SnsInfo oldSnsInfo = SnsInfo.builder().email("previousEmail").build();

        String newName = "new name";
        SnsInfo newSnsInfo = SnsInfo.builder().email("newEmail").build();
        User expectedUser = User.builder()
                .id(id)
                .name(newName)
                .email(email)
                .password(password)
                .build();
        expectedUser.addSnsInfo(newSnsInfo);


        assertThat(user.replace(newName, Arrays.asList(newSnsInfo))).isEqualTo(expectedUser);
    }
}