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
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User update(String email, UserRequestDto userRequestDto) {
        User user = userRepository.findByEmail(email).orElseThrow(ArticleNotFoundException::new);
        user.update(userRequestDto);

        return user;
    }
}
