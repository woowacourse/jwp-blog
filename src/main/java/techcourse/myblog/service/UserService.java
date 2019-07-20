package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserRequestDto;
<<<<<<< HEAD
import techcourse.myblog.exception.DuplicatedEmailException;

=======
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.exception.DuplicatedEmailException;
import techcourse.myblog.exception.EmailNotFoundException;
import techcourse.myblog.exception.InvalidPasswordException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static techcourse.myblog.service.UserAssembler.convertToDto;
>>>>>>> ba9fed822171eba44ccb2afd6d08e2aad0f225fd
import static techcourse.myblog.service.UserAssembler.convertToEntity;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserRequestDto userRequestDto) {
        User user = convertToEntity(userRequestDto);
        String email = user.getEmail();
        if (userRepository.findById(email).isPresent()) {
            throw new DuplicatedEmailException("이메일이 중복됩니다.");
        }
        userRepository.save(user);
    }
<<<<<<< HEAD
=======

    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserAssembler::convertToDto)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    public UserResponseDto findByEmailAndPassword(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!user.getPassword().equals(password)) {
                throw new InvalidPasswordException("틀린 비밀번호입니다.");
            }
            return convertToDto(user);
        }
        throw new EmailNotFoundException("틀린 이메일입니다!");
    }
>>>>>>> ba9fed822171eba44ccb2afd6d08e2aad0f225fd
}
