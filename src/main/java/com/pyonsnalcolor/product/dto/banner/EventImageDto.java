package com.pyonsnalcolor.product.dto.banner;

import com.pyonsnalcolor.product.entity.Banner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "홈 화면 - 이벤트 이미지")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventImageDto {

    @NotNull
    private String thumbnailImage;

    @NotNull
    private String detailImage;

    @NotNull
    private String userEvent;

    @NotNull
    private List<String> links;

    public static EventImageDto of(Banner banner) {
        return EventImageDto.builder()
                .links(banner.getLink())
                .thumbnailImage(banner.getThumbnailImage())
                .detailImage(banner.getImage())
                .userEvent(banner.getUserEvent())
                .build();
    }
}
