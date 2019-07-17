package techcourse.myblog.domain;

import techcourse.myblog.controller.dto.UserDTO;
import techcourse.myblog.model.User;

public class UserValidator {

    public User checkUser(UserDTO userDTO){
        //todo

        return new User(userDTO.getUserName(), userDTO.getEmail(), userDTO.getPassword());
    }
}
