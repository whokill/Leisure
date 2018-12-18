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

import cn.linghouse.Entity.My_Buy_Entity;
import cn.linghouse.leisure.R;

public class My_Buy_Adapter extends BaseAdapter {
    private Context context;
    private List<My_Buy_Entity> buy_entityList;

    public My_Buy_Adapter(Context context, List<My_Buy_Entity> buy_entityList) {
        this.context = context;
        this.buy_entityList = buy_entityList;
    }

    @Override
    public int getCount() {
        return buy_entityList.size();
    }

    @Override
    public Object getItem(int position) {
        return buy_entityList.get(position);
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
            convertView = inflater.inflate(R.layout.my_buy_item, null);
            holder = new ViewHolder();
            holder.my_buy_picurl = convertView.findViewById(R.id.my_buy_item_pic);
            holder.my_buy_name = convertView.findViewById(R.id.my_buy_item_name);
            holder.my_buy_price = convertView.findViewById(R.id.my_buy_item_pice);
            holder.my_buy_lable1 = convertView.findViewById(R.id.my_buy_item_label1);
            holder.my_buy_label2 = convertView.findViewById(R.id.my_buy_item_label2);
            holder.my_buy_date = convertView.findViewById(R.id.my_buy_item_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        My_Buy_Entity entity = buy_entityList.get(position);
        Glide.with(context)
                .load(entity.getMy_buy_picurl())
                .dontAnimate()
                .centerCrop()
                .placeholder(R.mipmap.logo)
                .into(holder.my_buy_picurl);
        holder.my_buy_name.setText(entity.getMy_buy_name());
        holder.my_buy_price.setText(entity.getMy_buy_price());
        holder.my_buy_lable1.setText(entity.getMy_buy_label1());
        holder.my_buy_label2.setText(entity.getMy_buy_label2());
        holder.my_buy_date.setText(entity.getMy_buy_date());
        return convertView;
    }

    public class ViewHolder {
        ImageView my_buy_picurl;
        TextView my_buy_name;
        TextView my_buy_price;
        TextView my_buy_lable1;
        TextView my_buy_label2;
        TextView my_buy_date;
    }
}
