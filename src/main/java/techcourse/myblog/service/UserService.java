package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.exception.DuplicatedEmailException;

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
}
