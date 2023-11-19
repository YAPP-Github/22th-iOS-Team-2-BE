package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.member.service.MemberService;
import com.pyonsnalcolor.product.dto.*;
import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.entity.BaseProduct;
import com.pyonsnalcolor.product.enumtype.Curation;
import com.pyonsnalcolor.product.enumtype.Sorted;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import com.pyonsnalcolor.product.repository.PbProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SearchProductService {
    private final EventProductRepository eventProductRepository;
    private final PbProductRepository pbProductRepository;
    private final MemberService memberService;

    public Page<ProductResponseDto> searchProduct(int pageNumber, int pageSize, int sorted, String searchKeyword) {
        List<BaseEventProduct> eventProducts = eventProductRepository.findByNameContaining(searchKeyword);
        List<BasePbProduct> pbProducts = pbProductRepository.findByNameContaining(searchKeyword);

        List<BaseProduct> searchProducts = getProductsExceptDuplicate(eventProducts, pbProducts);
        Comparator comparator = Sorted.findComparatorByFilterList(List.of(sorted));
        searchProducts.sort(comparator);

        List<ProductResponseDto> productResponseDtos = convertToProductResponseDto(searchProducts);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return convertToPage(productResponseDtos, pageRequest);
    }

    private List<BaseProduct> getProductsExceptDuplicate(
            List<BaseEventProduct> eventProducts,
            List<BasePbProduct> pbProducts
    ) {
        List<BaseProduct> products = eventProducts.stream()
                .filter(event -> pbProducts.stream()
                        .noneMatch(pb -> event.equals(pb)))
                .collect(Collectors.toList());
        products.addAll(pbProducts);
        return products;
    }

    private List<ProductResponseDto> convertToProductResponseDto(List<BaseProduct> products) {
        List<ProductResponseDto> responseDtos = products.stream()
                .map(BaseProduct::convertToDto)
                .collect(Collectors.toList());

        return responseDtos;
    }

    private Page<ProductResponseDto> convertToPage(List<ProductResponseDto> list, PageRequest pageRequest) {
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), list.size());

        return new PageImpl<>(list.subList(start, end), pageRequest, list.size());
    }

    public CurationProductsResponseDto getCurationProducts(List<String> favoriteIds) {
        List<Curation> curations = Arrays.asList(Curation.values());

        List<CurationProductResponseDto> curationProductResponseDtos = curations.stream()
                .map(curation -> createCurationProductResponseDto(curation, favoriteIds))
                .collect(Collectors.toUnmodifiableList());

        return new CurationProductsResponseDto(curationProductResponseDtos);
    }

    private CurationProductResponseDto createCurationProductResponseDto(Curation curation, List<String> favoriteIds) {
        List<ProductResponseDto> products = pbProductRepository.findByCuration(curation)
                .stream()
                .map(p -> memberService.updateProductIfFavorite(p.convertToDto(), favoriteIds))
                .collect(Collectors.toUnmodifiableList());

        return CurationProductResponseDto.builder()
                .title(curation.getKorean())
                .subTitle(curation.getDescription())
                .products(products)
                .build();
    }
}