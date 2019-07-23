package techcourse.myblog.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import techcourse.myblog.domain.exception.DuplicatedUserException;
import techcourse.myblog.domain.exception.NotFoundUserException;
import techcourse.myblog.domain.exception.NotMatchPasswordException;
import techcourse.myblog.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public List<User> findAll() {
        Iterable<User> iterable = userRepository.findAll();
        List<User> allUsers = new ArrayList<>();
        iterable.forEach(allUsers::add);
        return allUsers;
    }

    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()){
            throw new UnFoundUserException("존재하지 않는 유저입니다.");
        }
        return user.get();
    }

    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedUserException("이미 존재하는 email입니다.");
        }
    }

    @Transactional
    public User update(User originalUser, String newName) {
        User user = findByEmail(originalUser.getEmail());
        user.modifyName(newName);
        return user;
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
