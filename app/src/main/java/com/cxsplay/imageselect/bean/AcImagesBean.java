package com.cxsplay.imageselect.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by chuxiaoshan on 16/12/13.
 * AcImagesBean
 */

public class AcImagesBean extends BaseObservable {

    private int itemCount;

    private String currentFolderName;

    public AcImagesBean() {
    }

    public AcImagesBean(int itemCount, String currentFolderName) {
        this.itemCount = itemCount;
        this.currentFolderName = currentFolderName;
    }

    @Bindable
    public String getCurrentFolderName() {
        return currentFolderName;
    }

    public void setCurrentFolderName(String currentFolderName) {
        this.currentFolderName = currentFolderName;
        notifyPropertyChanged(BR.currentFolderName);
    }

    @Bindable
    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
        notifyPropertyChanged(BR.itemCount);
    }
}
