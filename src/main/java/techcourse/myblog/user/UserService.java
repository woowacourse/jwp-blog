package techcourse.myblog.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.exception.NotValidUpdateUserInfoException;
import techcourse.myblog.exception.NotValidUserInfoException;
import techcourse.myblog.exception.UnacceptablePathException;
import techcourse.myblog.repo.UserRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createNewUser(BindingResult bindingResult, UserDto userDto) {
        checkValidUserInformation(bindingResult, userDto);
        User user = new User(userDto);
        userRepository.save(user);
        log.info("새로운 {} 유저가 가입했습니다.", user.getUserName());
    }

    private void checkValidUserInformation(BindingResult bindingResult, UserDto userDto) {
        if (bindingResult.hasErrors()) {
            throw new NotValidUserInfoException(bindingResult.getFieldError().getDefaultMessage());
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new NotValidUserInfoException("중복된 이메일 입니다.");
        }
    }


    public List<User> findAll() {
        Iterator<User> userIterator = userRepository.findAll().iterator();
        List<User> users = new ArrayList<>();
        userIterator.forEachRemaining(users::add);
        return users;
    }

    public void checkRequestAboutMypage(HttpSession httpSession) {
        if (httpSession.getAttribute("user") == null) {
            throw new UnacceptablePathException();
        }
    }

    @Transactional
    public void updateUser(BindingResult bindingResult, HttpSession httpSession,
                           UserUpdateRequestDto userUpdateRequestDto) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            throw new NotValidUpdateUserInfoException(fieldError.getDefaultMessage());
        }
        String email = ((User) httpSession.getAttribute("user")).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundObjectException::new);
        user.setUserName(userUpdateRequestDto.getUserName());
        httpSession.setAttribute("user", user);
    }

    @Transactional
    public void deleteUser(HttpSession httpSession) {
        String email = ((User) httpSession.getAttribute("user")).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundObjectException::new);
        httpSession.removeAttribute("user");
        userRepository.delete(user);
    }


}
