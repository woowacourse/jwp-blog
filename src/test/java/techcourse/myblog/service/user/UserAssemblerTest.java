package techcourse.myblog.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.user.UserRequest;
import techcourse.myblog.service.dto.user.UserResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.service.user.UserAssembler.convertToDto;
import static techcourse.myblog.service.user.UserAssembler.convertToEntity;

public class UserAssemblerTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("john123@example.com", "john", "p@ssW0rd");
    }

    @Test
    void dto를_entity로_변환하는지_확인() {
        UserRequest userRequest = new UserRequest("john123@example.com", "john", "p@ssW0rd");
        assertThat(convertToEntity(userRequest)).isEqualTo(user);
    }

    @Test
    void entity를_dto로_변환하는지_확인() {
        UserResponse userResponse = new UserResponse(1L, "john123@example.com", "john");
        UserResponse convertToDto = convertToDto(user);
        assertThat(convertToDto.getEmail()).isEqualTo(userResponse.getEmail());
        assertThat(convertToDto.getName()).isEqualTo(userResponse.getName());
    }
}
