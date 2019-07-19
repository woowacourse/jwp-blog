package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.SnsInfoRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserDto;
import techcourse.myblog.domain.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SnsInfoRepository snsInfoRepository;

    public Optional<UserDto> findUser(UserDto userDto) {
        Optional<User> maybeUser = userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());
        return maybeUser.map(UserDto::from);
    }

    public Optional<UserDto> create(UserDto userDto) {
        try {
            log.info("create userDto" + userDto);
            User user = userRepository.save(userDto.toEntity());
            return Optional.of(UserDto.from(user));
        } catch (Exception e){
            return Optional.empty();
        }
    }

    public List<UserDto> readAll() {
        List<UserDto> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(UserDto.from(user)));

        return users;
    }

    public Optional<UserDto> readWithoutPasswordById(long id) {
        return userRepository.findById(id).map(UserDto::fromWithoutPassword);
    }

    public Optional<UserDto> readById(long id) {
        return userRepository.findById(id).map(UserDto::from);
    }

    @Transactional
    public UserDto updateUser(long id, UserDto userDto) {
        Optional<UserDto> maybeFindUserDto = readById(id);
        if (maybeFindUserDto.isPresent()) {
            maybeFindUserDto.get().updateUserInfo(userDto);

            snsInfoRepository.deleteByUserId(id);
            log.info("update userDto" + maybeFindUserDto);
            User user = userRepository.save(maybeFindUserDto.get().toEntity());

            return UserDto.fromWithoutPassword(user);
        }
        throw new IllegalArgumentException("업데이트 할 수 없습니다.");
    }

    public void deleteById(long id) {
        snsInfoRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }
}
