package com.cxsplay.imageselect;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cxsplay.imageselect.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main);
        bind.setHandler(new MainHandler(this));
    }

    public class MainHandler {

        private Context context;

        MainHandler(Context context) {
            this.context = context;
        }

        public void imageSelect(View v) {
            context.startActivity(new Intent(context, ImagesActivity.class));
        }
    }
}
