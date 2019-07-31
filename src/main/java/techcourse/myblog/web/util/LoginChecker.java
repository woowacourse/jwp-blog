package techcourse.myblog.web.util;

import org.springframework.stereotype.Component;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.web.exception.NotLoggedInException;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@Component
public class LoginChecker {
	public static final String LOGGED_IN_USER = "loggedInUser";

	public boolean isLoggedInSameId(HttpSession session, Long id) {
		UserPublicInfoDto loggedInUser = getLoggedInUser(session);
		return loggedInUser.getId().equals(id);
	}

	public boolean isLoggedIn(HttpSession session) {
		try {
			getLoggedInUser(session);
			return true;
		} catch (NotLoggedInException e) {
			return false;
		}
	}

	@NotNull
	public UserPublicInfoDto getLoggedInUser(HttpSession session) {
		UserPublicInfoDto user = (UserPublicInfoDto) session.getAttribute(LOGGED_IN_USER);
		if (user == null) {
			throw new NotLoggedInException();
		}
		return user;
	}
}
