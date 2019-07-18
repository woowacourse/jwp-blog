package techcourse.myblog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.UserDto;
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
        return userRepository.save(new User(userDto.getEmail(), userDto.getName(), userDto.getPassword())).getEmail();
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<UserDto> userDtos = new ArrayList<>();
        userRepository.findAll().forEach(user -> userDtos.add(new UserDto(user)));

        return userDtos;
    }

    @Transactional(readOnly = true)
    public UserDto findById(String email) {
        return new UserDto(userRepository.findById(email)
                .orElseThrow(IllegalArgumentException::new));
    }

    @Transactional
    public boolean login(@Valid UserDto userDto) {
        String password1 = userDto.getPassword();
        String password2 = findById(userDto.getEmail()).getPassword();

        return password1.equals(password2);
    }

    @Transactional
    public void modify(@Valid UserDto userDto) {
        User user = userRepository.findById(userDto.getEmail())
                .orElseThrow(IllegalAccessError::new);
        user.updateName(userDto.getName());
        user.updatePassword(userDto.getPassword());
    }

}
