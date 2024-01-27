package com.pyonsnalcolor.product.dto.banner;

import com.pyonsnalcolor.product.dto.ProductResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Schema(description = "홈 화면 - PICK! 조회 화면의 큐레이션별 Response DTO")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurationProductResponseDto {

    @NotNull
    private String title;
    @NotNull
    private String subTitle;
    @NotNull
    private Set<ProductResponseDto> products;
}