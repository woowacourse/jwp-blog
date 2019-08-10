package techcourse.myblog.application.assembler;

import org.springframework.stereotype.Component;
import techcourse.myblog.application.dto.UserRequest;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.domain.User;

@Component
public class UserAssembler {
    public User convertToUser(UserRequest userRequest) {
        return new User(userRequest.getName(), userRequest.getEmail(), userRequest.getPassword());
    }

    public User fromResponseToEntity(UserResponse userResponse) {
        return null;
    }

    public UserRequest convertToUserRequest(User user) {
        return new UserRequest(user.getName(), user.getEmail(), user.getPassword());
    }

    public UserResponse convertToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
