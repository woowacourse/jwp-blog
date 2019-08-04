package techcourse.myblog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.converter.UserConverter;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.application.service.exception.DuplicatedIdException;
import techcourse.myblog.application.service.exception.NotExistUserIdException;
import techcourse.myblog.application.service.exception.NotMatchEmailException;
import techcourse.myblog.application.service.exception.NotMatchPasswordException;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import javax.validation.Valid;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private UserConverter userConverter;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        userConverter = UserConverter.getInstance();
    }

    @Transactional
    public UserDto save(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicatedIdException("이미 사용중인 이메일입니다.");
        }

        User user = userConverter.convertFromDto(userDto);
        return userConverter.convertFromEntity(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userConverter.createFromEntities(userRepository.findAll());
    }

    public UserDto findByEmail(String email) {
        return userConverter.convertFromEntity(findUserByEmail(email));
    }

    @Transactional(readOnly = true)
    protected User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotExistUserIdException("해당 이메일의 유저가 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public void login(LoginDto loginDto) {
        String requestPassword = loginDto.getPassword();
        User user = findUserByEmail(loginDto.getEmail());

        checkPassword(user, requestPassword);
    }

    @Transactional
    public void modify(@Valid UserDto userDto, String email) {
        User user = findUserByEmail(userDto.getEmail());
        checkEmail(user, email);

        user.modify(userConverter.convertFromDto(userDto));
    }

    @Transactional
    public void removeById(UserDto userDto, String email) {
        User user = findUserByEmail(userDto.getEmail());
        checkEmail(user, email);

        userRepository.delete(user);
    }

    private void checkPassword(User user, String password) {
        if (!user.checkPassword(password)) {
            throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    protected void checkEmail(User user, String email) {
        if (!user.checkEmail(email)) {
            throw new NotMatchEmailException("이메일이 일치하지 않습니다.");
        }
    }
}