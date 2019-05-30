package com.kaisa.yql;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProductGroupActivity extends AppCompatActivity {

    private RelativeLayout rl_product_group_rule;
    private RelativeLayout rl_product_group_count_down;
    private TextView tv_bargain_countdown_title;
    private TextView tv_bargain_countdown_h;
    private TextView tv_bargain_countdown_m;
    private TextView tv_bargain_countdown_s;
    private LinearLayout ll_product_group_list;
    private TextView tv_product_group_number;
    private TextView tv_see_total_group;
    private RecyclerView recyclerview_group_list;
    private TextView tv_product_group_empty;

    private ProductGroupListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_group);
        initView();
        initData();
    }

    private void initView() {
        rl_product_group_rule = (RelativeLayout) findViewById(R.id.rl_product_group_rule);
        rl_product_group_count_down = (RelativeLayout) findViewById(R.id.rl_product_group_count_down);
        tv_bargain_countdown_title = (TextView) findViewById(R.id.tv_bargain_countdown_title);
        tv_bargain_countdown_h = (TextView) findViewById(R.id.tv_bargain_countdown_h);
        tv_bargain_countdown_m = (TextView) findViewById(R.id.tv_bargain_countdown_m);
        tv_bargain_countdown_s = (TextView) findViewById(R.id.tv_bargain_countdown_s);
        ll_product_group_list = (LinearLayout) findViewById(R.id.ll_product_group_list);
        tv_product_group_number = (TextView) findViewById(R.id.tv_product_group_number);
        tv_see_total_group = (TextView) findViewById(R.id.tv_see_total_group);
        recyclerview_group_list = (RecyclerView) findViewById(R.id.recyclerview_group_list);
        tv_product_group_empty = (TextView) findViewById(R.id.tv_product_group_empty);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerview_group_list.setLayoutManager(layoutManager);
        mAdapter = new ProductGroupListAdapter(this);
        recyclerview_group_list.setAdapter(mAdapter);
    }

    private void initData() {
        List<GroupBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GroupBean bean = new GroupBean();
            bean.name = "X战警" + i;
            bean.countdown = "23小时22分钟后结束" + i;
            bean.numberLeft = "还差3人成团" + i;
            List<GroupBean.Member> members = new ArrayList<>();
            for (int n = 0; n < 4; n++) {
                GroupBean.Member member = new GroupBean.Member();
                member.name = "member" + n;
                member.avatar = "";
                members.add(member);
            }
            bean.members = members;
            list.add(bean);
        }
        mAdapter.setData(list);
    }

}
