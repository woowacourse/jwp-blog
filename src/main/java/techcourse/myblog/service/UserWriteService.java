package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.service.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Service
public class UserWriteService {
    private final UserRepository userRepository;

    public UserWriteService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User save(UserDto userDto) {
        verifyDuplicateEmail(userDto.getEmail());
        return userRepository.save(userDto.toUser());
    }

    private void verifyDuplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException("이미 사용중인 이메일주소 입니다.");
        }
    }

    @Transactional
    public Optional<User> update(Long id, UserDto userDto) {
        Optional<User> modifyUser = userRepository.findById(id);
        modifyUser.ifPresent(user -> user.modify(userDto.toUser()));
        return userRepository.findById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
