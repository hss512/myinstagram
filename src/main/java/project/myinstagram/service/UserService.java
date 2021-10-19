package project.myinstagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.myinstagram.dto.UserDTO;
import project.myinstagram.entity.User;
import project.myinstagram.repository.UserRepository;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${file.path}")
    private String uploadPath;

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

    @Transactional
    public String userImageUpdate(Long id, MultipartFile profileImageFile) {

        User findUser = userRepository.findById(id).get();

        String originalFilename = profileImageFile.getOriginalFilename();

        UUID uuid = UUID.randomUUID();

        String uploadFilename = uuid+"_"+originalFilename;

        findUser.setProfileImage(uploadFilename);

        return uploadFilename;
    }
}
