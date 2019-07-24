package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.user.*;

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

    public UserDto findUser(UserDto userDto) {
        Optional<User> maybeUser = userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());
        if (maybeUser.isPresent()) {
            return UserDto.from(maybeUser.get());
        }
        throw new IllegalArgumentException("아이디, 비밀번호 확인!");
    }

    public UserDto create(UserDto userDto) {
        try {
            User user = userRepository.save(userDto.toEntity());
            return UserDto.from(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("회원 가입 오류!");
        }
    }

    public List<UserDto> readAll() {
        List<UserDto> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(UserDto.from(user)));

        return users;
    }

    public UserDto readWithoutPasswordById(long id) {
        Optional<UserDto> maybeUserDto = userRepository.findById(id).map(UserDto::fromWithoutPassword);
        if (maybeUserDto.isPresent()) {
            return maybeUserDto.get();
        }
        throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
    }

    @Transactional
    public UserDto updateUser(long id, UserDto userDto) {
        Optional<User> maybeFindUser = userRepository.findById(id);
        if (maybeFindUser.isPresent()) {
            updateInfo(userDto, maybeFindUser.get());

            Optional<User> user = userRepository.findById(id);
            return UserDto.fromWithoutPassword(user.get());
        }
        throw new IllegalArgumentException("업데이트 할 수 없습니다.");
    }

    private void updateInfo(UserDto userDto, User user) {
        user.updateInfo(userDto);

        updateSnsInfo(1L, userDto.getSnsGithubEmail(), user);
        updateSnsInfo(0L, userDto.getSnsFacebookEmail(), user);
    }

    private void updateSnsInfo(long snsCode, String snsEmail, User user) {
        Optional<SnsInfo> maybeSnsInfo = user.getSnsInfo(snsCode);
        if (maybeSnsInfo.isPresent() && snsEmail.length() == 0) {
            snsInfoRepository.deleteById(maybeSnsInfo.get().getId());
            user.deleteSnsInfo(maybeSnsInfo.get());
            return;
        }
        if (maybeSnsInfo.isPresent()) {
            maybeSnsInfo.get().updateSnsInfo(snsEmail);
            return;
        }
        if (snsEmail != null) {
            user.addSns(snsEmail, snsCode);
        }
    }

    public void deleteById(long id) {
        snsInfoRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }
}
