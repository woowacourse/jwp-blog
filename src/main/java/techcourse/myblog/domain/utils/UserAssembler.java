package techcourse.myblog.domain.utils;

import org.springframework.stereotype.Component;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.UserRequest;
import techcourse.myblog.service.exception.SignUpException;
import techcourse.myblog.support.encryptor.EncryptHelper;

@Component
public class UserAssembler {
    private final EncryptHelper encryptHelper;

    public UserAssembler(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public User toUserFromUserRequest(UserRequest userRequest) {
        try {
            String encryptedPassword = encryptHelper.encrypt(userRequest.getPassword());
            return new User(userRequest.getName(), userRequest.getEmail(), encryptedPassword);
        } catch (IllegalArgumentException e) {
            throw new SignUpException(e.getMessage());
        }
    }
}
