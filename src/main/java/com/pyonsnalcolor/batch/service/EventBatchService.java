package com.pyonsnalcolor.batch.service;


import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.entity.BaseProduct;
import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class EventBatchService extends BasicBatchServiceTemplate<BaseEventProduct> {
    public EventBatchService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }

    @Override
    protected <T extends BaseProduct> List<T> getNewProducts(List<T> allProducts) {
        if (allProducts.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> alreadyExistProducts = basicProductRepository.findAll();
        List<T> newProducts = allProducts.stream()
                .filter(product -> !alreadyExistProducts.contains(product))
                .peek(product -> log.info("새로운 행사 상품이 저장됩니다. {}", product))
                .collect(Collectors.toList());
        return newProducts;
    }

    @Override
    protected List<BaseEventProduct> getEventExpiredProducts(List<BaseEventProduct> allProducts) {
        if(allProducts.isEmpty()) {
            return Collections.emptyList();
        }
        StoreType storeType = allProducts.get(0).getStoreType();
        List<String> allProductNames = allProducts.stream().map(p -> p.getName()).collect(Collectors.toList());

        List<BaseEventProduct> alreadyExistEventProducts = basicProductRepository.findByStoreType(storeType);
        List<BaseEventProduct> expiredEventProducts = alreadyExistEventProducts.stream().filter(
                p -> !allProductNames.contains(p.getName())
        ).collect(Collectors.toList());

        return expiredEventProducts;
    }

    @Override
    protected void deleteProducts(List<BaseEventProduct> baseProducts) {
        basicProductRepository.deleteAll(baseProducts);
    }
}
