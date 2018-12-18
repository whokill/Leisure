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

import cn.linghouse.Entity.My_Release_Entity;
import cn.linghouse.leisure.R;

public class My_Release_Adapter extends BaseAdapter {
    private Context context;
    private List<My_Release_Entity> listentity;

    public My_Release_Adapter(Context context, List<My_Release_Entity> listentity) {
        this.context = context;
        this.listentity = listentity;
    }

    @Override
    public int getCount() {
        return listentity.size();
    }

    @Override
    public Object getItem(int position) {
        return listentity.get(position);
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
            convertView = inflater.inflate(R.layout.my_release_item,null);
            holder = new ViewHolder();
            holder.my_release_picurl = convertView.findViewById(R.id.my_release_item_pic);
            holder.my_release_name = convertView.findViewById(R.id.my_release_item_name);
            holder.my_release_price = convertView.findViewById(R.id.my_release_item_pice);
            holder.my_release_label1 = convertView.findViewById(R.id.my_release_item_label1);
            holder.my_release_label2 = convertView.findViewById(R.id.my_release_item_label2);
            holder.my_release_date = convertView.findViewById(R.id.my_release_item_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        My_Release_Entity release_entity = listentity.get(position);
        Glide.with(context)
                .load(release_entity.getPicurl())
                .dontAnimate()
                .placeholder(R.mipmap.logo)
                .centerCrop()
                .into(holder.my_release_picurl);
        holder.my_release_price.setText(release_entity.getPrice());
        holder.my_release_name.setText(release_entity.getName());
        holder.my_release_label1.setText(release_entity.getLabel1());
        holder.my_release_label2.setText(release_entity.getLabel2());
        holder.my_release_date.setText(release_entity.getDate());
        return convertView;
    }

    public class ViewHolder {
        ImageView my_release_picurl;
        TextView my_release_name;
        TextView my_release_price;
        TextView my_release_label1;
        TextView my_release_label2;
        TextView my_release_date;
    }
}
