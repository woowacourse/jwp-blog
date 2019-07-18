package techcourse.myblog.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private static final int NOT_FOUND_RESULT = 0;
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UserDto userDto) {
        validateEmail(userDto);

        User user = UserAssembler.writeUser(userDto);
        userRepository.save(user);
    }

    private void validateEmail(UserDto userDto) {
        if (userRepository.countByEmail(userDto.getEmail()) > NOT_FOUND_RESULT) {
            // TODO 예외 처리 클래스 찾아보기
            throw new DuplicateKeyException("중복되는 이메일입니다.");
        }
    }

    public List<UserDto> getAllUsers() {
        return UserAssembler.writeDtos(userRepository.findAll());
    }

    public User getUser(UserDto userDto) {
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        
        checkUserByEmail(email);
        checkPassword(email, password);
        
        return userRepository.findUserByEmailAndPassword(email, password);
    }

    private void checkUserByEmail(String email) {
        if (userRepository.countByEmail(email) == NOT_FOUND_RESULT) {
            throw new NoSuchElementException("존재하지 않는 이메일입니다.");
        }
    }

    private void checkPassword(String email, String password) {
        User user = userRepository.findUserByEmailAndPassword(email, password);
        if (user == null) {
            throw new NoSuchElementException("비밀번호가 일치하지 않습니다.");
        }
    }
}
