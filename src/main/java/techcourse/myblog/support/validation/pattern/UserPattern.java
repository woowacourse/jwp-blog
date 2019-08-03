package techcourse.myblog.support.validation.pattern;

public class UserPattern {
    public static final String NAME = "^[^ \\-!@#$%^&*(),.?\\\":{}|<>0-9]{2,10}$";
    public static final String EMAIL = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}$";
    
    public static final String NAME_CONSTRAINT_MESSAGE = "이름은 2~10자, 숫자나 특수문자가 포함될 수 없습니다.";
    public static final String EMAIL_CONSTRAINT_MESSAGE = "이메일 양식을 지켜주세요.";
    public static final String PASSWORD_CONSTRAINT_MESSAGE = "비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합으로 입력하세요.";
    public static final String NO_INPUT_MESSAGE = "입력이 필요합니다.";
}
