package techcourse.myblog.service;

public class UserAuthenticateException extends IllegalArgumentException {

    public UserAuthenticateException() {
        super("이메일 또는 비밀번호가 일치하지 않습니다");
    }
}
