package com.pyonsnalcolor.product.repository;

import com.pyonsnalcolor.product.entity.Banner;
import com.pyonsnalcolor.product.entity.enumtype.BannerType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends MongoRepository<Banner, String> {

    List<Banner> findAllByBannerType(BannerType bannerType);
}
