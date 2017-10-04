package com.example.tabishhussain.hopeorbits.holder;

import java.util.ArrayList;

/**
 * Created by abc on 9/29/2017.
 */

public class CategoryModelsCatItem {
    String categoryID;

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }


    public ArrayList<ItemModelSetCatItems> getItemModelSet() {
        return itemModelSet;
    }

    public void setItemModelSet(ArrayList<ItemModelSetCatItems> itemModelSet) {
        this.itemModelSet = itemModelSet;
    }

    ArrayList<ItemModelSetCatItems> itemModelSet = new ArrayList<>();
}
