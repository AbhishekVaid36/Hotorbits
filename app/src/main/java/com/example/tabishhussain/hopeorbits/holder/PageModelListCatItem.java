package com.example.tabishhussain.hopeorbits.holder;

import java.util.ArrayList;

/**
 * Created by abc on 9/29/2017.
 */

public class PageModelListCatItem {
    String pageID;
    ArrayList<CategoryModelsCatItem> categoryModels = new ArrayList<>();

    public String getPageID() {
        return pageID;
    }

    public void setPageID(String pageID) {
        this.pageID = pageID;
    }

    public ArrayList<CategoryModelsCatItem> getCategoryModels() {
        return categoryModels;
    }

    public void setCategoryModels(ArrayList<CategoryModelsCatItem> categoryModels) {
        this.categoryModels = categoryModels;
    }


}
