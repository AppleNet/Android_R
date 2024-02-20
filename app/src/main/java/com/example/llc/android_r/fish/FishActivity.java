package com.example.llc.android_r.fish;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.llc.android_r.R;

/**
 * com.example.llc.android_r.fish.FishActivity
 *
 * @author liulongchao
 * @since 2024/2/5
 */
public class FishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish);

        // ImageView imageView = findViewById(R.id.imageView);
        //imageView.setImageDrawable(new Fish());
        // imageView.setImageDrawable(new FishDrawable());
    }
}
