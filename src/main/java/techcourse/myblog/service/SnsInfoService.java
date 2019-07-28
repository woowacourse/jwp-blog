package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.user.SnsInfo;
import techcourse.myblog.domain.user.SnsInfoRepository;
import techcourse.myblog.domain.user.User;

import java.util.Optional;

@Service
public class SnsInfoService {
    @Autowired
    private SnsInfoRepository snsInfoRepository;

    public void updateSnsInfo(long snsCode, String snsEmail, User user) {
        Optional<SnsInfo> maybeSnsInfo = user.getSnsInfo(snsCode);
        if (maybeSnsInfo.isPresent() && snsEmail.length() == 0) {
            snsInfoRepository.deleteById(maybeSnsInfo.get().getId());
            user.deleteSnsInfo(maybeSnsInfo.get());
            return;
        }
        if (maybeSnsInfo.isPresent()) {
            maybeSnsInfo.get().updateSnsInfo(snsEmail);
            return;
        }
        if (snsEmail != null) {
            user.addSns(snsEmail, snsCode);
        }
    }

    public void deleteByUserId(long id) {
        snsInfoRepository.deleteByUserId(id);
    }
}
