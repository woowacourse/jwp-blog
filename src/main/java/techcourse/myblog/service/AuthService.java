package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.NotExistUserException;
import techcourse.myblog.exception.NotMatchAuthenticationException;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.translator.ModelTranslator;
import techcourse.myblog.translator.UserTranslator;

import javax.transaction.Transactional;

@Service
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final ModelTranslator<User, UserDto> userTranslator;

    public AuthService(final UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userTranslator = new UserTranslator();
    }

    public UserDto login(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new NotExistUserException("해당 이메일로 가입한 유저가 없습니다."));

        if (user.authenticate(userDto.getEmail(), userDto.getPassword())) {
            return userTranslator.toDto(user, new UserDto());
        }

        throw new NotMatchAuthenticationException("인증 정보가 일치하지 않습니다.");
    }
}
