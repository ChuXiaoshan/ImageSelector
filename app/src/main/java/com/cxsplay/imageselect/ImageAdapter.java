package com.cxsplay.imageselect;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cxsplay.imageselect.databinding.ItemRvBinding;

import java.util.List;

/**
 * Created by chuxiaoshan on 16/12/8.
 * ImageAdapter
 */

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListener mListener;

    private LayoutInflater mInflater;

    private List<String> selectedImages;

    private List<String> mImgPaths;

    private String folderPath;

    private String path;

    private int screenWidth;

    private int selectType;

    ImageAdapter(Context context, int selectType) {
        mInflater = LayoutInflater.from(context);
        this.selectType = selectType;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    void setData(List<String> listImage, List<String> selectedImages, String folderPath) {
        this.mImgPaths = listImage;
        this.selectedImages = selectedImages;
        this.folderPath = folderPath;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view = mInflater.inflate(R.layout.item_rv, parent, false);
                return new MyViewHolder(view);
            case 1:
                View emptyView = mInflater.inflate(R.layout.item_empty, parent, false);
                return new EmptyViewHolder(emptyView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //获取图片路径
        String path = "";
        if (position < mImgPaths.size()) {
            if (TextUtils.isEmpty(folderPath)) {
                path = mImgPaths.get(position);
            } else {
                path = folderPath + "/" + mImgPaths.get(position);
            }
        }
        this.path = path;

        switch (getItemViewType(position)) {
            case 0:
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                if (selectedImages.contains(path)) {
                    myViewHolder.bind.cbImage.setChecked(true);
                } else {
                    myViewHolder.bind.cbImage.setChecked(false);
                }
                myViewHolder.bind.setPath(path);
                myViewHolder.bind.ivItemImage.setTag(R.id.single_path_key, path);
                myViewHolder.bind.cbImage.setTag(R.id.single_path_key, path);
                break;
            case 1:
                break;
            default:
                break;
        }
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        int count = mImgPaths == null ? 0 : mImgPaths.size();
        int surplus = count % 3;
        if (surplus == 0) {
            surplus = 1;
        } else {
            surplus = 3 - surplus + 1;
        }
        return count == 0 ? 0 : count + surplus;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mImgPaths.size()) {
            return 0;
        } else {
            return 1;
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        ItemRvBinding bind;

        MyViewHolder(View itemView) {
            super(itemView);
            bind = DataBindingUtil.bind(itemView);
            bind.setHandler(new ItemHandler());
            if (selectType == ImagesActivity.TYPE_CROP ||
                    selectType == ImagesActivity.TYPE_SINGLE) {
                bind.cbImage.setVisibility(View.GONE);
            }
            bind.setPath(path);
            ViewGroup.LayoutParams layoutParams = bind.ivItemImage.getLayoutParams();
            layoutParams.width = screenWidth / 3;
            layoutParams.height = screenWidth / 3;
            bind.ivItemImage.setLayoutParams(layoutParams);
        }
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder {

        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ItemHandler {

        public void imageViewClick(View v) {
            if (mListener != null) {
                String path = (String) v.getTag(R.id.single_path_key);
                mListener.onImageViewClick(path);
            }
        }

        public void checkBoxClick(View v) {
            if (mListener != null) {
                String path = (String) v.getTag(R.id.single_path_key);
                mListener.onCheckBoxClick(v, path);
            }
        }
    }

    interface OnItemClickListener {
        void onImageViewClick(String str);

        void onCheckBoxClick(View v, String path);
    }
}
