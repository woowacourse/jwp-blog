package techcourse.myblog.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserRequestDto;

@Component
public class DtoToUser implements Converter<UserRequestDto, User> {

    private static final String EMPTY_USER_REQUEST_DTO = "'UserRequestDto'가 없습니다.";

    @Override
    public User convert(UserRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException(EMPTY_USER_REQUEST_DTO);
        }
        return new User(dto.getName(), dto.getPassword(), dto.getEmail());
    }
}
