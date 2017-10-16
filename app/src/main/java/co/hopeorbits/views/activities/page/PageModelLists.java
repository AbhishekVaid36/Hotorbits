package co.hopeorbits.views.activities.page;

import java.util.ArrayList;

import co.hopeorbits.holder.CategoryModelss;

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
