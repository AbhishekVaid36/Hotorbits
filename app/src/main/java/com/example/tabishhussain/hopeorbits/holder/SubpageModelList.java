package com.example.tabishhussain.hopeorbits.holder;

import java.util.ArrayList;

/**
 * Created by abc on 9/28/2017.
 */

public class SubpageModelList {
    String pageID;

    public String getPageID() {
        return pageID;
    }

    public void setPageID(String pageID) {
        this.pageID = pageID;
    }

    public ArrayList<SubCategoryModels> getCategoryModels() {
        return categoryModels;
    }

    public void setCategoryModels(ArrayList<SubCategoryModels> categoryModels) {
        this.categoryModels = categoryModels;
    }

    ArrayList<SubCategoryModels> categoryModels = new ArrayList<>();
}
