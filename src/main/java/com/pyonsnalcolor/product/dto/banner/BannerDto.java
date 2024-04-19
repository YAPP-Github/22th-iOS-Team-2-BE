package com.pyonsnalcolor.product.dto.banner;

import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.entity.Banner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Schema(description = "큐레이션 및 이벤트 이미지 DTO")
@Getter
@Builder
@AllArgsConstructor
public class BannerDto {

    private String title;

    private String subTitle;
    // 큐레이션 상품
    private Set<ProductResponseDto> products;
    // 이미지 주소
    private String image;

    private String thumbnailImage;

    public static BannerDto of (CurationProductResponseDto curation) {
        return BannerDto.builder()
                .products(curation.getProducts())
                .title(curation.getTitle())
                .subTitle(curation.getSubTitle())
                .build();
    }

    public static BannerDto of (Banner banner) {
        return BannerDto.builder()
                .title(banner.getTitle())
                .thumbnailImage(banner.getThumbnailImage())
                .image(banner.getImage())
                .build();
    }
}
