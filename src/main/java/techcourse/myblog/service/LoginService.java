package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.controller.dto.LoginDTO;
import techcourse.myblog.exception.LoginFailException;
import techcourse.myblog.exception.UserNotExistException;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.UserRepository;

@Service
public class LoginService {
    private UserRepository userRepository;
    @Autowired
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    ;

    public User getLoginUser(LoginDTO loginDTO) {
        User findUser = checkUser(loginDTO);
        if (checkPassword(loginDTO.getPassword(), findUser.getPassword())) {
            return findUser;
        }
        throw new LoginFailException("아이디와 비밀번호가 일치하지 않습니다.");
    }

    private boolean checkPassword(String password, String checkPassword) {
        return password.equals(checkPassword);
    }

    private User checkUser(LoginDTO loginDTO) {
        User findUser = userRepository.findUserByEmailAddress(loginDTO.getEmail());
        if (findUser != null) {
            return findUser;
        }
        throw new UserNotExistException("해당 아이디를 가진 유저는 존재하지 않습니다.");
    }
}
