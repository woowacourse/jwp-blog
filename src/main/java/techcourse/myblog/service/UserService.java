package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.controller.dto.UserDTO;
import techcourse.myblog.exception.EmailRepetitionException;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void save(UserDTO userDTO){
        if (isDuplicateEmail(userDTO)){
            throw new EmailRepetitionException("이메일이 중복입니다.");
        }
        userRepository.save(new User(userDTO.getUserName(), userDTO.getEmail(), userDTO.getPassword()));
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public boolean isDuplicateEmail(UserDTO userDTO) {
        return userRepository.findUserByEmailAddress(userDTO.getEmail()) != null;
    }

    public void delete(UserDTO userDTO){
        userRepository.deleteUserByEmailAddress(userDTO.getEmail());
    }
}
