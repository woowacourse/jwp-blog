package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.controller.dto.UserDTO;
import techcourse.myblog.domain.UserValidator;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void save(UserDTO userDTO){
        UserValidator userValidator = new UserValidator();
        User user = userValidator.checkUser(userDTO);
        userRepository.save(user);
    }

    public List<User> getUsers(){
        return (List<User>) userRepository.findAll();
    }
}
