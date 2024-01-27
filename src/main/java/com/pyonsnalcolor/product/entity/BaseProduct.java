package com.pyonsnalcolor.product.entity;

import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.enumtype.Category;
import com.pyonsnalcolor.product.enumtype.Curation;
import com.pyonsnalcolor.product.enumtype.EventType;
import com.pyonsnalcolor.product.enumtype.StoreType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.persistence.Id;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@SuperBuilder
@ToString
@Getter
@NoArgsConstructor
public abstract class BaseProduct extends BaseTimeEntity {
    @Id
    private String id;

    @Indexed
    private StoreType storeType;

    private String image;

    @Indexed
    private String name;

    private int price;

    private String description;

    private Category category;

    private EventType eventType;

    private Boolean isNew;

    private List<Review> reviews = new ArrayList<>();

    private int reviewCount;

    private int viewCount;

    private Curation curation;

    public void updateCuration(Curation curation) {
        this.curation = curation;
    }

    public void deleteCuration() {
        this.curation = null;
    }

    public abstract ProductResponseDto convertToDto();

    public void addReview(Review review) {
        reviews.add(review);
    }

    public void increaseViewCount() {
        this.viewCount += 1;
    }

    public void increaseReviewCount() {
        this.reviewCount += 1;
    }

    public void decreaseReviewCount() {
        this.reviewCount -= 1;
    }

    public void updateIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void updateEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String formattingPrice(int price) {
        return NumberFormat.getInstance().format(price);
    }

    public static Comparator<BaseProduct> getCategoryComparator() {
        return Comparator.comparing(p -> Category.GOODS.equals(p.getCategory()));
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BaseProduct))
            return false;
        BaseProduct baseProduct = (BaseProduct) o;
        return this.name.equals(baseProduct.name) && this.storeType == baseProduct.storeType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, storeType);
    }
}
