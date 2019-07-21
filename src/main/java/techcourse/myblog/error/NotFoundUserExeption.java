package techcourse.myblog.error;

public class NotFoundUserExeption extends RuntimeException {
    public NotFoundUserExeption(String msg) {
        super(msg);
    }
}
