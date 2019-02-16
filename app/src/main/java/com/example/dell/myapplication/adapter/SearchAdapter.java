package com.example.dell.myapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.myapplication.R;
import com.example.dell.myapplication.bean.Query;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;
    private List<Query.ResultBean> list;

    public SearchAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }

    public void setList(List<Query.ResultBean> list) {
        //this.list.clear();
        this.list = list;
        notifyDataSetChanged();
    }
    public void addList(List<Query.ResultBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder viewHolder, final int i) {
        ViewHolder viewHolder1 = (ViewHolder) viewHolder;
        viewHolder1.search_title.setText(list.get(i).getCommodityName());
        viewHolder1.search_price.setText(list.get(i).getPrice()+"");
        Uri parse = Uri.parse(list.get(i).getMasterPic());
        viewHolder1.search_image.setImageURI(parse);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListrn!=null){
                    onClickListrn.getdata(list.get(i).getCommodityId()+"");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView search_image;
        TextView search_title,search_price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            search_image = itemView.findViewById(R.id.search_image);
            search_title = itemView.findViewById(R.id.search_title);
            search_price = itemView.findViewById(R.id.search_price);
        }
    }
    //接口回调
    private OnClickListrn onClickListrn;
    public interface OnClickListrn{
      void getdata(String id);
    }
    public void  setOnClickListrner(OnClickListrn monClickListrn){
        onClickListrn=monClickListrn;
    }
}
