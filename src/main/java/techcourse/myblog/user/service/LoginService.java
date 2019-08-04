package techcourse.myblog.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.user.exception.LoginException;
import techcourse.myblog.user.repository.UserRepository;
import techcourse.myblog.utils.converter.UserConverter;

@Service
public class LoginService {
    private static final String NOT_EXIST_USER = "등록된 이메일이 없습니다.";
    private static final String INCORRECT_PASSWORD = "비밀번호가 일치하지 않습니다.";

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserResponseDto loginByEmailAndPwd(UserRequestDto userRequestDto) {
        User user = getUser(userRequestDto);
        checkMatchPassword(user, userRequestDto);

        return UserConverter.toResponseDto(user);
    }

    private User getUser(UserRequestDto userRequestDto) {
        return userRepository.findByEmail(userRequestDto.getEmail())
                .orElseThrow(() -> new LoginException(NOT_EXIST_USER));
    }

    private void checkMatchPassword(User user, UserRequestDto dto) {
        if (!user.isMatchPassword(dto)) {
            throw new LoginException(INCORRECT_PASSWORD);
        }
    }
}
