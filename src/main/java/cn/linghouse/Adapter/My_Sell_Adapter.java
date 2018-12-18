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

import cn.linghouse.Entity.My_Sell_Entity;
import cn.linghouse.leisure.R;

public class My_Sell_Adapter extends BaseAdapter {
    private Context context;
    private List<My_Sell_Entity> entity;

    public My_Sell_Adapter(Context context, List<My_Sell_Entity> entity) {
        this.context = context;
        this.entity = entity;
    }

    @Override
    public int getCount() {
        return entity.size();
    }

    @Override
    public Object getItem(int position) {
        return entity.get(position);
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
            convertView = inflater.inflate(R.layout.my_sell_item,null);
            holder = new ViewHolder();
            holder.my_sell_picurl = convertView.findViewById(R.id.my_sell_item_pic);
            holder.my_sell_name = convertView.findViewById(R.id.my_sell_item_name);
            holder.my_sell_price = convertView.findViewById(R.id.my_sell_item_pice);
            holder.my_sell_label1 = convertView.findViewById(R.id.my_sell_item_label1);
            holder.my_sell_label2 = convertView.findViewById(R.id.my_sell_item_label2);
            holder.my_sell_tradstates = convertView.findViewById(R.id.my_sell_item_sold);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        My_Sell_Entity sell_entity = entity.get(position);
        Glide.with(context)
                .load(sell_entity.getPicurl())
                .centerCrop()
                .placeholder(R.mipmap.logo)
                .dontAnimate()
                .into(holder.my_sell_picurl);
        holder.my_sell_name.setText(sell_entity.getTitle());
        holder.my_sell_price.setText(sell_entity.getPrice());
        holder.my_sell_label1.setText(sell_entity.getLabel1());
        holder.my_sell_label2.setText(sell_entity.getLabel2());
        holder.my_sell_tradstates.setText(sell_entity.getTradstates());
        return convertView;
    }

    public class ViewHolder {
        ImageView my_sell_picurl;
        TextView my_sell_name;
        TextView my_sell_price;
        TextView my_sell_label1;
        TextView my_sell_label2;
        TextView my_sell_tradstates;
    }
}
