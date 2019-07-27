package techcourse.myblog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.converter.UserConverter;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.application.service.exception.DuplicatedIdException;
import techcourse.myblog.application.service.exception.NotExistUserIdException;
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
    public void save(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new DuplicatedIdException("이미 사용중인 이메일입니다.");
        }

        User user = userConverter.convertFromDto(userDto);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userConverter.createFromEntities(userRepository.findAll());
    }

    public UserDto findById(String email) {
        return userConverter.convertFromEntity(findUserById(email));
    }

    @Transactional(readOnly = true)
    User findUserById(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotExistUserIdException("해당 이메일의 유저가 존재하지 않습니다.", "/login"));
    }

    @Transactional(readOnly = true)
    public void login(LoginDto loginDto) {
        String requestPassword = loginDto.getPassword();
        User user = findUserById(loginDto.getEmail());

        if (!user.isSamePassword(requestPassword)) {
            throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    public void modify(@Valid UserDto userDto, String email) {
        User user = findUserById(userDto.getEmail());

        if (user.isDifferentEmail(email)) {
            throw new IllegalArgumentException();
        }

        user.modify(userConverter.convertFromDto(userDto));
    }

    @Transactional
    public void removeById(UserDto userDto, String email) {
        User user = findUserById(userDto.getEmail());

        if (user.isDifferentEmail(email)) {
            throw new IllegalArgumentException();
        }

        userRepository.delete(user);
    }
}
