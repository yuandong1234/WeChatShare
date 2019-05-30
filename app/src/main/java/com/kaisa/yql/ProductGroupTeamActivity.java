package com.kaisa.yql;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductGroupTeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_group_team);

        initView();
    }

    private TabLayout tabLayout;
    private ViewPager viewpager;

    private ArrayList<Fragment> fragments;
    private FragmentManager fragmentManager;
    private ArrayList<String> titles;


    private void initView() {
        tabLayout = findViewById(R.id.tabLayout);
        viewpager = findViewById(R.id.viewpager);


        fragmentManager = getSupportFragmentManager();
        fragments = new ArrayList<>();
        titles = new ArrayList<>();

        initTabs();
    }


    private void initTabs() {
        titles.clear();
        titles.add(getString(R.string.mall_product_group_all));
        titles.add(getString(R.string.mall_product_group_going));
        titles.add(getString(R.string.mall_product_group_success));
        titles.add(getString(R.string.mall_product_group_end));

        for (String title : titles) {
            ProductGroupTeamFragment fragment = new ProductGroupTeamFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ProductGroupTeamFragment.TYPE, title);
            fragment.setArguments(bundle);

            fragments.add(fragment);
        }

        ProductPageAdapter adapter = new ProductPageAdapter(fragmentManager, fragments, titles);
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);

        //自定义view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View view = LayoutInflater.from(this).inflate(R.layout.layout_group_team_tab, null);

                TextView tv_tab = view.findViewById(R.id.title);
                tv_tab.setGravity(Gravity.CENTER);
                tv_tab.setText(titles.get(i));
                tab.setCustomView(view);
            }
        }
    }


}
