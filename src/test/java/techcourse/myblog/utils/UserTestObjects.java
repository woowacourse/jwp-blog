package techcourse.myblog.utils;

import techcourse.myblog.application.dto.UserRequestDto;

public class UserTestObjects {
    private static final String AUTHOR_NAME = "미르";
    private static final String UPDATE_NAME = "미르르르";
    private static final String AUTHOR_EMAIL = "ddu0422@naver.com";
    private static final String READER_NAME = "올라프";
    private static final String READER_EMAIL = "gusud96@gmail.com";
    private static final String PASSWORD = "asdf1234!A";

    public static final UserRequestDto LOGIN_USER_DTO = new UserRequestDto(AUTHOR_NAME, AUTHOR_EMAIL, PASSWORD);
    public static final UserRequestDto LOGIN_FAIL_USER_DTO = new UserRequestDto(READER_NAME, READER_EMAIL, PASSWORD);
    public static final UserRequestDto SIGN_UP_USER_DTO = new UserRequestDto(AUTHOR_NAME, AUTHOR_EMAIL, PASSWORD);
    public static final UserRequestDto UPDATE_USER_DTO = new UserRequestDto(UPDATE_NAME, AUTHOR_EMAIL, PASSWORD);
    public static final UserRequestDto AUTHOR_DTO = new UserRequestDto(AUTHOR_NAME, AUTHOR_EMAIL, PASSWORD);
    public static final UserRequestDto READER_DTO = new UserRequestDto(READER_NAME, READER_EMAIL, PASSWORD);
}
