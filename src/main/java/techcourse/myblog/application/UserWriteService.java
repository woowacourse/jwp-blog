package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.UserRequestDto;
import techcourse.myblog.application.dto.UserResponseDto;
import techcourse.myblog.application.exception.DuplicatedEmailException;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;

@Service
@Transactional
public class UserWriteService {
    private final UserRepository userRepository;

    public UserWriteService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto save(UserRequestDto userRequestDto) {
        verifyDuplicateEmail(userRequestDto);
        return UserAssembler.buildUserResponseDto(userRepository.save(UserAssembler.toEntity(userRequestDto)));
    }

    private void verifyDuplicateEmail(UserRequestDto userRequestDto) {
        userRepository.findByEmail(userRequestDto.getEmail())
                .ifPresent(x -> { throw new DuplicatedEmailException(); });
    }

    public void update(User user, UserRequestDto userRequestDto) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    user.modifyName(userRequestDto.getName());
                    existingUser.modifyName(userRequestDto.getName());
                });
    }

    public void remove(User user) {
        userRepository.delete(user);
    }
}
