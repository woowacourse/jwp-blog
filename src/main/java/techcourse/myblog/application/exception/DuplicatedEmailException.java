package techcourse.myblog.application.exception;

public class DuplicatedEmailException extends RuntimeException {
    private static final String DUPLICATED_USER_MESSAGE = "이미 존재하는 email입니다";
    
    public DuplicatedEmailException() {
        super(DUPLICATED_USER_MESSAGE);
    }
}
