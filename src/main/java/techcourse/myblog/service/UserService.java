package techcourse.myblog.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.service.dto.UserLoginRequest;
import techcourse.myblog.service.dto.UserRequest;
import techcourse.myblog.service.dto.UserResponse;
import techcourse.myblog.service.exception.EditException;
import techcourse.myblog.service.exception.LoginException;
import techcourse.myblog.support.encrytor.EncryptHelper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EncryptHelper encryptHelper;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, EncryptHelper encryptHelper, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.encryptHelper = encryptHelper;
        this.modelMapper = modelMapper;
    }

    public void saveUser(UserRequest userRequest) {
        User user = createUser(userRequest);
        userRepository.save(user);
    }

    private User createUser(UserRequest userRequest) {
        userRequest.setPassword(encryptPassword(userRequest));
        return modelMapper.map(userRequest, User.class);
    }

    private String encryptPassword(UserRequest userRequest) {
        return encryptHelper.encrypt(userRequest.getPassword());
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList())
                ;
    }

    public UserResponse findUserByEmail(UserLoginRequest userLoginRequest) {
        User user = userRepository.findUserByEmail(userLoginRequest.getEmail())
                .orElseThrow(() -> new LoginException("일치하는 email이 없습니다!"));

        checkPassword(userLoginRequest, user);
        return modelMapper.map(user, UserResponse.class);
    }

    private void checkPassword(UserLoginRequest userLoginRequest, User user) {
        if (!encryptHelper.isMatch(userLoginRequest.getPassword(), user.getPassword())) {
            throw new LoginException("비밀번호가 일치하지 않습니다!");
        }
    }

    @Transactional
    public UserResponse editUserName(Long userId, String name) {
        User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
        changeName(name, user);
        return modelMapper.map(user, UserResponse.class);
    }

    private void changeName(String name, User user) {
        try {
            user.changeName(name);
        } catch (IllegalArgumentException e) {
            throw new EditException(e.getMessage());
        }
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }
}
