package cn.linghouse.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.linghouse.Entity.Recommend_Entity;
import cn.linghouse.leisure.R;

public class Recommend_Adapter extends RecyclerView.Adapter<Recommend_Adapter.MyViewHolder>{
    private List<Recommend_Entity> recommend_entity;
    private Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recommend_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return recommend_entity.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView recommend_image1;
        private ImageView recommend_image2;
        private TextView recommend_title1;
        private TextView recommend_title2;
        public MyViewHolder(View itemView) {
            super(itemView);

        }
    }
}
