package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.EmailRepetitionException;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.service.dto.UserDTO;

import java.util.List;

@Service
@Transactional
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(UserDTO userDTO) {
        if (isDuplicateEmail(userDTO)) {
            throw new EmailRepetitionException("이메일이 중복입니다.");
        }
        return userRepository.save(userDTO.toDomain());
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public boolean isDuplicateEmail(UserDTO userDTO) {
        return userRepository.existsByEmail(userDTO.getEmail());
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public User update(UserDTO userDTO) {
        log.error("email {} ", userDTO.getEmail());
        log.error("name {} ", userDTO.getUserName());
        log.error("password {} ", userDTO.getPassword());

        User user = userRepository.findByEmail(userDTO.getEmail()).get();
        user.update(userDTO.toDomain());

        return user;
    }
}
