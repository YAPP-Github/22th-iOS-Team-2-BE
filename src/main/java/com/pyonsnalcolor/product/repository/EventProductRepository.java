package com.pyonsnalcolor.product.repository;

import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.enumtype.Curation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventProductRepository extends BasicProductRepository<BaseEventProduct, String> {
    List<BaseEventProduct> findByCuration(Curation curation);
}