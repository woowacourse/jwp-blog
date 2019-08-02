package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.user.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final SnsInfoService snsInfoService;

    @Autowired
    public UserService(UserRepository userRepository, SnsInfoService snsInfoService) {
        this.userRepository = userRepository;
        this.snsInfoService = snsInfoService;
    }

    public UserDto findByUserDto(UserDto userDto) {
        Optional<User> maybeUser = userRepository.findByEmailAndPassword(userDto.toEntity().getEmail(), userDto.toEntity().getPassword());
        if (maybeUser.isPresent()) {
            List<SnsInfo> snsInfos = snsInfoService.findByUserId(maybeUser.get().getId());
            return UserInfoDto.fromWithSNS(maybeUser.get(), snsInfos);
        }

        throw new IllegalArgumentException("아이디, 비밀번호 확인!");
    }

    public UserDto findByUserEmail(UserDto userDto) {
        Optional<User> maybeUser = userRepository.findByEmail(userDto.toEntity().getEmail());
        return maybeUser.map(UserInfoDto::from)
                .orElseThrow(() -> new IllegalArgumentException("없는 유저!"));
    }

    public UserDto create(UserDto userDto) {
        try {
            User user = userRepository.save(userDto.toEntity());
            return UserInfoDto.from(user);
        } catch (Exception e) {
            throw new IllegalArgumentException("회원 가입 오류!");
        }
    }

    public List<UserDto> readAll() {
        List<UserDto> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(UserInfoDto.from(user)));

        return users;
    }

    @Transactional
    public UserDto update(UserDto userDto, UserInfoDto userInfoDto) {
        Optional<User> maybeFindUser = userRepository.findById(userDto.toEntity().getId());
        if (maybeFindUser.isPresent()) {
            maybeFindUser.get().updateInfo(userInfoDto.toEntity());
            snsInfoService.update(userInfoDto, maybeFindUser.get());
            return findByUserDto(userInfoDto);
        }
        throw new IllegalArgumentException("업데이트 할 수 없습니다.");
    }

    public void deleteById(UserDto userDto) {
        long id = userDto.toEntity().getId();
        userRepository.deleteById(id);
    }
}
