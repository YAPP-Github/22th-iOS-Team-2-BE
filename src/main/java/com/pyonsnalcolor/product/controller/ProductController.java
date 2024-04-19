package com.pyonsnalcolor.product.controller;

import com.pyonsnalcolor.member.AuthMemberId;
import com.pyonsnalcolor.member.service.MemberService;
import com.pyonsnalcolor.product.dto.banner.CurationProductsResponseDto;
import com.pyonsnalcolor.product.dto.banner.HomeBannerDto;
import com.pyonsnalcolor.product.dto.banner.HomeBannersDto;
import com.pyonsnalcolor.product.metadata.FilterItems;
import com.pyonsnalcolor.product.metadata.ProductMetaData;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.service.BannerService;
import com.pyonsnalcolor.product.service.SearchProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@io.swagger.v3.oas.annotations.tags.Tag(name = "상품 api")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final SearchProductService searchProductService;
    private final MemberService memberService;
    private final BannerService bannerService;

    @Operation(summary = "상품 검색", description = "전체 상품 중에 해당 키워드가 포함된 상품을 조회합니다.")
    @GetMapping("/products/search")
    public ResponseEntity<Page<ProductResponseDto>> searchProducts(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int sortedCode,
            @Parameter(hidden = true) @AuthMemberId Long memberId

    ) {
        Page<ProductResponseDto> products = searchProductService.searchProduct(pageNumber, pageSize, sortedCode, name);
        List<String> favoriteIds = memberService.getProductIdsOfFavorite(memberId);
        Page<ProductResponseDto> results = memberService.updateProductsIfFavorite(products, favoriteIds);
        return new ResponseEntity(results, HttpStatus.OK);
    }

    @Operation(summary = "상품 관련 메타 데이터 조회", description = "상품 항목별 메타 데이터를 조회합니다.")
    @GetMapping("/products/meta-data")
    public ResponseEntity<Map<String, List<FilterItems>>> getProductMetaData() {
        ProductMetaData productMetaData = ProductMetaData.getInstance();
        Map<String, List<FilterItems>> result = productMetaData.getMetadata();
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @Operation(summary = "큐레이션 상품 조회", description = "큐레이션 상품을 전체 조회합니다.")
    @GetMapping("/products/curation")
    public ResponseEntity<CurationProductsResponseDto> getCurationProducts(
            @Parameter(hidden = true) @AuthMemberId Long memberId
    ) {
        List<String> favoriteIds = memberService.getProductIdsOfFavorite(memberId);
        CurationProductsResponseDto result = bannerService.getCurationProducts(favoriteIds);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @Operation(summary = "홈 화면 큐레이션 상품 및 이벤트 이미지 조회", description = "홈 화면의 큐레이션 상품 및 이벤트 이미지를 조회합니다.")
    @GetMapping("/products/home-banner")
    public ResponseEntity<HomeBannersDto> getHomeBanners(
            @Parameter(hidden = true) @AuthMemberId Long memberId
    ) {
        List<String> favoriteIds = memberService.getProductIdsOfFavorite(memberId);
        HomeBannersDto result = bannerService.getHomeBanners(favoriteIds);
        return new ResponseEntity(result, HttpStatus.OK);
    }
}