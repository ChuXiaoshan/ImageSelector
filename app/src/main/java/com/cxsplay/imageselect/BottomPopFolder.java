package com.cxsplay.imageselect;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.cxsplay.imageselect.bean.FolderBean;
import com.cxsplay.imageselect.databinding.BottomSheetLayoutBinding;

import java.util.List;

/**
 * Created by chuxiaoshan on 16/12/12.
 * BottomPopFolder
 */

public class BottomPopFolder extends BottomSheetDialogFragment {

    private OnFolderItemClickListener listener;

    private BottomSheetLayoutBinding bind;

    private List<FolderBean> listFolder;

    public BottomPopFolder() {
    }

    @SuppressLint("ValidFragment")
    public BottomPopFolder(List<FolderBean> listFolder) {
        this.listFolder = listFolder;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback
            = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        bind = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_layout, null, false);
        dialog.setContentView(bind.getRoot());

        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) ((View) bind.getRoot().getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        initData();
    }

    public void setOnFolderItemClickListener(OnFolderItemClickListener listener) {
        this.listener = listener;
    }

    private void initData() {
        FolderAdapter adapter = new FolderAdapter(getContext(), listFolder);
        bind.rvFolder.setAdapter(adapter);
        adapter.setOnItemClickListener(new FolderAdapter.OnItemClickListener() {
            @Override
            public void onViewClick(FolderBean folderBean) {
                if (listener == null) {
                    return;
                }
                listener.folderItemClick(folderBean);
                Toast.makeText(getContext(), "dddd" + folderBean.getDir(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnFolderItemClickListener {
        void folderItemClick(FolderBean folderBean);
    }
}
