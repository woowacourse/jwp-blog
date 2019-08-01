package techcourse.myblog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.converter.UserConverter;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserRequestDto;
import techcourse.myblog.application.dto.UserResponseDto;
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
    private UserConverter userConverter = UserConverter.getInstance();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public String save(UserRequestDto userRequestDto) {
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new DuplicatedIdException("이미 사용중인 이메일입니다.");
        }
        User user = userConverter.convertFromDto(userRequestDto);
        return userRepository.save(user).getEmail();
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        return userConverter.createFromEntities(userRepository.findAll());
    }

    User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotExistUserIdException("해당 이메일의 유저가 존재하지 않습니다."));
    }

    @Valid
    @Transactional(readOnly = true)
    public UserResponseDto findByEmail(String email) {
        return userConverter.convertFromEntity(findUserByEmail(email));
    }

    @Transactional(readOnly = true)
    public void login(LoginDto loginDto) {
        String password = loginDto.getPassword();
        User user = findUserByEmail(loginDto.getEmail());
        if (!user.authenticate(password)) {
            throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    public void modify(UserRequestDto userRequestDto) {
        User user = findUserByEmail(userRequestDto.getEmail());
        user.modify(userRequestDto);
    }

    @Transactional
    public void removeByEmail(String email) {
        userRepository.deleteByEmail(email);
    }
}