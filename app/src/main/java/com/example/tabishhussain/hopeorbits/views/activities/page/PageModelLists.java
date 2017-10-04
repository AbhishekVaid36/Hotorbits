package com.example.tabishhussain.hopeorbits.views.activities.page;

import com.example.tabishhussain.hopeorbits.holder.CategoryModelss;

import java.util.ArrayList;

/**
 * Created by abc on 9/28/2017.
 */

class PageModelLists {

    String pageID;

    public String getPageID() {
        return pageID;
    }

    public void setPageID(String pageID) {
        this.pageID = pageID;
    }

    public ArrayList<CategoryModelss> getCategoryModels() {
        return categoryModels;
    }

    public void setCategoryModels(ArrayList<CategoryModelss> categoryModels) {
        this.categoryModels = categoryModels;
    }

    ArrayList<CategoryModelss> categoryModels = new ArrayList<>();

}
