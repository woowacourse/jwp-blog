package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserSignUpRequestDto;
import techcourse.myblog.dto.UserUpdateRequestDto;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void create() {
        UserSignUpRequestDto userSignUpRequestDto = new UserSignUpRequestDto("Martin", "martin@gmail.com", "Aa12345!", "Aa12345!");
        userService.create(userSignUpRequestDto);
        assertTrue(userRepository.findByEmail("martin@gmail.com").isPresent());
    }

    @Test
    void update() {
        User user = saveUser();
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setUserName("NewName");

        User updatedUser = userService.update(userUpdateRequestDto, user.getUserId());
        assertThat(updatedUser.getUserName()).isEqualTo("NewName");
        assertThat(updatedUser.getEmail()).isEqualTo("martin@gmail.com");
        assertThat(updatedUser.getPassword()).isEqualTo("Aa12345!");
    }

    @Test
    void delete() {
        User user = saveUser();
        userService.delete(user.getUserId());

        assertFalse(userRepository.findByEmail("martin@gamil.com").isPresent());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    private User saveUser() {
        User user = User.builder()
                .userName("Martin")
                .email("martin@gmail.com")
                .password("Aa12345!")
                .build();
        return userRepository.save(user);
    }
}