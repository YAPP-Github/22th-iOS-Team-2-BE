package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.member.service.MemberService;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.dto.banner.*;
import com.pyonsnalcolor.product.entity.Banner;
import com.pyonsnalcolor.product.entity.enumtype.BannerType;
import com.pyonsnalcolor.product.enumtype.Curation;
import com.pyonsnalcolor.product.repository.BannerRepository;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import com.pyonsnalcolor.product.repository.PbProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;
    private final PbProductRepository pbProductRepository;
    private final EventProductRepository eventProductRepository;
    private final MemberService memberService;

    public HomeBannersDto getHomeBanners(List<String> favoriteIds) {
        List<EventImageDto> images = bannerRepository.findAllByBannerType(BannerType.HOME_EVENT)
                .stream().map(EventImageDto::of).collect(Collectors.toUnmodifiableList());

        List<Curation> curations = Arrays.asList(Curation.values());
        List<Object> curationProducts = curations.stream()
                .map(curation -> createCurationProductResponseDto(curation, favoriteIds))
                .collect(Collectors.toUnmodifiableList());

        HomeBannerDto curationDto = HomeBannerDto.builder()
                .type("curationProducts")
                .value(curationProducts)
                .build();
        HomeBannerDto eventDto = HomeBannerDto.builder()
                .type("eventBanners")
                .value(List.of(EventBannerDto.of(images)))
                .build();

        return new HomeBannersDto(List.of(curationDto, eventDto));
    }

    public CurationProductsResponseDto getCurationProducts(List<String> favoriteIds) {
        List<Curation> curations = Arrays.asList(Curation.values());

        List<CurationProductResponseDto> curationProductResponseDtos = curations.stream()
                .map(curation -> createCurationProductResponseDto(curation, favoriteIds))
                .collect(Collectors.toUnmodifiableList());

        return new CurationProductsResponseDto(curationProductResponseDtos);
    }

    private CurationProductResponseDto createCurationProductResponseDto(Curation curation, List<String> favoriteIds) {
        List<ProductResponseDto> eventProducts = eventProductRepository.findByCuration(curation)
                .stream()
                .map(p -> memberService.updateProductIfFavorite(p.convertToDto(), favoriteIds))
                .collect(Collectors.toUnmodifiableList());

        List<ProductResponseDto> pbProducts = pbProductRepository.findByCuration(curation)
                .stream()
                .map(p -> memberService.updateProductIfFavorite(p.convertToDto(), favoriteIds))
                .collect(Collectors.toUnmodifiableList());

        Set<ProductResponseDto> result = new HashSet<>();
        result.addAll(eventProducts);
        result.addAll(pbProducts);

        return CurationProductResponseDto.builder()
                .title(curation.getKorean())
                .subTitle(curation.getDescription())
                .products(result)
                .build();
    }
}