package techcourse.myblog.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserRequestDto;

@Component
public class ToUser implements Converter<UserRequestDto, User> {

    private static final String EMPTY_USER_REQUEST_DTO = "'UserRequestDto'가 없습니다.";

    @Override
    public User convert(UserRequestDto source) {
        if (source == null) {
            throw new IllegalArgumentException(EMPTY_USER_REQUEST_DTO);
        }
        return new User(source.getName(), source.getPassword(), source.getEmail());
    }
}
