package com.example.llc.android_r.rv;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llc.android_r.R;

import java.util.ArrayList;
import java.util.List;

/**
 * com.example.llc.android_r.rv.RecyclerViewActivity
 *
 * @author liulongchao
 * @since 2024/1/12
 */
public class RecyclerViewActivity extends AppCompatActivity {

    private List<NBAStar> starList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        init();
        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 自定义分割线
        recyclerView.addItemDecoration(new NBAStarDecoration(this));
        recyclerView.setAdapter(new NBAStarAdapter(this, starList));
    }


    private void init() {
        starList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 20; j++) {
                if (i % 2 == 0) {
                    starList.add(new NBAStar("詹姆斯" + j, "现役球星" + i));
                } else {
                    starList.add(new NBAStar("科比" + j, "退役球星" + i));
                }
            }
        }
    }
}
