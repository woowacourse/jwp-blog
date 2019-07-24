package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.controller.dto.UserDTO;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.EmailRepetitionException;
import techcourse.myblog.exception.UserNotExistException;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDTO userDTO) {
        if (isDuplicateEmail(userDTO)) {
            throw new EmailRepetitionException("이메일이 중복입니다.");
        }
        userRepository.save(new User(userDTO.getUserName(), userDTO.getEmail(), userDTO.getPassword()));
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public boolean isDuplicateEmail(UserDTO userDTO) {
        return userRepository.existsByEmail(userDTO.getEmail());
    }

    @Transactional
    public void delete(String email) {
        userRepository.removeByEmail(email);
    }

    public User update(UserDTO userDTO) {
        log.error("email {} ", userDTO.getEmail());
        log.error("name {} ", userDTO.getUserName());
        log.error("password {} ", userDTO.getPassword());
        int result = userRepository.updateUserByEmailAddress(userDTO.getUserName(), userDTO.getPassword(), userDTO.getEmail());
        if (result == 0) {
            throw new UserNotExistException("유저정보가 없습니다.");
        }
        return userRepository.findByEmail(userDTO.getEmail());
    }
}
