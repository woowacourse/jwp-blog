package techcourse.myblog.web;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.web.dto.UserDto;

import javax.validation.Valid;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(@Valid UserDto userDto, BindingResult bindingResult) {
        checkUserDataError(bindingResult);
        checkSamePasswords(userDto);
        checkDuplicateEmail(userDto);
        User user = User.of(userDto.getName(), userDto.getEmail(), userDto.getPassword());
        userRepository.save(user);
    }

    private void checkUserDataError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidSighUpException("올바르지 않은 입력 값 입니다.");
        }
    }

    private void checkSamePasswords(UserDto userData) {
        if (!userData.getPassword().equals(userData.getPasswordConfirm())) {
            throw new InvalidSighUpException("비밀번호가 일치하지 않습니다.");
        }
    }

    private void checkDuplicateEmail(UserDto userData) {
        if (userRepository.findByEmail(userData.getEmail()).isPresent()) {
            throw new InvalidSighUpException("해당 이메일은 존재하는 이메일 입니다.");
        }
    }
}
