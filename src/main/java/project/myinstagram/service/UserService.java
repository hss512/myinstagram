package project.myinstagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.myinstagram.dto.user.SignUpDTO;
import project.myinstagram.dto.user.UserDTO;
import project.myinstagram.entity.Subscribe;
import project.myinstagram.entity.User;
import project.myinstagram.repository.subscribe.SubscribeRepository;
import project.myinstagram.repository.user.UserRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final SubscribeRepository subscribeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${file.path}")
    private String uploadPath;

    @Transactional
    public SignUpDTO userUpdate(Long id, SignUpDTO userDTO) {
        User findUser = userRepository.findById(id).get();

        String encodePassword = bCryptPasswordEncoder.encode(userDTO.getPassword());

        findUser.setName(userDTO.getName());
        findUser.setEmail(userDTO.getEmail());
        findUser.setBio(userDTO.getBio());
        findUser.setPassword(encodePassword);
        findUser.setWebsite(userDTO.getWebsite());
        findUser.setSex(userDTO.getSex());
        findUser.setPhoneNumber(userDTO.getPhoneNumber());

        return findUser.toSignUpDTO();
    }

    @Transactional
    public String userImageUpdate(Long id, MultipartFile profileImageFile) {

        log.info("userProfileImageUpdate");

        User findUser = userRepository.findById(id).get();

        String username = findUser.getUsername();

        String profileImage = findUser.getProfileImage();

        String originalFilename = profileImageFile.getOriginalFilename();

        UUID uuid = UUID.randomUUID();

        String uploadFilename = uuid+"_"+originalFilename;

        findUser.setProfileImage(uploadFilename);

        BufferedImage image = null;

        try {
            if (!new File(uploadPath, username).exists()) {

                System.out.println("파일 체크 : " + !new File(uploadPath, username).exists());
                File file = new File(uploadPath, username);
                file.mkdir();
            }
            if (profileImage != null) {
                new File(uploadPath + username + "/" + profileImage).delete();
            }
            image = ImageIO.read(profileImageFile.getInputStream());

            int width = image.getWidth();
            int height = image.getHeight();

            if(width > height){
                width = height;
            }else{
                height = width;
            }

            BufferedImage thumbImage = Thumbnails.of(image)
                    .sourceRegion(Positions.CENTER, width, height)
                    .size(150, 150)
                    .outputQuality(1.0f).outputFormat("png") // 보다 고화질
                    .asBufferedImage();

            ImageIO.write(thumbImage, "png", new File(uploadPath + username + "/" + uploadFilename));


        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadFilename;
    }

    public List<UserDTO> getUserList(String searchText, Long userId) {

        List<UserDTO> userList = userRepository.findByUsernameLikeOrNameLike("%" + searchText + "%", "%" + searchText + "%").stream().map(User::toDTO).collect(Collectors.toList());

        List<UserDTO> subList = subscribeRepository.findAllByFromUserId(userId).stream().map(Subscribe::getToUser).map(User::toDTO).collect(Collectors.toList());

        List<UserDTO> sortedList = userList.stream()
                .filter(userDTO -> subList.stream().anyMatch(Predicate.isEqual(userDTO)))
                .collect(Collectors.toList());

        sortedList.addAll(userList.stream()
                .filter(userDTO -> subList.stream().noneMatch(Predicate.isEqual(userDTO)))
                .collect(Collectors.toList()));

        if (userList == null){
            return null;
        }

        return sortedList;
    }
}
