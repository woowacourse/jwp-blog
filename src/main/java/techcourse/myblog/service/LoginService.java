package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserEmail;
import techcourse.myblog.domain.UserException;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

@Service
public class LoginService {
    private static final String NOT_EXIST_USER = "등록된 이메일이 없습니다.";
    private static final String NOT_MATCH_PASSWORD = "비밀번호가 일치하지 않습니다.";

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserDto loginByEmailAndPwd(UserDto userDto) {
        User user = userRepository.findByEmail(UserEmail.of(userDto.getEmail()))
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
        if (!user.isMatchPassword(userDto)) {
            throw new UserException(NOT_MATCH_PASSWORD);
        }
        return new UserDto(user);
    }
}
