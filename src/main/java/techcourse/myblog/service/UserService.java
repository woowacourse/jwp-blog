package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.exception.NotValidUpdateUserInfoException;
import techcourse.myblog.exception.NotValidUserInfoException;
import techcourse.myblog.exception.UnacceptablePathException;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.service.dto.UserUpdateRequestDto;
import techcourse.myblog.user.User;
import techcourse.myblog.user.UserRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createNewUser(UserDto userDto) {
        checkValidUserInformation(userDto);
        User user = userDto.toEntity();
        userRepository.save(user);
        log.info("새로운 {} 유저가 가입했습니다.", user.getUserName());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void checkRequestAboutMypage(HttpSession httpSession) {
        if (httpSession.getAttribute("user") == null) {
            throw new UnacceptablePathException();
        }
    }

    @Transactional
    public User updateUser(BindingResult bindingResult, HttpSession httpSession,
                           UserUpdateRequestDto userUpdateRequestDto) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            throw new NotValidUpdateUserInfoException(fieldError.getDefaultMessage());
        }
        String email = ((User) httpSession.getAttribute("user")).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundObjectException::new);
        user.changeUserName(userUpdateRequestDto.getUserName());
        return user;
    }

    public void deleteUser(HttpSession httpSession) {
        String email = ((User) httpSession.getAttribute("user")).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundObjectException::new);
        userRepository.delete(user);
    }

    private void checkValidUserInformation(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new NotValidUserInfoException("중복된 이메일 입니다.");
        }
    }


}
