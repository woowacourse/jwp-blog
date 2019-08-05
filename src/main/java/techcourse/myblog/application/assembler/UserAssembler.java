package techcourse.myblog.application.assembler;

import techcourse.myblog.application.dto.UserResponseDto;
import techcourse.myblog.domain.User;

public class UserAssembler implements Assembler<User, UserResponseDto> {
    private static UserAssembler assembler = new UserAssembler();

    private UserAssembler() {
    }

    public static UserAssembler getInstance() {
        return assembler;
    }

    @Override
    public UserResponseDto convertEntityToDto(User entity) {
        return new UserResponseDto(entity.getId(), entity.getEmail(), entity.getName());
    }
}