package cn.linghouse.leisure.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.linghouse.leisure.Entity.Search_Entity;
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
            holder.title = convertView.findViewById(R.id.search_item_title);
            holder.pice = convertView.findViewById(R.id.search_item_pice);
            holder.pinkage = convertView.findViewById(R.id.search_item_pinkage);
            holder.data = convertView.findViewById(R.id.search_item_data);
            holder.place = convertView.findViewById(R.id.search_item_place);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Search_Entity entity = search_entities.get(position);
        holder.title.setText(entity.getTitle());
        holder.pice.setText(entity.getPice());
        holder.pinkage.setText(entity.getPinkage());
        holder.data.setText(entity.getData());
        holder.place.setText(entity.getPlace());
        return convertView;
    }
    public class ViewHolder{
        TextView title;
        TextView pice;
        TextView pinkage;
        TextView data;
        TextView place;
    }
}
