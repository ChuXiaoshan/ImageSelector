package com.cxsplay.imageselect;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cxsplay.imageselect.databinding.ActivityMainBinding;
import com.cxsplay.imageselect.util.CustomBinder;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding bind;

    private static int REQUEST_CODE_SINGLE_IMAGE = 0x001;

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
        }
    }

    public class MainHandler {

        public void imageSelect(View v) {
            //            startActivity(new Intent(MainActivity.this, ImagesActivity.class));
            Intent intent = new Intent(MainActivity.this, ImagesActivity.class);
            intent.putExtra(ImagesActivity.SELECT_TYPE, 1);
            startActivityForResult(intent, REQUEST_CODE_SINGLE_IMAGE);

        }
    }

}
