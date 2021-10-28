package project.myinstagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.myinstagram.dto.user.SignUpDTO;
import project.myinstagram.entity.User;
import project.myinstagram.repository.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void signUp(SignUpDTO userDTO){
        String password = userDTO.getPassword();
        userDTO.setPassword(bCryptPasswordEncoder.encode(password));
        userDTO.setRole("ROLE_USER");
        userRepository.save(userDTO.toEntity());
    }

    @Transactional(readOnly = true)
    public int idCheck(String userId){

        User findUser = userRepository.findByUsername(userId);

        if (findUser == null) {
            return 0;
        }else
            return 1;
    }

}
