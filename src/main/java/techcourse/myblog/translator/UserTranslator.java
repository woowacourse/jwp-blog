package techcourse.myblog.translator;

import org.springframework.stereotype.Component;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;

@Component
public class UserTranslator implements ModelTranslator<User, UserDto>{
    @Override
    public User toEntity(User user, final UserDto userDto) {
        Long id = processValue(user.getId(), userDto.getId());
        String name = processValue(user.getName(), userDto.getName());
        String email = processValue(user.getEmail(), userDto.getEmail());
        String password = processValue(user.getPassword(), userDto.getPassword());

        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

    private <T> T processValue(T domainValue, T dtoValue) {
        return domainValue != null ? domainValue : dtoValue;
    }
}
