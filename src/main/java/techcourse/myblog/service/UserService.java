package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserRequestDto;
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

    public UserResponseDto update(String email, String name) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User retrieveUser = userOptional.get();
            retrieveUser.update(name);
            User persistUser = userRepository.save(retrieveUser);
            return convertToDto(persistUser);
        }
        throw new EmailNotFoundException("존재하지 않는 회원입니다.");
    }

    // TODO: 2019-07-20 dto통합하기
    public void delete(final UserResponseDto user) {
        userRepository.deleteById(user.getEmail());
    }
}
