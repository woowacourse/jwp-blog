package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserUpdateDto;
import techcourse.myblog.exception.DuplicateEmailException;
import techcourse.myblog.exception.InvalidDataFormException;
import techcourse.myblog.exception.UnequalPasswordException;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDto userDto, BindingResult bindingResult) {
        checkUserDataForm(bindingResult);
        checkEqualPassword(userDto);
        checkDuplicateEmail(userDto.getEmail());
        User user = User.of(userDto);
        userRepository.save(user);
    }

    private void checkUserDataForm(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldError();
            String errorMessage = error.getDefaultMessage();
            throw new InvalidDataFormException(errorMessage);
        }
    }

    private void checkEqualPassword(UserDto userDto) {
        if (!userDto.getPassword().equals(userDto.getPasswordConfirm())) {
            throw new UnequalPasswordException();
        }
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException();
        }
    }

    public void update(User user, UserUpdateDto userUpdateDto, BindingResult bindingResult) {
        checkUserDataForm(bindingResult);
        user.updateUser(userUpdateDto);
        userRepository.save(user);
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }
}
