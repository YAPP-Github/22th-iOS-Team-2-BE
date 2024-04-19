package com.pyonsnalcolor.product.dto.banner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "홈 화면 - 큐레이션 및 이벤트 이미지 조회용 DTO")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HomeBannerDto {

    @NotNull
    private String type;

    @NotNull
    private List<Object> value;
}
