package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.List;

@Service
public class UserService {
    private static final String DUPLICATED_EMAIL = "이미 가입되어 있는 이메일 주소입니다. 다른 이메일 주소를 입력해 주세요.";
    private static final String WRONG_LOGIN = "잘못된 이메일 주소 또는 패스워드를 입력하셨습니다.";

    @Autowired
    private UserRepository userRepository;

    private boolean isDuplicatedEmail(final String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByEmail(final String email) {
        return userRepository
                .findByEmail(email).get();
    }

    public void signUp(final UserDto dto) {
        if (isDuplicatedEmail(dto.getEmail())) {
            throw new ValidationException(DUPLICATED_EMAIL);
        }
        final User user = new User(dto);
        userRepository.save(user);
    }

    public User login(final LoginDto dto) {
        return userRepository
                .findByEmailAndPassword(dto.getEmail(), dto.getPassword()).get();
    }

    @Transactional
    public User update(final Long id, final UserDto userDto) {
        final User persistUser = userRepository.findById(id).get();
        persistUser.setUsername(userDto.getUsername());
        return persistUser;
    }

    public void delete(final User user) {
        userRepository.delete(user);
    }
}
