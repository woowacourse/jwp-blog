package techcourse.myblog.service;

import techcourse.myblog.domain.user.User;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.exception.LoginException;
import techcourse.myblog.service.request.UserLoginDto;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
	private final UserRepository userRepository;

	public LoginService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User login(UserLoginDto userLoginDto) {
		User loginUser = userRepository.findByInformationEmail(userLoginDto.getEmail())
				.orElseThrow(LoginException::new);
		if (!loginUser.matchPassword(userLoginDto.getPassword())) {
			throw new LoginException();
		}
		return loginUser;
	}
}
