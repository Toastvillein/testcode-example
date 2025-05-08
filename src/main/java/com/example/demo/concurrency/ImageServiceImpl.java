//package com.example.demo.concurrency;
//
//import com.example.tastefulai.domain.image.dto.ImageResponseDto;
//import com.example.tastefulai.domain.image.entity.Image;
//import com.example.tastefulai.domain.image.repository.ImageRepository;
//import com.example.tastefulai.domain.member.entity.Member;
//import com.example.tastefulai.domain.member.repository.MemberRepository;
//import com.example.tastefulai.global.error.errorcode.ErrorCode;
//import com.example.tastefulai.global.error.exception.BadRequestException;
//import com.example.tastefulai.global.error.exception.NotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@Service
//@RequiredArgsConstructor
//public class ImageServiceImpl {
//
//    private final MemberRepository memberRepository;
//    private final ImageRepository imageRepository;
//    private final S3Uploader s3Uploader;
//
//    public ImageResponseDto uploadImage(Member member, MultipartFile image) {
//        String imgUrl = s3Uploader.uploadImage(image); // return String imgUrl
//
//        // 기존에 저장된 사진을 db와 S3에서 삭제
//        deleteImage(member);
//
//        Image savedImage = imageRepository.save(new Image(imgUrl, adsfads, member));
//
//        return new ImageResponseDto(savedImage.getImageUrl());
//    }
//
//    public void deleteImage(Member member) {
//        Image existImage = imageRepository.findByMemberOrElseThrow(member);
//        s3Uploader.deleteS3Image(existImage.getFileName());
//        imageRepository.delete(existImage);
//    }
//
//    // 1. 양방향 버리기
//    // 2. Image 엔티티에서 OneToOne -> ManyToOne 바꾸기
//}
