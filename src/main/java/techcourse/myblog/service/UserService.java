package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.persistence.UserRepository;
import techcourse.myblog.service.dto.UserRequestDto;
import techcourse.myblog.service.exception.ArticleNotFoundException;
import techcourse.myblog.service.exception.AuthenticationFailException;

import javax.transaction.Transactional;

@Service
public class UserService {
    public final static String LOGGED_IN_USER_SESSION_KEY = "user";
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void update(String email, UserRequestDto userRequestDto) {
        User user = userRepository.findByEmail(email).orElseThrow(ArticleNotFoundException::new);
        user.update(userRequestDto);
    }

    public User authenticate(UserRequestDto userRequestDto) {
        User user = userRepository.findByEmail(userRequestDto.getEmail()).orElseThrow(AuthenticationFailException::new);
        if (user.isMatch(userRequestDto)) {
            return user;
        }
        throw new AuthenticationFailException();
    }
}
