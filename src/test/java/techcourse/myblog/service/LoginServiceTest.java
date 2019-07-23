package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.AuthenticationDto;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginService loginService;

    @Test
    void login() {
        User user = User.builder()
                .userName("Martin")
                .email("martin@gmail.com")
                .password("Aa12345!")
                .build();

        userRepository.save(user);
        AuthenticationDto authenticationDto = new AuthenticationDto("martin@gmail.com", "Aa12345!");
        User loginUser = loginService.login(authenticationDto);
        assertThat(loginUser.getUserName()).isEqualTo("Martin");
        assertThat(loginUser.getEmail()).isEqualTo("martin@gmail.com");
        assertThat(loginUser.getPassword()).isEqualTo("Aa12345!");
    }
}