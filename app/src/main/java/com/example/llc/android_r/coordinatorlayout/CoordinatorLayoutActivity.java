package com.example.llc.android_r.coordinatorlayout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.llc.android_r.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

/**
 * com.example.llc.android_r.coordinatorlayout.CoordinatorLayoutActivity
 *
 * @author liulongchao
 * @since 2024/1/30
 */
public class CoordinatorLayoutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private List<Fragment> fragmentList;
    private List<String> titles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        appBarLayout = findViewById(R.id.appbar);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab);
        // setSupportActionBar(this.toolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle("CoordinatorLayout");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()){
                    collapsingToolbarLayout.setTitle("CoordinatorLayout");
                } else {
                    collapsingToolbarLayout.setTitle("");
                }
            }
        });

        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        fragmentList = new ArrayList<>();
        fragmentList.add(OneFragment.newIntance());
        fragmentList.add(TwoFragment.newIntance());
        fragmentList.add(ThreeFragment.newIntance());
        fragmentList.add(FourFragment.newIntance());
        titles = new ArrayList<>();
        titles.add("One");
        titles.add("Two");
        titles.add("Three");
        titles.add("Four");

        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(this,fragmentList);
        viewPager.setAdapter(myFragmentAdapter);


        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles.get(position));
            }
        }).attach();
    }
}
