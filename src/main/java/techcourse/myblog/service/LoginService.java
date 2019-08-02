package techcourse.myblog.service;

import techcourse.myblog.dto.request.UserLoginDto;
import techcourse.myblog.exception.LoginException;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.user.User;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
	private final UserRepository userRepository;

	public LoginService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User login(UserLoginDto userLoginDto) {
		User loginUser = userRepository.findByInformation_Email(userLoginDto.getEmail())
				.orElseThrow(LoginException::new);
		if (!loginUser.matchPassword(userLoginDto.getPassword())) {
			throw new LoginException();
		}
		return loginUser;
	}
}
