package cn.linghouse.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.linghouse.Entity.Search_Entity;
import cn.linghouse.leisure.R;

public class Search_Adapter extends BaseAdapter {
    private List<Search_Entity> search_entities;
    private Context context;

    public Search_Adapter(List<Search_Entity> search_entities, Context context) {
        this.search_entities = search_entities;
        this.context = context;
    }

    @Override
    public int getCount() {
        return search_entities == null ? 0 : search_entities.size();
    }

    @Override
    public Object getItem(int position) {
        return search_entities.get(position);
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
            convertView = inflater.inflate(R.layout.search_item,null);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.search_item_name);
            holder.pice = convertView.findViewById(R.id.search_item_pice);
            holder.score = convertView.findViewById(R.id.search_item_score);
            holder.detail = convertView.findViewById(R.id.search_item_data);
            holder.seller = convertView.findViewById(R.id.search_item_seller_name);
            holder.picurl = convertView.findViewById(R.id.search_item_pic);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Search_Entity entity = search_entities.get(position);
        Glide.with(context)
                .load("http://leisure.myosotis.cc/leisure/commodity/xujunwei/0b93a3f2efcc47498140714737845a77.jpg")
                .placeholder(R.mipmap.ic_launcher)
                .dontAnimate()
                .into(holder.picurl);
        holder.title.setText(entity.getName());
        holder.pice.setText(entity.getPice());
        holder.score.setText(entity.getScore());
        holder.detail.setText(entity.getDetail());
        holder.seller.setText(entity.getSeller());
        return convertView;
    }
    public class ViewHolder{
        ImageView picurl;
        TextView title;
        TextView pice;
        TextView score;
        TextView detail;
        TextView seller;
    }
}
