package com.pyonsnalcolor.push.service;

import com.pyonsnalcolor.member.entity.Member;
import com.pyonsnalcolor.member.repository.MemberRepository;
import com.pyonsnalcolor.member.enumtype.OAuthType;
import com.pyonsnalcolor.member.enumtype.Role;
import com.pyonsnalcolor.member.service.AuthService;
import com.pyonsnalcolor.product.entity.Banner;
import com.pyonsnalcolor.product.entity.enumtype.BannerType;
import com.pyonsnalcolor.product.repository.BannerRepository;
import com.pyonsnalcolor.push.dto.PushProductStoreRequestDto;
import com.pyonsnalcolor.push.dto.PushProductStoreResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

// @Transactional
@SpringBootTest
class PushProductStoreServiceTest {

    @Autowired
    private PushProductStoreService pushProductStoreService;
    @Autowired
    private BannerRepository bannerRepository;


    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이벤트 제목1")
    void savePushKeyword() {
        Banner banner1 = Banner.builder().bannerType(BannerType.HOME_EVENT)
                .id(UUID.randomUUID().toString())
                .title("이벤트 제목1")
                .image("https://pyonsnal-bucket.s3.ap-northeast-2.amazonaws.com/eventImageTest.png")
                .build();

        Banner banner2 = Banner.builder().bannerType(BannerType.HOME_EVENT)
                .id(UUID.randomUUID().toString())
                .title("이벤트 제목2")
                .image("https://pyonsnal-bucket.s3.ap-northeast-2.amazonaws.com/eventImageTest.png")
                .build();

        Banner banner3 = Banner.builder().bannerType(BannerType.HOME_EVENT)
                .id(UUID.randomUUID().toString())
                .title("이벤트 제목3")
                .image("https://pyonsnal-bucket.s3.ap-northeast-2.amazonaws.com/eventImageTest.png")
                .build();
        bannerRepository.save(banner1);
        bannerRepository.save(banner2);
        bannerRepository.save(banner3);
    }


//    @DisplayName("회원가입하면 모든 편의점을 구독 신청합니다.")
//    @Test
//    void getPushProductStores() {
//        Member member =  Member.builder()
//                .email("sample@gmail.com")
//                .OAuthType(OAuthType.APPLE)
//                .oAuthId("apple-sample@gmail.com")
//                .refreshToken("refreshToken")
//                .role(Role.ROLE_USER).build();
//        Member savedMember = memberRepository.save(member);
//        authService.createPushProductStores(member);
//
//        List<PushProductStoreResponseDto> result = pushProductStoreService.getPushProductStores(savedMember.getId());
//
//        assertAll(
//                () -> assertThat(result.size()).isEqualTo(8),
//                () -> assertThat(result).extracting("productStore")
//                        .contains("PB_CU", "PB_GS25", "PB_EMART24", "PB_SEVEN_ELEVEN",
//                                "EVENT_CU", "EVENT_GS25", "EVENT_EMART24", "EVENT_SEVEN_ELEVEN"),
//                () -> assertThat(result).extracting("isSubscribed")
//                        .containsOnly(true));
//    }
//
//    @DisplayName("편의점 푸시 알림을 구독 취소합니다.")
//    @Test
//    void subscribePushProductStores() {
//        Member member =  Member.builder()
//                .email("sample@gmail.com")
//                .OAuthType(OAuthType.APPLE)
//                .oAuthId("apple-sample@gmail.com")
//                .refreshToken("refreshToken")
//                .role(Role.ROLE_USER).build();
//        Member savedMember = memberRepository.save(member);
//        authService.createPushProductStores(savedMember);
//
//        PushProductStoreRequestDto requestDto = PushProductStoreRequestDto.builder()
//                .productStores(List.of("EVENT_CU", "PB_CU"))
//                .build();
//
//        pushProductStoreService.unsubscribePushProductStores(savedMember.getId(), requestDto);
//        List<PushProductStoreResponseDto> result = pushProductStoreService.getPushProductStores(savedMember.getId());
//
//        assertAll(
//                () -> assertThat(result).filteredOn(PushProductStoreResponseDto::getProductStore, "PB_CU")
//                        .filteredOn(PushProductStoreResponseDto::getIsSubscribed, false),
//                () -> assertThat(result).filteredOn(PushProductStoreResponseDto::getProductStore,"EVENT_CU")
//                        .filteredOn(PushProductStoreResponseDto::getIsSubscribed, false));
//    }
}
