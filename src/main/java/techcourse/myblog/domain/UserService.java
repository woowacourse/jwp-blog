package techcourse.myblog.domain;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import techcourse.myblog.domain.exception.NotFoundUserException;
import techcourse.myblog.domain.exception.NotMatchPasswordException;
import techcourse.myblog.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAll() {
        Iterable<User> iterable = userRepository.findAll();
        List<User> allUsers = new ArrayList<>();
        iterable.forEach(allUsers::add);
        return allUsers;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void update(User originalUser, User modifiedUser) {
        originalUser.modifyName(modifiedUser.getName());
        userRepository.save(originalUser);
    }

    public User login(UserDto userDto) {
        Optional<User> loginUser = userRepository.findByEmail(userDto.getEmail());

        if (!loginUser.isPresent()) {
            throw new NotFoundUserException("이메일을 확인해주세요.");
        }

        if (!loginUser.get().matchPassword(userDto.toUser())) {
            throw new NotMatchPasswordException("비밀번호를 확인해주세요.");
        }

        return loginUser.get();
    }

    public void delete(User user) {
        if (user != null && user.matchEmail(user)) {
            userRepository.delete(user);
        }
    }
}
