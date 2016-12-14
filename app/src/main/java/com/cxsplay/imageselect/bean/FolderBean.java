package com.cxsplay.imageselect.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.cxsplay.imageselect.BR;

/**
 * Created by chuxiaoshan on 16/12/8.
 * FolderBean
 */

public class FolderBean extends BaseObservable {

    private String dir;

    private String firstImagePath;

    private String name;

    private int count;

    @Bindable
    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = this.dir.lastIndexOf("/");
        setName(this.dir.substring(lastIndexOf + 1));
        notifyPropertyChanged(BR.dir);
    }

    @Bindable
    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
        notifyPropertyChanged(BR.firstImagePath);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
    }

    @Override
    public String toString() {
        return "FolderBean{" +
                "dir='" + dir + '\'' +
                ", firstImagePath='" + firstImagePath + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
