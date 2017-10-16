package co.hopeorbits.holder;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by abc on 9/27/2017.
 */

public class CategoryModels implements Serializable {

    String categoryID,categoryName,error,categoryImage;

    public ArrayList<ItemModelSet> getItemModelSet() {
        return itemModelSet;
    }

    public void setItemModelSet(ArrayList<ItemModelSet> itemModelSet) {
        this.itemModelSet = itemModelSet;
    }

    ArrayList<ItemModelSet> itemModelSet = new ArrayList<>();

    ArrayList<CategoryIntoCategoryList> categoryIntoCategoryList = new ArrayList<>();


    public ArrayList<CategoryIntoCategoryList> getCategoryIntoCategoryList() {
        return categoryIntoCategoryList;
    }

    public void setCategoryIntoCategoryList(ArrayList<CategoryIntoCategoryList> categoryIntoCategoryList) {
        this.categoryIntoCategoryList = categoryIntoCategoryList;
    }

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

}
