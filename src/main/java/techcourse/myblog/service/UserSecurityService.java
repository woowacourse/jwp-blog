package techcourse.myblog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Slf4j
@Service
public class UserSecurityService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @PostConstruct
    private void created(){
        log.debug("체크 로그인");
    }


    @Override
    public UserDetails loadUserByUsername(String email)throws UsernameNotFoundException{
        log.info("loadUserBuUserName user id {} ", email);
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return user.get();
        }
        throw new UsernameNotFoundException("no user");
    }
}
