package com.cxsplay.imageselect;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cxsplay.imageselect.bean.FolderBean;
import com.cxsplay.imageselect.databinding.ItemFolderBinding;

import java.util.List;

/**
 * Created by chuxiaoshan on 16/12/8.
 * ImageAdapter
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyViewHolder> {

    private OnItemClickListener mListener;

    private List<FolderBean> listFolder;

    private LayoutInflater mInflater;

    FolderAdapter(Context context, List<FolderBean> listFolder) {
        this.listFolder = listFolder;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_folder, parent, false);
        return new MyViewHolder(view, mListener);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FolderBean folderBean = listFolder.get(position);
        holder.bind.setBean(folderBean);
        holder.bind.itemFolder.setTag(folderBean);
    }

    @Override
    public int getItemCount() {
        return listFolder == null ? 0 : listFolder.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ItemFolderBinding bind;

        MyViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            bind = DataBindingUtil.bind(itemView);
            bind.setHandler(new FolderItemHandler(listener));
        }
    }

    public class FolderItemHandler {

        OnItemClickListener mListener;

        FolderItemHandler(OnItemClickListener listener) {
            this.mListener = listener;
        }

        public void viewClick(View v) {
            if (mListener != null) {
                FolderBean folderBean = (FolderBean) v.getTag();
                mListener.onViewClick(folderBean);
            }
        }
    }

    interface OnItemClickListener {
        void onViewClick(FolderBean folderBean);
    }
}
