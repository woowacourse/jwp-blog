package techcourse.myblog.support.encrytor;

public interface EncryptHelper {
    String encrypt(String plainPassword);

    boolean isMatch(String plainPassword, String encryptedPassword);
}
