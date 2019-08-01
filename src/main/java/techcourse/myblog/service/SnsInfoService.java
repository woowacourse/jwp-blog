package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.user.SnsInfo;
import techcourse.myblog.domain.user.SnsInfoRepository;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserInfoDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SnsInfoService {
    private final SnsInfoRepository snsInfoRepository;

    @Autowired
    public SnsInfoService(SnsInfoRepository snsInfoRepository) {
        this.snsInfoRepository = snsInfoRepository;
    }

    @Transactional
    public void update(UserInfoDto userInfoDto, User user) {
        updateBySnsCode(userInfoDto.toSnsInfoByFaceBook(user));
        updateBySnsCode(userInfoDto.toSnsInfoByGithub(user));
    }

    private void updateBySnsCode(SnsInfo snsInfo) {
        Optional<SnsInfo> maybeSnsInfo = snsInfoRepository.findBySnsCode(snsInfo.getSnsCode());
        if (maybeSnsInfo.isPresent()) {
            maybeSnsInfo.get().updateSnsInfo(snsInfo);
            return;
        }
        snsInfoRepository.save(snsInfo);
    }

    public void deleteByUserId(long id) {
        snsInfoRepository.deleteByUserId(id);
    }

    public List<SnsInfo> findByUserId(long userId) {
        return snsInfoRepository.findByUserId(userId);
    }
}
