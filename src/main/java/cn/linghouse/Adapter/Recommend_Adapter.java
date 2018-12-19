package cn.linghouse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.linghouse.Entity.Recommend_Entity;
import cn.linghouse.UI.GoodDetailsActivity;
import cn.linghouse.leisure.R;

public class Recommend_Adapter extends BaseAdapter {
    private List<Recommend_Entity> recommend_entity;
    private Context context;

    public Recommend_Adapter(List<Recommend_Entity> recommend_entity, Context context) {
        this.recommend_entity = recommend_entity;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (recommend_entity == null) {
            return 0;
        } else {
            return recommend_entity.size() % 2 == 0 ? recommend_entity.size() / 2 : recommend_entity.size() / 2 + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return recommend_entity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.recommend_item, null);
            viewHolder = new ViewHolder();
            viewHolder.recommend_image1 = convertView.findViewById(R.id.iv_recommend_item_image1);
            viewHolder.recommend_image2 = convertView.findViewById(R.id.iv_recommend_item_image2);
            viewHolder.recommend_title1 = convertView.findViewById(R.id.tv_recommend_item_title1);
            viewHolder.recommend_title2 = convertView.findViewById(R.id.tv_recommend_item_title2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!recommend_entity.get(position * 2).equals(viewHolder.recommend_image1.getTag(R.id.iv_recommend_item_image1))) {
            viewHolder.recommend_title1.setText(recommend_entity.get(position * 2).getRecommed_title());
            Glide.with(context)
                    .load(recommend_entity.get(position * 2).getRecommed_pic_url())
                    .placeholder(R.mipmap.logo)
                    .dontAnimate()
                    .centerCrop()
                    .into(viewHolder.recommend_image1);
            if (position * 2 + 1 >= recommend_entity.size()) {
                viewHolder.recommend_image2.setVisibility(View.INVISIBLE);
                viewHolder.recommend_title2.setVisibility(View.INVISIBLE);
            } else if (!recommend_entity.get(position * 2 + 1).equals(viewHolder.recommend_image1.getTag(R.id.iv_recommend_item_image2))) {
                viewHolder.recommend_image2.setVisibility(View.VISIBLE);
                viewHolder.recommend_title2.setVisibility(View.VISIBLE);
                viewHolder.recommend_title2.setText(recommend_entity.get(position * 2 + 1).getRecommed_title());
                Glide.with(context)
                        .load(recommend_entity.get(position * 2 + 1).getRecommed_pic_url())
                        .placeholder(R.mipmap.logo)
                        .dontAnimate()
                        .centerCrop()
                        .into(viewHolder.recommend_image2);
                viewHolder.recommend_image1.setTag(R.id.iv_recommend_item_image1, recommend_entity.get(position * 2 + 1));
            }
            viewHolder.recommend_image1.setTag(R.id.iv_recommend_item_image1, recommend_entity.get(position * 2));
        }

        viewHolder.recommend_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("title", recommend_entity.get(position * 2).getRecommed_title());
                intent.putExtra("price", recommend_entity.get(position * 2).getRecommed_price());
                intent.putExtra("details", recommend_entity.get(position * 2).getRecommed_detail());
                intent.putExtra("cnumber", recommend_entity.get(position * 2).getCnumber());
                intent.putExtra("imagelist", recommend_entity.get(position * 2).getRecommed_images());
                intent.setClass(context, GoodDetailsActivity.class);
                context.startActivity(intent);
            }
        });

        viewHolder.recommend_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("title", recommend_entity.get(position * 2 + 1).getRecommed_title());
                intent.putExtra("price", recommend_entity.get(position * 2 + 1).getRecommed_price());
                intent.putExtra("details", recommend_entity.get(position * 2 + 1).getRecommed_detail());
                intent.putExtra("cnumber", recommend_entity.get(position * 2 + 1).getCnumber());
                intent.putExtra("imagelist", recommend_entity.get(position * 2 + 1).getRecommed_images());
                intent.setClass(context, GoodDetailsActivity.class);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        private ImageView recommend_image1;
        private ImageView recommend_image2;
        private TextView recommend_title1;
        private TextView recommend_title2;
    }

}
