package co.hopeorbits.holder;

import java.util.ArrayList;

/**
 * Created by abc on 9/28/2017.
 */

public class SubCategoryModels {

    String categoryID;

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public ArrayList<SubCategoryIntoCategoryList> getCategoryIntoCategoryList() {
        return categoryIntoCategoryList;
    }

    public void setCategoryIntoCategoryList(ArrayList<SubCategoryIntoCategoryList> categoryIntoCategoryList) {
        this.categoryIntoCategoryList = categoryIntoCategoryList;
    }

    ArrayList<SubCategoryIntoCategoryList> categoryIntoCategoryList = new ArrayList<>();
}
