package techcourse.myblog.service;

public class DuplicatedEmailException extends RuntimeException {
    public static final String DUPLICATED_USER_MESSAGE = "이미 존재하는 email입니다";

    public DuplicatedEmailException() {
        super(DUPLICATED_USER_MESSAGE);
    }
}
