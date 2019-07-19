package techcourse.myblog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.application.service.exception.DuplicatedIdException;
import techcourse.myblog.application.service.exception.NotExistIdException;
import techcourse.myblog.application.service.exception.NotMatchPasswordException;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public String save(UserDto userDto) {
        User user = new User(userDto.getEmail(), userDto.getName(), userDto.getPassword());

        if(userRepository.findById(userDto.getEmail()).isPresent()){
            throw new DuplicatedIdException("이미 사용중인 이메일입니다.");
        }
        return userRepository.save(user).getEmail();
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<UserDto> userDtos = new ArrayList<>();
        userRepository.findAll().forEach(user -> userDtos.add(new UserDto(user)));

        return userDtos;
    }

    @Valid
    @Transactional(readOnly = true)
    public UserDto findById(String email) {
        return new UserDto(userRepository.findById(email)
                .orElseThrow(() -> new NotExistIdException("해당 이메일의 유저가 존재하지 않습니다.", "/login")));
    }

    @Transactional
    public void login(LoginDto loginDto) {
        String requestPassword = loginDto.getPassword();
        String expectedPassword = findById(loginDto.getEmail()).getPassword();

        if(!requestPassword.equals(expectedPassword)){
            throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    public void modify(@Valid UserDto userDto) {
        User user = userRepository.findById(userDto.getEmail())
                .orElseThrow(() -> new NotExistIdException("해당 이메일의 유저가 존재하지 않습니다.", "/"));
        user.updateName(userDto.getName());
        user.updatePassword(userDto.getPassword());
    }

    @Transactional
    public void remove(String email) {
        userRepository.deleteById(email);
    }
}
