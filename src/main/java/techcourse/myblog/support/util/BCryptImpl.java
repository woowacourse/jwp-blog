package techcourse.myblog.support.util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptImpl implements EncryptHelper {

    @Override
    public String encrypt(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    @Override
    public boolean isMatch(String plainPassword, String encryptedPassword) {
        return BCrypt.checkpw(plainPassword, encryptedPassword);
    }
}
