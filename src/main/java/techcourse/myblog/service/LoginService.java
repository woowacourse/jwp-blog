package techcourse.myblog.service;

import techcourse.myblog.domain.User;
import techcourse.myblog.dto.request.UserLoginDto;
import techcourse.myblog.exception.NotFoundEmailException;
import techcourse.myblog.exception.NotMatchPasswordException;
import techcourse.myblog.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
	private final UserRepository userRepository;

	public LoginService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User login(UserLoginDto userLoginDto) {
		User loginUser = userRepository.findByEmail(userLoginDto.getEmail())
				.orElseThrow(NotFoundEmailException::new);
		if (!loginUser.matchPassword(userLoginDto.getPassword())) {
			throw new NotMatchPasswordException();
		}
		return loginUser;
	}
}
