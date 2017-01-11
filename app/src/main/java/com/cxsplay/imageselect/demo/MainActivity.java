package com.cxsplay.imageselect.demo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cxsplay.imageselect.ImagesActivity;
import com.cxsplay.imageselect.R;
import com.cxsplay.imageselect.databinding.ActivityMainBinding;
import com.cxsplay.imageselect.util.CustomBinder;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static int REQUEST_CODE_SINGLE_IMAGE = 0x001;

    private static int REQUEST_CODE_MULIT_IMAGE = 0x002;

    private static int REQUEST_CODE_CROP_IMAGE = 0x003;

    private ActivityMainBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main);
        bind.setHandler(new MainHandler());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_SINGLE_IMAGE) {
            String path = data.getStringExtra(ImagesActivity.RETURN_KEY);
            CustomBinder.loadImage(bind.ivShow1, path, null, null);
        } else if (requestCode == REQUEST_CODE_MULIT_IMAGE) {
            ArrayList<String> images = data.getStringArrayListExtra(ImagesActivity.RETURN_KEY);
            bind.rvSelectImages.setAdapter(new SelectedImageAdapter(this, images));
        } else if (requestCode == REQUEST_CODE_CROP_IMAGE) {
            String path = data.getStringExtra(ImagesActivity.RETURN_KEY);
            Uri uri = Uri.fromFile(new File(path));
            Uri destination = Uri.fromFile(new File(getCacheDir(), System.currentTimeMillis() + ".jpg"));
            Crop.of(uri, destination).asSquare().start(this);
        } else {
            CustomBinder.loadImage(bind.ivShow1, Crop.getOutput(data), null, null);
        }
    }

    public class MainHandler {

        public void imageSingle(View v) {
            Intent intent = new Intent(MainActivity.this, ImagesActivity.class);
            intent.putExtra(ImagesActivity.SELECT_TYPE, ImagesActivity.TYPE_SINGLE);
            startActivityForResult(intent, REQUEST_CODE_SINGLE_IMAGE);
            bind.rvSelectImages.setVisibility(View.GONE);
            bind.ivShow1.setVisibility(View.VISIBLE);
        }

        public void imageMulit(View v) {
            Intent intent = new Intent(MainActivity.this, ImagesActivity.class);
            intent.putExtra(ImagesActivity.SELECT_TYPE, ImagesActivity.TYPE_MULIT);
            intent.putExtra(ImagesActivity.SELECT_LIMIT, 9);
            startActivityForResult(intent, REQUEST_CODE_MULIT_IMAGE);
            bind.rvSelectImages.setVisibility(View.VISIBLE);
            bind.ivShow1.setVisibility(View.GONE);
        }

        public void imageCrop(View v) {
            Intent intent = new Intent(MainActivity.this, ImagesActivity.class);
            intent.putExtra(ImagesActivity.SELECT_TYPE, ImagesActivity.TYPE_SINGLE);
            startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
            bind.rvSelectImages.setVisibility(View.GONE);
            bind.ivShow1.setVisibility(View.VISIBLE);
        }
    }
}
