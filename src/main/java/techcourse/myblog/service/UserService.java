package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDto userDto) {
        checkDuplicatedEmail(userDto.getEmail());
        userRepository.save(userDto.toEntity());
    }

    private void checkDuplicatedEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            throw new SignUpException(SignUpException.EMAIL_DUPLICATION_MESSAGE);
        }
    }
}
