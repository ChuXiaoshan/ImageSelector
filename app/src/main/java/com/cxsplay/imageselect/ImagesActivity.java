package com.cxsplay.imageselect;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.cxsplay.imageselect.bean.AcImagesBean;
import com.cxsplay.imageselect.bean.FolderBean;
import com.cxsplay.imageselect.databinding.ActivityImagesBinding;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImagesActivity extends AppCompatActivity {

    public static final int RQ_PERMISSION_WRITE_EXTERNAL_STORAGE = 0x000;

    private static final int DATA_LOADED = 0x001;

    private ProgressDialog progressDialog;

    private List<FolderBean> listFolder;

    private ActivityImagesBinding bind;

    private AcImagesBean imagesBean;

    private List<String> listAllImage;

    private ImageAdapter adapter;

    private BottomPopFolder bpFragment;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DATA_LOADED) {
                refreshData(listAllImage, "", "所有图片");
                progressDialog.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_images);
        imagesBean = new AcImagesBean();
        bind.setHandler(new ImagesHandler());
        bind.setBean(imagesBean);
        init();
        getPermission();
    }

    private void init() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        listAllImage = new ArrayList<>();
        listFolder = new ArrayList<>();
        adapter = new ImageAdapter(ImagesActivity.this);
        bind.rvBillingRecord.setAdapter(adapter);
        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onImageViewClick() {
                Toast.makeText(ImagesActivity.this, "onImageViewClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImageButtonClick() {
                Toast.makeText(ImagesActivity.this, "onImageButtonClick", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 向用户请求获取读写存储卡的权限
     */
    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            initData();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RQ_PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == RQ_PERMISSION_WRITE_EXTERNAL_STORAGE) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    initData();
                } else {
                    Toast.makeText(ImagesActivity.this, "使用此功能必须获取存储卡的权限",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        }
    }

    /**
     * 利用ContentProvider扫描手机中的所有图片
     */
    private void initData() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "当前存储卡不可用！", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog = ProgressDialog.show(this, null, "正在加载...");
        new Thread() {

            @Override
            public void run() {
                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = ImagesActivity.this.getContentResolver();
                Cursor cursor = cr.query(imageUri,
                        null,
                        MediaStore.Images.Media.MIME_TYPE + " = ? or " + MediaStore.Images.Media.MIME_TYPE + " = ?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                if (cursor == null) {
                    Toast.makeText(ImagesActivity.this, "未找到该类型的文件", Toast.LENGTH_SHORT).show();
                    return;
                }
                Set<String> mDirPaths = new HashSet<>();
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    listAllImage.add(path);
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null) {
                        continue;
                    }
                    String dirPath = parentFile.getAbsolutePath();
                    FolderBean folderBean;
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImagePath(path);
                    }
                    if (parentFile.list() == null) {
                        continue;
                    }
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File file, String s) {
                            return (s.endsWith(".jpg")
                                    || s.endsWith(".jpeg")
                                    || s.endsWith(".png"));
                        }
                    }).length;
                    folderBean.setCount(picSize);
                    listFolder.add(folderBean);
                }
                cursor.close();
                mHandler.sendEmptyMessage(DATA_LOADED);
            }
        }.start();
    }

    private void initBottomPop() {
        bpFragment = new BottomPopFolder(listFolder);
        bpFragment.setOnFolderItemClickListener(
                new BottomPopFolder.OnFolderItemClickListener() {
                    @Override
                    public void folderItemClick(FolderBean folderBean) {
                        File file = new File(folderBean.getDir());
                        List<String> listImage = Arrays.asList(file.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File file, String s) {
                                return s.endsWith(".jpg")
                                        || s.endsWith(".jpeg")
                                        || s.endsWith(".png");
                            }
                        }));
                        refreshData(listImage, file.getAbsolutePath(), folderBean.getName());
                        bpFragment.dismiss();

                        File[] files = file.listFiles();
                        System.out.println("----->" + files[0].getAbsolutePath());
                    }
                }
        );
    }

    /**
     * 刷新 recyclerView数据
     *
     * @param list       list
     * @param folderPath folderPath
     * @param folderName folderName
     */
    public void refreshData(List<String> list, String folderPath, String folderName) {
        adapter.setData(list, folderPath);
        adapter.notifyDataSetChanged();
        imagesBean.setItemCount(list.size());
        imagesBean.setCurrentFolderName(folderName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (bpFragment != null) {
            bpFragment.dismiss();
        }
    }

    public class ImagesHandler {

        public void showBottomPop(View v) {
            if (bpFragment == null) {
                initBottomPop();
            }
            bpFragment.show(getSupportFragmentManager(), bpFragment.getTag());
        }
    }
}