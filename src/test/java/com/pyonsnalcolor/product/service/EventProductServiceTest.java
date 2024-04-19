package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.member.entity.Member;
import com.pyonsnalcolor.member.enumtype.OAuthType;
import com.pyonsnalcolor.member.enumtype.Role;
import com.pyonsnalcolor.product.entity.Banner;
import com.pyonsnalcolor.product.entity.enumtype.BannerType;
import com.pyonsnalcolor.product.repository.BannerRepository;
import com.pyonsnalcolor.push.PushKeyword;
import com.pyonsnalcolor.push.dto.PushKeywordRequestDto;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class EventProductServiceTest {

    @Autowired
    private BannerRepository bannerRepository;

    @Test
    @DisplayName("검색 키워드에서 특수문자는 제외되고 조회합니다.")
    void savePushKeyword() {
        Banner banner1 = Banner.builder().bannerType(BannerType.HOME_EVENT)
                .title("이벤트 제목1")
                .image("https://pyonsnal-bucket.s3.ap-northeast-2.amazonaws.com/eventImageTest.png")
                .build();

        Banner banner2 = Banner.builder().bannerType(BannerType.HOME_EVENT)
                .title("이벤트 제목2")
                .image("https://pyonsnal-bucket.s3.ap-northeast-2.amazonaws.com/eventImageTest.png")
                .build();

        Banner banner3 = Banner.builder().bannerType(BannerType.HOME_EVENT)
                .title("이벤트 제목3")
                .image("https://pyonsnal-bucket.s3.ap-northeast-2.amazonaws.com/eventImageTest.png")
                .build();
        bannerRepository.save(banner1);
        bannerRepository.save(banner2);
        bannerRepository.save(banner3);
    }

}