package com.kaisa.yql;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ProductGroupMemberAdapter extends RecyclerView.Adapter<ProductGroupMemberAdapter.ViewHolder> {

    private Context mContext;
    private List<GroupBean.Member> list = new ArrayList<>();
    private LayoutInflater inflater;

    public ProductGroupMemberAdapter(Context context) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_product_group_member_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        GroupBean.Member bean = list.get(position);
        if (!TextUtils.isEmpty(bean.avatar)) {

        }
    }

    public void setData(List<GroupBean.Member> members) {
        this.list = members;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }
}
