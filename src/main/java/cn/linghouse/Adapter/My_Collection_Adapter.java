package cn.linghouse.Adapter;
/*
 *Create by on 2018/12/19
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

import cn.linghouse.Entity.My_Collection_Entity;
import cn.linghouse.leisure.R;

public class My_Collection_Adapter extends BaseAdapter {
    private Context context;
    private List<My_Collection_Entity> collection_list;

    public My_Collection_Adapter(Context context, List<My_Collection_Entity> collection_list) {
        this.context = context;
        this.collection_list = collection_list;
    }

    @Override
    public int getCount() {
        return collection_list.size();
    }

    @Override
    public Object getItem(int position) {
        return collection_list.get(position);
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
            convertView = inflater.inflate(R.layout.collection_item,null);
            holder = new ViewHolder();
            holder.my_collection_item_picurl = convertView.findViewById(R.id.my_collection_item_pic);
            holder.my_collection_item_name = convertView.findViewById(R.id.my_collection_item_name);
            holder.my_collection_item_details = convertView.findViewById(R.id.my_collection_item_details);
            holder.my_collection_price = convertView.findViewById(R.id.my_collection_item_price);
            holder.my_collection_sortname = convertView.findViewById(R.id.my_collection_item_sortname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        My_Collection_Entity entity = collection_list.get(position);
        Glide.with(context)
                .load(entity.getPicurl())
                .dontAnimate()
                .error(R.mipmap.logo)
                .placeholder(R.mipmap.logo)
                .centerCrop()
                .into(holder.my_collection_item_picurl);
        holder.my_collection_item_name.setText(entity.getName());
        holder.my_collection_item_details.setText(entity.getDetails());
        holder.my_collection_price.setText(entity.getPrice());
        holder.my_collection_sortname.setText(entity.getSortname());
        return convertView;
    }

    public class ViewHolder {
        ImageView my_collection_item_picurl;
        TextView my_collection_item_name;
        TextView my_collection_item_details;
        TextView my_collection_price;
        TextView my_collection_sortname;
    }
}
