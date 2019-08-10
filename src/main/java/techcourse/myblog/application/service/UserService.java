package techcourse.myblog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.assembler.UserAssembler;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserRequestDto;
import techcourse.myblog.application.dto.UserResponseDto;
import techcourse.myblog.application.service.exception.DuplicatedIdException;
import techcourse.myblog.application.service.exception.NotExistUserIdException;
import techcourse.myblog.application.service.exception.NotMatchPasswordException;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;

    private UserAssembler userAssembler = UserAssembler.getInstance();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public String save(UserRequestDto userRequestDto) {
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new DuplicatedIdException("이미 사용중인 이메일입니다.");
        }
        User user = new User.UserBuilder()
                .email(userRequestDto.getEmail())
                .name(userRequestDto.getName())
                .password(userRequestDto.getPassword())
                .build();
        User saved = userRepository.save(user);
        return saved.getEmail();
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(userAssembler::convertEntityToDto)
                .collect(Collectors.toList());
    }

    User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotExistUserIdException("해당 이메일의 유저가 존재하지 않습니다."));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotExistUserIdException("해당 이메일의 유저가 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public User login(LoginDto loginDto) {
        String password = loginDto.getPassword();
        User user = findUserByEmail(loginDto.getEmail());
        if (!user.authenticate(password)) {
            throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    @Transactional
    public void modify(UserRequestDto userRequestDto, User userFromSession) {
        User user = findUserById(userFromSession.getId());
        user.modify(userRequestDto);
    }

    @Transactional
    public void removeByUser(User user) {
        userRepository.deleteById(user.getId());
    }
}