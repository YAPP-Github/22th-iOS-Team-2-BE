package com.pyonsnalcolor.product.controller;

import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.service.PbProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "PB 상품 api")
@RestController
@RequiredArgsConstructor
public class PbProductController {
    private final PbProductService pbProductService;

    @Operation(summary = "PB 상품 조회", description = "PB 상품을 조회합니다.")
    @Parameter(name = "pageNumber", description = "조회할 페이지 번호")
    @Parameter(name = "pageSize", description = "페이지별 상품 갯수")
    @Parameter(name = "storeType", description = "편의점 종류(seven_eleven, cu, gs25, emart24, all)")
    @Parameter(name = "sorted", description = "정렬순서")
    @GetMapping("/products/pb-products")
    public Page<ProductResponseDto> getPbProducts(@RequestParam("pageNumber") int pageNumber,
                                                  @RequestParam("pageSize") int pageSize,
                                                  @RequestParam(
                                                     value = "storeType",
                                                     defaultValue = "all"
                                             ) String storeType,
                                                  @RequestParam(
                                                     value = "sorted",
                                                     defaultValue = "updatedTime"
                                             ) String sorted) {
        return pbProductService.getProductsWithPaging(pageNumber, pageSize, storeType, sorted);
    }

    @Operation(summary = "PB 상품 단건 조회", description = "id 바탕으로 PB 상품을 조회합니다.")
    @GetMapping("/products/pb-products/{id}")
    public ProductResponseDto getPbProducts(@PathVariable String id) throws Throwable {
        ProductResponseDto product = pbProductService.getProduct(id);
        return product;
    }
}
