package techcourse.myblog.util;

import org.junit.jupiter.api.Test;
import techcourse.myblog.support.util.BCryptImpl;
import techcourse.myblog.support.util.EncryptHelper;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BCryptImplTest {

    private EncryptHelper encryptHelper = new BCryptImpl();

    @Test
    void 비밀번호_정상_인코딩() {
        String plainPassword = "PassWord!1";
        String encodedPassword = encryptHelper.encrypt(plainPassword);
        assertTrue(encryptHelper.isMatch(plainPassword, encodedPassword));
    }
}
