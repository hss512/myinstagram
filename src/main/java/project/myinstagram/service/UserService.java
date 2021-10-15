package project.myinstagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.myinstagram.dto.UserDTO;
import project.myinstagram.entity.User;
import project.myinstagram.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public UserDTO userUpdate(Long id, UserDTO userDTO) {
        User findUser = userRepository.findById(id).get();

        String encodePassword = bCryptPasswordEncoder.encode(userDTO.getPassword());

        findUser.setName(userDTO.getName());
        findUser.setEmail(userDTO.getEmail());
        findUser.setBio(userDTO.getBio());
        findUser.setPassword(encodePassword);
        findUser.setWebsite(userDTO.getWebsite());
        findUser.setSex(userDTO.getSex());
        findUser.setPhoneNumber(userDTO.getPhoneNumber());

        return findUser.toDTO();
    }
}
