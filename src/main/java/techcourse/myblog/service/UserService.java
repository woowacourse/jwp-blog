package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserFactory;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserProfileDto;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.exception.NotFoundUserException;
import techcourse.myblog.service.exception.SignUpException;
import techcourse.myblog.service.exception.UserArgumentException;
import techcourse.myblog.service.exception.UserUpdateException;

import javax.transaction.Transactional;
import java.util.List;

import static techcourse.myblog.service.exception.UserArgumentException.EMAIL_DUPLICATION_MESSAGE;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Long save(UserDto userDto) {
        try {
            checkDuplicatedEmail(userDto.getEmail());
            User user = UserFactory.generateUser(userDto);
            userRepository.save(user);
            return user.getId();
        } catch (Exception e) {
            throw new SignUpException(e.getMessage());
        }
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(NotFoundUserException::new);
    }

    private void checkDuplicatedEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserArgumentException(EMAIL_DUPLICATION_MESSAGE);
        }
    }

    @Transactional
    public Long update(UserProfileDto userProfileDto) {
        try {
            User user = userRepository.findByEmail(userProfileDto.getEmail())
                    .orElseThrow(NotFoundUserException::new);
            user.updateByUserProfileDto(userProfileDto);
            return user.getId();
        } catch (Exception e) {
            throw new UserUpdateException(e.getMessage());
        }
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
