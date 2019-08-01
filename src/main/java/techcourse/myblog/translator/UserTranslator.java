package techcourse.myblog.translator;

import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;

public class UserTranslator implements ModelTranslator<User, UserDto> {
    @Override
    public User toEntity(final User user, final UserDto userDto) {
        Long id = processValue(user.getId(), userDto.getId());
        String name = processName(user.getName(), userDto.getName());
        String email = processValue(user.getEmail(), userDto.getEmail());
        String password = processValue(user.getPassword(), userDto.getPassword());

        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

    @Override
    public UserDto toDto(User user, UserDto userDto) {
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    private <T> T processValue(T domainValue, T dtoValue) {
        return domainValue != null ? domainValue : dtoValue;
    }

    private <T> T processName(T name, T updateName) {
        return updateName == null ? name : updateName;
    }
}
