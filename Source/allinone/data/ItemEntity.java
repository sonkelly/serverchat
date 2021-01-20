package allinone.data;

import java.io.Serializable;

public class ItemEntity implements Serializable {

    private int itemId;
    private String name;
    private int price;
    private int itemType;
    private String image;
    private int price_change;

    public ItemEntity(int itemId, String name, int price, int itemType,
            String image, int _price_change) {
        super();
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.itemType = itemType;
        this.image = image;
        this.price_change = _price_change;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriceChange() {
        return price_change;
    }

    public void setPriceChnage(int price) {
        this.price_change = price;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
