package com.kaisa.yql;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 开团列表适配器
 */
public class ProductGroupListAdapter extends RecyclerView.Adapter<ProductGroupListAdapter.ViewHolder> {
    private Context mContext;
    private List<GroupBean> list = new ArrayList<>();
    private LayoutInflater inflater;

    public ProductGroupListAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_product_group_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        final GroupBean bean = list.get(position);
        viewHolder.tv_name.setText(bean.name);
        viewHolder.tv_count_down.setText(bean.countdown);
        viewHolder.tv_group_number_left.setText(bean.numberLeft);
        if (position == list.size() - 1) {
            viewHolder.divider.setVisibility(View.GONE);
        } else {
            viewHolder.divider.setVisibility(View.VISIBLE);
        }
        viewHolder.tv_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(bean);
            }
        });
    }

    public void setData(List<GroupBean> groupBeanList) {
        if (groupBeanList != null) {
            list = groupBeanList;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_avatar;
        public TextView tv_name;
        public TextView tv_count_down;
        public TextView tv_group;
        public TextView tv_group_number_left;
        public View divider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_count_down = (TextView) itemView.findViewById(R.id.tv_count_down);
            tv_group = (TextView) itemView.findViewById(R.id.tv_group);
            tv_group_number_left = (TextView) itemView.findViewById(R.id.tv_group_number_left);
            divider = (View) itemView.findViewById(R.id.divider);
        }
    }

    private void showDialog(GroupBean bean) {
        CommonCenterDialog.Builder builder = new CommonCenterDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_mall_product_group_dialog, null);

        RelativeLayout rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
        TextView tv_bargain_countdown_title = (TextView) view.findViewById(R.id.tv_bargain_countdown_title);
        TextView tv_bargain_countdown_h = (TextView) view.findViewById(R.id.tv_bargain_countdown_h);
        TextView tv_bargain_countdown_m = (TextView) view.findViewById(R.id.tv_bargain_countdown_m);
        TextView tv_bargain_countdown_s = (TextView) view.findViewById(R.id.tv_bargain_countdown_s);
        TextView tv_product_group_title = (TextView) view.findViewById(R.id.tv_product_group_title);
        TextView tv_product_group_left = (TextView) view.findViewById(R.id.tv_product_group_left);
        RecyclerView recyclerview_member = (RecyclerView) view.findViewById(R.id.recyclerview_member);
        TextView tv_group = (TextView) view.findViewById(R.id.tv_group);

        //参团成员列表
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        recyclerview_member.setLayoutManager(layoutManager);
        ProductGroupMemberAdapter adapter = new ProductGroupMemberAdapter(mContext);
        recyclerview_member.setAdapter(adapter);
        adapter.setData(bean.members);


        final CommonCenterDialog dialog = builder.view(view)
                //.height(200)
                .style(R.style.theme_common_dialog)
                .cancelTouchOut(true)
                .build();

        tv_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.show();
    }
}
