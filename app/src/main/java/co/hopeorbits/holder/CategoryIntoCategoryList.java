package co.hopeorbits.holder;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by abc on 9/28/2017.
 */

public class CategoryIntoCategoryList implements Serializable{
    String categoryID,categoryName,error,categoryImage,price,size,quantity,categoryIntoCategoryList;
    ArrayList<IntoItemModelSet> itemModelSet = new ArrayList<>();

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCategoryIntoCategoryList() {
        return categoryIntoCategoryList;
    }

    public void setCategoryIntoCategoryList(String categoryIntoCategoryList) {
        this.categoryIntoCategoryList = categoryIntoCategoryList;
    }

    public ArrayList<IntoItemModelSet> getItemModelSet() {
        return itemModelSet;
    }

    public void setItemModelSet(ArrayList<IntoItemModelSet> itemModelSet) {
        this.itemModelSet = itemModelSet;
    }
}
