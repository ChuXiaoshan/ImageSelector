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

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_rv, parent, false);
        return new MyViewHolder(view);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String path;
        if (TextUtils.isEmpty(folderPath)) {
            path = mImgPaths.get(position);
        } else {
            path = folderPath + "/" + mImgPaths.get(position);
        }
        this.path = path;
        if (selectedImages.contains(path)) {
            holder.bind.cbImage.setChecked(true);
        } else {
            holder.bind.cbImage.setChecked(false);
        }
        holder.bind.setPath(path);
        holder.bind.ivItemImage.setTag(R.id.single_path_key, path);
        holder.bind.cbImage.setTag(R.id.single_path_key, path);
    }

    @Override
    public int getItemCount() {
        return mImgPaths == null ? 0 : mImgPaths.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

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
