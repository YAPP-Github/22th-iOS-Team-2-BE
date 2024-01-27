package com.pyonsnalcolor.product.dto.banner;

import com.pyonsnalcolor.product.entity.Banner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "홈 화면 - 이벤트 배너")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventBannerDto {
    @NotNull
    private String title;

    @NotNull
    private List<EventImageDto> bannerDetails;

    public static EventBannerDto of(List<EventImageDto> images) {
        return EventBannerDto.builder()
                .title("편스널 컬러 새소식")
                .bannerDetails(images)
                .build();
    }
}
