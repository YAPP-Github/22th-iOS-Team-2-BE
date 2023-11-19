package com.pyonsnalcolor.member.service;

import com.pyonsnalcolor.exception.PyonsnalcolorAuthException;
import com.pyonsnalcolor.exception.PyonsnalcolorProductException;
import com.pyonsnalcolor.member.dto.FavoriteRequestDto;
import com.pyonsnalcolor.member.dto.MemberInfoResponseDto;
import com.pyonsnalcolor.member.dto.NicknameRequestDto;
import com.pyonsnalcolor.member.entity.Favorite;
import com.pyonsnalcolor.member.entity.Member;
import com.pyonsnalcolor.member.repository.FavoriteRepository;
import com.pyonsnalcolor.member.repository.MemberRepository;
import com.pyonsnalcolor.product.ProductFactory;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.enumtype.ProductType;
import com.pyonsnalcolor.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.pyonsnalcolor.exception.model.AuthErrorCode.NICKNAME_ALREADY_EXIST;
import static com.pyonsnalcolor.exception.model.AuthErrorCode.REFRESH_TOKEN_MISMATCH;
import static com.pyonsnalcolor.exception.model.CommonErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final ProductFactory productFactory;

    public MemberInfoResponseDto getMemberInfo(Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        return new MemberInfoResponseDto(member);
    }

    public void updateNickname(Long memberId, NicknameRequestDto nicknameRequestDto
    ) {
        Member member = memberRepository.getReferenceById(memberId);
        String updatedNickname = nicknameRequestDto.getNickname();
        if (checkIfNicknameIsDuplicate(updatedNickname)) {
            throw new PyonsnalcolorAuthException(NICKNAME_ALREADY_EXIST);
        }

        member.updateNickname(updatedNickname);
        memberRepository.save(member);
    }

    private boolean checkIfNicknameIsDuplicate(String nickname) {
       return memberRepository.findByNickname(nickname).isPresent();
    }

    public Slice<String> getProductIdsOfFavorite(Pageable pageable, Long memberId, ProductType productType) {
        return favoriteRepository.getFavoriteByMemberIdAndProductType(pageable, memberId, productType)
                .map(Favorite::getProductId);
    }

    public List<String> getProductIdsOfFavorite(Long memberId, ProductType productType) {
        return favoriteRepository.getFavoriteByMemberIdAndProductType(memberId, productType)
                .stream().map(Favorite::getProductId)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<String> getProductIdsOfFavorite(Long memberId) {
        return favoriteRepository.getFavoriteByMemberId(memberId)
                .stream().map(Favorite::getProductId)
                .collect(Collectors.toUnmodifiableList());
    }

    public Page<ProductResponseDto> updateProductsIfFavorite(Page<ProductResponseDto> products,
                                                             List<String> favoriteIds) {
        return products.map(
                p -> updateProductIfFavorite(p, favoriteIds));
    }

    public ProductResponseDto updateProductIfFavorite(ProductResponseDto product,
                                                      List<String> favoriteProductIds) {
        if (favoriteProductIds.contains(product.getId())) {
            product.setFavoriteTrue();
        }
        return product;
    }

    public void createFavorite(Long memberId, FavoriteRequestDto favoriteRequestDto) {
        ProductType productType = favoriteRequestDto.getProductType();
        ProductService productService = productFactory.getService(productType);
        productService.validateProductTypeOfProduct(favoriteRequestDto.getProductId());
        String productId = favoriteRequestDto.getProductId();

        Member member = memberRepository.getReferenceById(memberId);
        Favorite favorite = Favorite.builder()
                .member(member)
                .productId(productId)
                .productType(productType)
                .build();

        if (favoriteRepository.findByProductIdAndMemberId(productId, memberId).isPresent()) {
            throw new PyonsnalcolorProductException(FAVORITE_PRODUCT_ALREADY_EXIST);
        }
        favoriteRepository.save(favorite);
    }

    public void deleteFavorite(Long memberId, FavoriteRequestDto favoriteRequestDto) {
        ProductService productService = productFactory.getService(favoriteRequestDto.getProductType());
        productService.validateProductTypeOfProduct(favoriteRequestDto.getProductId());
        String productId = favoriteRequestDto.getProductId();

        Favorite favorite = favoriteRepository.findByProductIdAndMemberId(productId, memberId)
                .orElseThrow(() -> new PyonsnalcolorProductException(FAVORITE_PRODUCT_NOT_EXIST));
        favoriteRepository.delete(favorite);
    }
}