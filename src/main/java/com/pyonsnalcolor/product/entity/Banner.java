package com.pyonsnalcolor.product.entity;

import com.pyonsnalcolor.product.entity.enumtype.BannerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Document(collection = "banner")
public class Banner {
    @Id
    private String id;
    private BannerType bannerType;
    private String title;
    private String image;
    private String thumbnailImage;
    private String userEvent;
    private List<String> link;
}
