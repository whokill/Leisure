package cn.linghouse.Adapter;
/*
 *Create by on 2018/12/18
 *Author:Linghouse
 *describe:
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.linghouse.Entity.All_Order_Entity;
import cn.linghouse.leisure.R;

public class My_All_Order_Adapter extends BaseAdapter {
    private Context context;
    private List<All_Order_Entity> order_entity;

    public My_All_Order_Adapter(Context context, List<All_Order_Entity> order_entity) {
        this.context = context;
        this.order_entity = order_entity;
    }

    @Override
    public int getCount() {
        return order_entity.size();
    }

    @Override
    public Object getItem(int position) {
        return order_entity.size();
        //return order_entity == null ? 0 : order_entity.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.my_all_order_item,null);
            holder = new ViewHolder();
            holder.all_order_pic = convertView.findViewById(R.id.all_order_item_pic);
            holder.all_order_name = convertView.findViewById(R.id.all_order_item_name);
            holder.all_order_price = convertView.findViewById(R.id.all_order_item_pice);
            holder.all_order_label1 = convertView.findViewById(R.id.all_order_item_label1);
            holder.all_order_label2 = convertView.findViewById(R.id.all_order_item_label2);
            holder.all_order_sold = convertView.findViewById(R.id.all_order_item_sold);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        All_Order_Entity orderEntity = order_entity.get(position);
        Glide.with(context)
                .load(orderEntity.getPicurl())
                .centerCrop()
                .placeholder(R.mipmap.logo)
                .dontAnimate()
                .into(holder.all_order_pic);
        holder.all_order_name.setText(orderEntity.getTitle());
        holder.all_order_price.setText(orderEntity.getPrice());
        holder.all_order_label1.setText(orderEntity.getLabel1());
        holder.all_order_label2.setText(orderEntity.getLabel2());
        holder.all_order_sold.setText(orderEntity.getTradstates());
        return convertView;
    }

    public class ViewHolder {
        ImageView all_order_pic;
        TextView all_order_name;
        TextView all_order_label1;
        TextView all_order_label2;
        TextView all_order_price;
        TextView all_order_sold;
    }
}
