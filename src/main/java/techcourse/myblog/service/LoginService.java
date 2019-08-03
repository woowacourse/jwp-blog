package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.LoginRequest;
import techcourse.myblog.dto.UserResponse;
import techcourse.myblog.exception.LoginException;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.utils.converter.DtoConverter;

@Service
public class LoginService {
    private static final String NOT_EXIST_USER = "등록된 이메일이 없습니다.";
    private static final String INCORRECT_PASSWORD = "비밀번호가 일치하지 않습니다.";

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse loginByEmailAndPwd(LoginRequest loginRequestDto) {
        User user = getUser(loginRequestDto);
        checkMatchPassword(user, loginRequestDto);

        return DtoConverter.convert(user);
    }

    private User getUser(LoginRequest loginRequestDto) {
        return userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new LoginException(NOT_EXIST_USER));
    }

    private void checkMatchPassword(User user, LoginRequest loginRequestDto) {
        if (!user.isMatchPassword(loginRequestDto.getPassword())) {
            throw new LoginException(INCORRECT_PASSWORD);
        }
    }
}
