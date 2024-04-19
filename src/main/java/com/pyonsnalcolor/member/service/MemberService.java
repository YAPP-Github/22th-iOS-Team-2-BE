package com.pyonsnalcolor.member.service;

import com.pyonsnalcolor.exception.PyonsnalcolorAuthException;
import com.pyonsnalcolor.exception.PyonsnalcolorProductException;
import com.pyonsnalcolor.member.GuestValidator;
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
import com.pyonsnalcolor.product.repository.ImageRepository;
import com.pyonsnalcolor.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.pyonsnalcolor.exception.model.AuthErrorCode.*;
import static com.pyonsnalcolor.exception.model.CommonErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final ProductFactory productFactory;
    private final ImageRepository imageRepository;

    public MemberInfoResponseDto getMemberInfo(Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        return new MemberInfoResponseDto(member);
    }

    public void updateProfile(Long memberId, MultipartFile profileImage, NicknameRequestDto nicknameRequestDto) {
        Member member = memberRepository.getReferenceById(memberId);
        if (nicknameRequestDto != null) {
            member = updateNickname(member, nicknameRequestDto);
        }
        if (profileImage != null && !profileImage.isEmpty()) {
            member = updateProfileImage(member, profileImage);
        }
        memberRepository.save(member);
    }

    private Member updateProfileImage(Member member, MultipartFile profileImage) {
        String filePath = imageRepository.uploadImage(profileImage);
        member.updateProfileImage(filePath);
        return member;
    }

    private Member updateNickname(Member member, NicknameRequestDto nicknameRequestDto) {
        validateNicknameFormat(nicknameRequestDto);

        String updatedNickname = nicknameRequestDto.getNickname();
        member.updateNickname(updatedNickname);
        return member;
    }

    public void validateNicknameFormat(NicknameRequestDto nicknameRequestDto) {
        validateNonBlankNickname(nicknameRequestDto);
        checkIfNicknameIsDuplicate(nicknameRequestDto);
    }

    private void validateNonBlankNickname(NicknameRequestDto nicknameRequestDto) {
        String nickname = nicknameRequestDto.getNickname();
        if (nickname.isBlank()) {
            throw new PyonsnalcolorAuthException(INVALID_BLANK_NICKNAME);
        }
    }

    private void checkIfNicknameIsDuplicate(NicknameRequestDto nicknameRequestDto) {
        String nickname = nicknameRequestDto.getNickname();
        if (!memberRepository.findByNickname(nickname).isEmpty()) {
            throw new PyonsnalcolorAuthException(NICKNAME_ALREADY_EXIST);
        }
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