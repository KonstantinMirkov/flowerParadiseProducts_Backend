package app.model.entity;

import app.model.entity.enums.GiftSize;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static app.constant.ConstantMessages.*;

@Entity
@Table(name = "gift")
public class GiftEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    @Length(min = 3, max = 50, message = PRODUCT_NAME_MUST_BE_BETWEEN_3_AND_50_CHARACTERS)
    private String name;

    @Column(unique = true, nullable = false)
    @Length(min = 3, max = 50, message = PRODUCT_TYPE_MUST_BE_BETWEEN_3_AND_50_CHARACTERS)
    private String type;

    @Column
    @Length(min = 5, max = 250, message = DESCRIPTION_MUST_BE_BETWEEN_5_AND_250_CHARACTERS)
    private String description;

    @Column
    @Min(value = 1, message = QUANTITY_MUST_BE_AT_LEAST_1_OR_BIGGER)
    @Max(value = 1000, message = QUANTITY_MUST_BE_1000_OR_LOWER)
    private int quantity;

    @Column(name = "gift_size")
    private GiftSize giftSize;

    public GiftEntity() {
    }

    public GiftEntity(String name, String type, String description, int quantity, GiftSize giftSize) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.quantity = quantity;
        this.giftSize = giftSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public GiftSize getGiftSize() {
        return giftSize;
    }

    public void setGiftSize(GiftSize giftSize) {
        this.giftSize = giftSize;
    }
}
