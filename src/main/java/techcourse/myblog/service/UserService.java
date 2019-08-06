package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.controller.dto.LoginDTO;
import techcourse.myblog.controller.dto.UserDTO;
import techcourse.myblog.exception.EmailRepetitionException;
import techcourse.myblog.exception.LoginFailException;
import techcourse.myblog.exception.UserNotExistException;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDTO userDTO) {
        if (isExistEmail(userDTO)) {
            throw new EmailRepetitionException("이미 사용중인 이메일입니다.");
        }
        userRepository.save(new User(userDTO.getUserName(), userDTO.getEmail(), userDTO.getPassword()));
    }

    private boolean isExistEmail(UserDTO userDTO) {
        return userRepository.existsByEmail(userDTO.getEmail());
    }

    public User getLoginUser(LoginDTO loginDTO) {
        User findUser = checkUser(loginDTO);
        if (!findUser.authenticate(loginDTO.getPassword())) {
            throw new LoginFailException("아이디와 비밀번호가 일치하지 않습니다.");
        }
        return findUser;
    }

    private User checkUser(LoginDTO loginDTO) {
        return userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UserNotExistException("해당 아이디를 가진 유저는 존재하지 않습니다."));
    }

    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Transactional
    public User update(UserDTO userDTO) {
        User findUser = userRepository.findByEmail(userDTO.getEmail()).orElseThrow(() -> new UserNotExistException("유저 정보가 없습니다."));
        findUser.setUserName(userDTO.getUserName());
        findUser.setPassword(userDTO.getPassword());
        userRepository.save(findUser);

        return findUser;
    }

    @Transactional
    public void delete(User user) {
        User findUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UserNotExistException("유저 정보가 없습니다."));
        userRepository.delete(findUser);
    }
}
