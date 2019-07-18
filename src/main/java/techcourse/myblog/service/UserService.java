package techcourse.myblog.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
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
        List<User> users = userRepository.findUsersByEmail(userDto.getEmail());
        if (users.size() > 0) {
            // TODO 예외 처리 클래스 찾아보기
            throw new DuplicateKeyException("중복되는 이메일입니다.");
        }
    }

    public List<UserDto> getAllUsers() {
        return UserAssembler.writeDtos(userRepository.findAll());
    }
}
