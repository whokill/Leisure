package cn.linghouse.Adapter;
/*
 *Create by on 2018/12/25
 *Author:Linghouse
 *describe:分类搜索商品实体类
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

import cn.linghouse.Entity.Sort_Entity;
import cn.linghouse.leisure.R;

public class SortAdapter extends BaseAdapter {
    private List<Sort_Entity> entityList;
    private Context context;

    public SortAdapter(List<Sort_Entity> entityList, Context context) {
        this.entityList = entityList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return entityList == null ? 0 : entityList.size();
    }

    @Override
    public Object getItem(int position) {
        return entityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.activity_sort_item,null);
            holder = new ViewHolder();
            holder.picurl = convertView.findViewById(R.id.sort_item_pic);
            holder.pice = convertView.findViewById(R.id.sort_item_pice);
            holder.title = convertView.findViewById(R.id.sort_item_name);
            holder.label1 = convertView.findViewById(R.id.sort_item_label1);
            holder.label2 = convertView.findViewById(R.id.sort_item_label2);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Sort_Entity entity = entityList.get(position);
        Glide.with(context)
                .load(entity.getPicurl())
                .dontAnimate()
                .centerCrop()
                .placeholder(R.mipmap.logo)
                .into(holder.picurl);
        holder.title.setText(entity.getName());
        holder.pice.setText(entity.getPice());
        holder.label1.setText(entity.getLabel1());
        holder.label2.setText(entity.getLabel2());
        return convertView;
    }

    public class ViewHolder{
        ImageView picurl;
        TextView title;
        TextView pice;
        TextView label1;
        TextView label2;
    }
}
