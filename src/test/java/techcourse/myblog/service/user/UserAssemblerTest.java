package techcourse.myblog.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.user.UserRequestDto;
import techcourse.myblog.service.dto.user.UserResponseDto;

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
        UserRequestDto userRequestDto = new UserRequestDto("john123@example.com", "john", "p@ssW0rd");
        assertThat(convertToEntity(userRequestDto)).isEqualTo(user);
    }

    @Test
    void entity를_dto로_변환하는지_확인() {
        UserResponseDto userResponseDto = new UserResponseDto(1L, "john123@example.com", "john");
        UserResponseDto convertToDto = convertToDto(user);
        assertThat(convertToDto.getEmail()).isEqualTo(userResponseDto.getEmail());
        assertThat(convertToDto.getName()).isEqualTo(userResponseDto.getName());
    }
}
