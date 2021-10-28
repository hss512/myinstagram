package project.myinstagram.principal;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.myinstagram.entity.User;
import project.myinstagram.repository.user.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User findUser = userRepository.findByUsername(username);

        if(findUser == null){
            System.out.println("로그인 오류");
            return null;
        }else{
            System.out.println("로그인 성공");
            return new CustomUserDetails(findUser.toSignUpDTO());
        }
    }
}
