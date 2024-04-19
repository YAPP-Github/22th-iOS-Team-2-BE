package com.pyonsnalcolor.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "Category 요청 DTO")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {

    private String id;
    private String category;
}