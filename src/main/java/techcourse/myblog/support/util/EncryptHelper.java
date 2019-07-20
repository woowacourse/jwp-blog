package techcourse.myblog.support.util;

public interface EncryptHelper {
    String encrypt(String plainPassword);

    boolean isMatch(String plainPassword, String encryptedPassword);
}
