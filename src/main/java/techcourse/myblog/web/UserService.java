package techcourse.myblog.web;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.web.dto.UserDto;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDto userDto, BindingResult bindingResult) {
        checkUserDataForm(bindingResult);
        checkSamePasswords(userDto);
        checkDuplicateEmail(userDto);
        User user = User.of(userDto.getName(), userDto.getEmail(), userDto.getPassword());
        userRepository.save(user);
    }

    private void checkUserDataForm(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldError();
            String errorMessage = error.getDefaultMessage();
            throw new InvalidDataFormException("test error : " + errorMessage);
        }
    }

    private void checkSamePasswords(UserDto userDto) {
        if (!userDto.getPassword().equals(userDto.getPasswordConfirm())) {
            throw new UnequalPasswordException();
        }
    }

    private void checkDuplicateEmail(UserDto userData) {
        if (userRepository.findByEmail(userData.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }
    }
}
