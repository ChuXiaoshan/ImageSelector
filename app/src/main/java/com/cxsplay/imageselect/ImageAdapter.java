package com.cxsplay.imageselect;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cxsplay.imageselect.databinding.ItemRvBinding;

import java.util.List;

/**
 * Created by chuxiaoshan on 16/12/8.
 * ImageAdapter
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private OnItemClickListener mListener;

    private LayoutInflater mInflater;

    private List<String> mImgPaths;

    private String folderPath;

    private int screenWidth;

    ImageAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    public void setData(List<String> listImage, String folderPath) {
        this.mImgPaths = listImage;
        this.folderPath = folderPath;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_rv, parent, false);
        return new MyViewHolder(view, mListener);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String path = folderPath + "/" + mImgPaths.get(position);
        holder.bind.setPath(path);
        holder.bind.ivItemImage.setTag(R.id.single_path_key, path);
    }

    @Override
    public int getItemCount() {
        return mImgPaths == null ? 0 : mImgPaths.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ItemRvBinding bind;

        MyViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            bind = DataBindingUtil.bind(itemView);
            bind.setHandler(new ItemHandler(listener));
            ViewGroup.LayoutParams layoutParams = bind.ivItemImage.getLayoutParams();
            layoutParams.width = screenWidth / 3;
            layoutParams.height = screenWidth / 3;
            bind.ivItemImage.setLayoutParams(layoutParams);
        }
    }

    public class ItemHandler {

        OnItemClickListener mListener;

        ItemHandler(OnItemClickListener listener) {
            this.mListener = listener;
        }

        public void imageViewClick(View v) {
            if (mListener != null) {
                String path = (String) v.getTag(R.id.single_path_key);
                mListener.onImageViewClick(path);
            }
        }

        public void imageBtnClick(View v) {
            if (mListener != null) {
                mListener.onImageButtonClick();
            }
        }
    }

    interface OnItemClickListener {
        void onImageViewClick(String str);

        void onImageButtonClick();
    }
}
