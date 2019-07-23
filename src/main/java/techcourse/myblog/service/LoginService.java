package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.domain.User.UserRepository;
import techcourse.myblog.web.UserRequestDto;

import java.util.NoSuchElementException;

@Service
public class LoginService {
	private final UserRepository userRepository;

	@Autowired
	public LoginService(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public boolean authenticate(final UserRequestDto.LoginRequestDto loginRequestDto) {
		try {
			User user = userRepository.findByEmail(loginRequestDto.getEmail())
					.orElseThrow(NoSuchElementException::new);
			return user.isSamePassword(loginRequestDto.getPassword());
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public User findByLoginRequestDto(final UserRequestDto.LoginRequestDto loginRequestDto) {
		return userRepository.findByEmail(loginRequestDto.getEmail())
				.orElseThrow(NoSuchElementException::new);
	}
}
