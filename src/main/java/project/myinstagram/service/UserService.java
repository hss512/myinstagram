package project.myinstagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.myinstagram.dto.user.SignUpDTO;
import project.myinstagram.entity.User;
import project.myinstagram.repository.UserRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
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
    public String userImageUpdate(Long id, MultipartFile profileImageFile)/* throws IOException*/ {

        log.info("userProfileImageUpdate");

        User findUser = userRepository.findById(id).get();

        String username = findUser.getUsername();

        String profileImage = findUser.getProfileImage();

        String originalFilename = profileImageFile.getOriginalFilename();

        UUID uuid = UUID.randomUUID();

        String uploadFilename = uuid+"_"+originalFilename;

        findUser.setProfileImage(uploadFilename);

        String contentType = profileImageFile.getContentType();



        /*BufferedImage srcImg = ImageIO.read(profileImageFile.getInputStream());

        // 썸네일 크기 입니다.
        int dw = 450, dh = 270;

        // 원본이미지 크기 입니다.
        int ow = srcImg.getWidth();
        int oh = srcImg.getHeight();

        // 늘어날 길이를 계산하여 패딩합니다.
        int pd = 0;
        if(ow > oh) {
            pd = (int)(Math.abs((dh * ow / (double)dw) - oh) / 2d);
        } else {
            pd = (int)(Math.abs((dw * oh / (double)dh) - ow) / 2d);
        }
        srcImg = Scalr.pad(srcImg, pd, Color.WHITE, Scalr.OP_ANTIALIAS);

        // 이미지 크기가 변경되었으므로 다시 구합니다.
        ow = srcImg.getWidth();
        oh = srcImg.getHeight();

        // 썸네일 비율로 크롭할 크기를 구합니다.
        int nw = ow;
        int nh = (ow * dh) / dw;
        if(nh > oh) {
            nw = (oh * dw) / dh;
            nh = oh;
        }

        // 늘려진 이미지의 중앙을 썸네일 비율로 크롭 합니다.
        BufferedImage cropImg = Scalr.crop(srcImg, (ow-nw)/2, (oh-nh)/2, nw, nh);

        // 리사이즈(썸네일 생성)
        BufferedImage destImg = Scalr.resize(cropImg, dw, dh);*/



        try {
            if (!new File(uploadPath, username).exists()) {

                System.out.println("파일 체크 : " + !new File(uploadPath, username).exists());
                File file = new File(uploadPath, username);
                file.mkdir();
            }
            if (profileImage != null) {
                new File(uploadPath + username + "/" + profileImage).delete();
            }

            /*ImageIO.write(destImg, ".jpeg", new File(uploadPath + username + "/" + uploadFilename));

            log.info("contentType={}",contentType);*/

            profileImageFile.transferTo(new File(uploadPath + username + "/" + uploadFilename));


        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadFilename;
    }
}
