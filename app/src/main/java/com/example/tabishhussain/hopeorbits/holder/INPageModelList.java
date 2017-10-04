package com.example.tabishhussain.hopeorbits.holder;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by abc on 9/26/2017.
 */

public class INPageModelList implements Serializable {
    String pageID;
    String pageName;
    String currency;
    String details;
    String pageImage;
    String errorMessage;

    ArrayList<CategoryModels> categoryModels = new ArrayList<>();

    public ArrayList<CategoryModels> getCategoryModels() {
        return categoryModels;
    }

    public void setCategoryModels(ArrayList<CategoryModels> categoryModels) {
        this.categoryModels = categoryModels;
    }

    public String getPageID() {
        return pageID;
    }

    public void setPageID(String pageID) {
        this.pageID = pageID;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPageImage() {
        return pageImage;
    }

    public void setPageImage(String pageImage) {
        this.pageImage = pageImage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
