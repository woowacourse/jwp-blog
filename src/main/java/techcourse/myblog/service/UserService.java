package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    public void save(UserDTO userDTO){
        if (isDuplicateEmail(userDTO)){
            throw new EmailRepetitionException("이메일이 중복입니다.");
        }
        userRepository.save(new User(userDTO.getUserName(), userDTO.getEmail(), userDTO.getPassword()));
    }

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

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public boolean isDuplicateEmail(UserDTO userDTO) {
        return userRepository.findUserByEmailAddress(userDTO.getEmail()) != null;
    }

    public void delete(String email){
        userRepository.deleteUserByEmailAddress(email);
    }

    public User update(UserDTO userDTO) {
        log.error("email {} ", userDTO.getEmail());
        log.error("name {} ", userDTO.getUserName());
        log.error("password {} ", userDTO.getPassword());
        int result = userRepository.updateUserByEmailAddress(userDTO.getUserName(), userDTO.getPassword(), userDTO.getEmail());
        if(result == 0){
            throw new UserNotExistException("유저정보가 없습니다.");
        }
        return userRepository.findUserByEmailAddress(userDTO.getEmail());
    }
}
