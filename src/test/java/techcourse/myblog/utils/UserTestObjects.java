package techcourse.myblog.utils;

import techcourse.myblog.application.dto.UserDto;

public class UserTestObjects {
    private static final String AUTHOR_NAME = "미르";
    private static final String UPDATE_NAME = "미르르르";
    private static final String AUTHOR_EMAIL = "ddu0422@naver.com";
    private static final String READER_NAME = "올라프";
    private static final String READER_EMAIL = "gusud96@gmail.com";
    private static final String PASSWORD = "asdf1234!A";

    public static final UserDto LOGIN_USER_DTO = new UserDto(AUTHOR_NAME, AUTHOR_EMAIL, PASSWORD);
    public static final UserDto LOGIN_FAIL_USER_DTO = new UserDto(READER_NAME, READER_EMAIL, PASSWORD);
    public static final UserDto SIGN_UP_USER_DTO = new UserDto(AUTHOR_NAME, AUTHOR_EMAIL, PASSWORD);
    public static final UserDto UPDATE_USER_DTO = new UserDto(UPDATE_NAME, AUTHOR_EMAIL, PASSWORD);
    public static final UserDto AUTHOR_DTO = new UserDto(AUTHOR_NAME, AUTHOR_EMAIL, PASSWORD);
    public static final UserDto READER_DTO = new UserDto(READER_NAME, READER_EMAIL, PASSWORD);
}
