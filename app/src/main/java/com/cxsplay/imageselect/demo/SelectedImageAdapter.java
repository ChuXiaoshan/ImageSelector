package com.cxsplay.imageselect.demo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cxsplay.imageselect.R;
import com.cxsplay.imageselect.databinding.ItemSelectedRvBinding;

import java.util.List;

/**
 * Created by chuxiaoshan on 16/12/8.
 * ImageAdapter
 */

public class SelectedImageAdapter extends RecyclerView.Adapter<SelectedImageAdapter.MyViewHolder> {

    private LayoutInflater mInflater;

    private List<String> selectedImages;

    private int screenWidth;

    public SelectedImageAdapter(Context context, List<String> selectedImages) {
        mInflater = LayoutInflater.from(context);
        this.selectedImages = selectedImages;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_selected_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind.setPath(selectedImages.get(position));
    }

    @Override
    public int getItemCount() {
        return selectedImages == null ? 0 : selectedImages.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ItemSelectedRvBinding bind;

        MyViewHolder(View itemView) {
            super(itemView);
            bind = DataBindingUtil.bind(itemView);
            ViewGroup.LayoutParams layoutParams = bind.ivItemSelectedImage.getLayoutParams();
            layoutParams.width = screenWidth / 3;
            layoutParams.height = screenWidth / 3;
            bind.ivItemSelectedImage.setLayoutParams(layoutParams);
        }
    }
}
