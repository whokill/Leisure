package cn.linghouse.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.linghouse.leisure.Entity.Shopping_Address_Entity;
import cn.linghouse.leisure.R;

public class Shopping_Address_Adapter extends RecyclerView.Adapter<Shopping_Address_Adapter.MyHolder>{
    private Context context;
    private List<Shopping_Address_Entity> address_entity;

    public Shopping_Address_Adapter(Context context,List<Shopping_Address_Entity> entities){
        this.context = context;
        this.address_entity = entities;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shopping_address_item,parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Shopping_Address_Entity entity = address_entity.get(position);
        holder.item_probably_address.setText(entity.getProbably_address());
        holder.item_detail_address.setText(entity.getDetail_address());
    }

    @Override
    public int getItemCount() {
        return address_entity.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private TextView item_probably_address,item_detail_address;
        public MyHolder(View itemView) {
            super(itemView);
            item_probably_address = itemView.findViewById(R.id.tv_item_probably_address);
            item_detail_address = itemView.findViewById(R.id.tv_item_detail_address);
        }
    }
}
