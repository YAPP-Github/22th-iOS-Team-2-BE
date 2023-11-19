package com.pyonsnalcolor.product.controller;

import com.pyonsnalcolor.member.AuthMemberId;
import com.pyonsnalcolor.member.service.MemberService;
import com.pyonsnalcolor.product.dto.*;
import com.pyonsnalcolor.product.enumtype.ProductType;
import com.pyonsnalcolor.product.service.PbProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "PB 상품 api")
@RestController
@RequiredArgsConstructor
public class PbProductController {

    private final PbProductService pbProductService;
    private final MemberService memberService;

    @Operation(summary = "PB 상품 필터 조회", description = "PB 상품을 필터링 조회합니다.")
    @PostMapping("/products/pb-products")
    public ResponseEntity<Page<PbProductResponseDto>> getPbProductsByFilter(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam String storeType,
            @RequestBody ProductFilterRequestDto productFilterRequestDto,
            @Parameter(hidden = true) @AuthMemberId Long memberId
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ProductResponseDto> products = pbProductService.getPagedProductsByFilter(
                pageable, storeType, productFilterRequestDto);
        List<String> favoriteIds = memberService.getProductIdsOfFavorite(memberId, ProductType.PB);
        Page<ProductResponseDto> results = memberService.updateProductsIfFavorite(products, favoriteIds);
        return new ResponseEntity(results, HttpStatus.OK);
    }

    @Operation(summary = "리뷰 등록", description = "특정 상품의 리뷰를 등록합니다")
    @PostMapping(value = "/products/pb-products/{id}/reviews",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> registerReview(
            @PathVariable String id,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestPart(value = "reviewDto") ReviewRequestDto reviewDto
    ) throws Throwable {
        pbProductService.registerReview(imageFile, reviewDto, id);

        return ResponseEntity.noContent().build(); //현재는 리뷰 단건 조회하는 기능 존재 x -> location 지정 안함
    }

    @Operation(summary = "PB 상품 단건 조회", description = "id로 PB 상품을 조회합니다.")
    @GetMapping("/products/pb-products/{id}")
    public ResponseEntity<PbProductResponseDto> getPbProduct(
            @PathVariable String id,
            @Parameter(hidden = true) @AuthMemberId Long memberId
    ) {
        List<String> favoriteIds = memberService.getProductIdsOfFavorite(memberId, ProductType.PB);
        ProductResponseDto product = pbProductService.getProductById(id);
        ProductResponseDto result = memberService.updateProductIfFavorite(product, favoriteIds);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @Operation(summary = "PB 상품 리뷰 좋아요", description = "id에 해당하는 PB 상품의 리뷰 좋아요 카운트 증가.")
    @PutMapping("/products/pb-products/{productId}/reviews/{reviewId}/like")
    public ResponseEntity<Void> likeReview(@PathVariable("productId") String productId,
                                           @PathVariable("reviewId") String reviewId,
                                           @RequestParam("writerId") Long writerId) throws Throwable {
        pbProductService.likeReview(productId, reviewId, writerId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "PB 상품 리뷰 싫어요", description = "id에 해당하는 PB 상품의 리뷰 싫어요 카운트 증가.")
    @PutMapping("/products/pb-products/{productId}/reviews/{reviewId}/hate")
    public ResponseEntity<Void> hateReview(@PathVariable("productId") String productId,
                                           @PathVariable("reviewId") String reviewId,
                                           @RequestParam("writerId") Long writerId) throws Throwable {
        pbProductService.hateReview(productId, reviewId, writerId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "카테고리 설정", description = "상품의 카테고리를 수정합니다")
    @PatchMapping(value = "/products/pb-products/category")
    public ResponseEntity<Void> modifyCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        ProductResponseDto responseDto = pbProductService.updateCategory(categoryRequestDto);
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
