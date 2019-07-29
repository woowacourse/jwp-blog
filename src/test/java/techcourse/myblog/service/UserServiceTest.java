package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserFactory;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserProfileDto;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.exception.NotFoundUserException;
import techcourse.myblog.service.exception.SignUpException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {
    public static final String VALID_PASSWORD = "passWORD1!";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserDto testUserDto;
    private Long setUpArticleId;

    @BeforeEach
    void setUp() {
        testUserDto = new UserDto("name", "email@woowa.com", VALID_PASSWORD, VALID_PASSWORD);
        User testUser = UserFactory.generateUser(testUserDto);

        userRepository.save(testUser);
        setUpArticleId = testUser.getId();
    }

    @Test
    @DisplayName("새로운 User를 생성한다.")
    void saveUser() {
        UserDto userDto = new UserDto("new", "new@woowa.com", VALID_PASSWORD, VALID_PASSWORD);
        Long createdId = userService.save(userDto);

        User user = userRepository.findById(createdId).orElseThrow(NotFoundUserException::new);
        assertThat(user.getEmail()).isEqualTo("new@woowa.com");
    }

    @Test
    @DisplayName("이메일이 중복되는 경우에 예외를 던져준다.")
    void checkEmailDuplication() {
        UserDto duplicatedEmailUserDto = new UserDto(
                "name",
                "email@woowa.com",
                VALID_PASSWORD,
                VALID_PASSWORD
        );

        assertThatThrownBy(() -> userService.save(duplicatedEmailUserDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("User정보를 User id를 통해 찾는다.")
    void findByIdTest() {
        User foundUser = userRepository.findById(setUpArticleId)
                .orElseThrow(NotFoundUserException::new);

        assertThat(foundUser.getEmail()).isEqualTo(testUserDto.getEmail());
    }

    @Test
    @DisplayName("User 정보를 update한다.")
    void updateUser() {
        UserProfileDto updatedUserProfile = new UserProfileDto(
                setUpArticleId,
                "updated",
                "email@woowa.com"
        );

        userService.update(updatedUserProfile);
        User updatedUser = userRepository.findById(setUpArticleId)
                .orElseThrow(NotFoundUserException::new);

        assertThat(updatedUser.getName()).isEqualTo("updated");
    }

    @Test
    @DisplayName("id를 이용해 User를 삭제한다.")
    void deleteUser() {
        // When
        Long deletedId;
        testUserDto = new UserDto("deleted", "deleted@woowa.com", VALID_PASSWORD, VALID_PASSWORD);
        User testUser = UserFactory.generateUser(testUserDto);

        userRepository.save(testUser);
        deletedId = testUser.getId();

        // Then
        userService.deleteById(deletedId);

        // Given
        assertThatThrownBy(() -> userService.findById(deletedId))
                .isInstanceOf(NotFoundUserException.class);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteById(setUpArticleId);
    }
}