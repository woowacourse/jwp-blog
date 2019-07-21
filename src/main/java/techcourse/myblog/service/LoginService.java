package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserException;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.repository.UserRepository;

@Service
public class LoginService {
    private static final String NOT_EXIST_USER = "등록된 이메일이 없습니다.";
    private static final String NOT_MATCH_PASSWORD = "비밀번호가 일치하지 않습니다.";

    private UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginDto loginByEmailAndPwd(UserRequestDto userRequestDto) {
        try {
            User user = userRepository.findByEmail(userRequestDto.getEmail())
                    .orElseThrow(() -> new UserException(NOT_EXIST_USER));
            if (user.isMatchPassword(userRequestDto)) {
                return new LoginDto(true, null, user.getName());
            }
            return new LoginDto(false, NOT_MATCH_PASSWORD);
        } catch (UserException e) {
            return new LoginDto(false, e.getMessage());
        }
    }
}
