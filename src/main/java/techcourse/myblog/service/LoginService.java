package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserException;
import techcourse.myblog.dto.LoginDto;
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
    public User loginByEmailAndPwd(LoginDto loginDto) {
        User user = userRepository.findByEmailEmail(loginDto.getEmail())
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
        if (!user.isMatchPassword(loginDto.getPassword())) {
            throw new UserException(NOT_MATCH_PASSWORD);
        }
        return user;
    }
}
