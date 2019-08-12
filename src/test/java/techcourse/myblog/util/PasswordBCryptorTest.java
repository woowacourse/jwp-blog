package techcourse.myblog.util;

import org.junit.jupiter.api.Test;
import techcourse.myblog.support.encrytor.EncryptHelper;
import techcourse.myblog.support.encrytor.PasswordBCryptor;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordBCryptorTest {

    private EncryptHelper encryptHelper = new PasswordBCryptor();

    @Test
    void 비밀번호_정상_인코딩() {
        String plainPassword = "PassWord!1";
        String encodedPassword = encryptHelper.encrypt(plainPassword);
        assertTrue(encryptHelper.isMatch(plainPassword, encodedPassword));
    }
}
