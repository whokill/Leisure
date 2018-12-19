package cn.linghouse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;
import java.util.UUID;

import cn.linghouse.App.Config;
import cn.linghouse.Entity.Index_Pic_Entity;
import cn.linghouse.UI.GoodDetailsActivity;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;
import okhttp3.Call;

import static android.content.Context.MODE_PRIVATE;

public class Index_Adapter extends BaseAdapter {
    private Context context;
    private Intent intent = new Intent();
    private List<Index_Pic_Entity> pic_entity;

    public Index_Adapter(Context context, List<Index_Pic_Entity> entity) {
        this.context = context;
        this.pic_entity = entity;
    }

    @Override
    public int getCount() {
        if (pic_entity == null) {
            return 0;
        } else {
            return pic_entity.size() % 2 == 0 ? pic_entity.size() / 2 : pic_entity.size() / 2 + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return pic_entity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.activity_index_item, null);
            viewHolder = new ViewHolder();
            viewHolder.item_image1 = convertView.findViewById(R.id.iv_item_image1);
            viewHolder.item_image2 = convertView.findViewById(R.id.iv_item_image2);
            viewHolder.item_title1 = convertView.findViewById(R.id.tv_item_title1);
            viewHolder.item_title2 = convertView.findViewById(R.id.tv_item_title2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!pic_entity.get(position * 2).equals(viewHolder.item_image1.getTag(R.id.iv_item_image1))) {
            viewHolder.item_title1.setText(pic_entity.get(position * 2).getTitle());
            Glide.with(context)
                    .load(pic_entity.get(position * 2).getPic_url())
                    .placeholder(R.mipmap.logo)
                    .dontAnimate()
                    .centerCrop()
                    .into(viewHolder.item_image1);
            if (position * 2 + 1 >= pic_entity.size()) {
                viewHolder.item_image2.setVisibility(View.INVISIBLE);
                viewHolder.item_title2.setVisibility(View.INVISIBLE);
            } else if (!pic_entity.get(position * 2 + 1).equals(viewHolder.item_image1.getTag(R.id.iv_item_image2))) {
                viewHolder.item_image2.setVisibility(View.VISIBLE);
                viewHolder.item_title2.setVisibility(View.VISIBLE);
                viewHolder.item_title2.setText(pic_entity.get(position * 2 + 1).getTitle());
                Glide.with(context)
                        .load(pic_entity.get(position * 2 + 1).getPic_url())
                        .placeholder(R.mipmap.logo)
                        .dontAnimate()
                        .centerCrop()
                        .into(viewHolder.item_image2);
                viewHolder.item_image1.setTag(R.id.iv_item_image1, pic_entity.get(position * 2 + 1));
            }
            viewHolder.item_image1.setTag(R.id.iv_item_image1, pic_entity.get(position * 2));
        }

        viewHolder.item_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("title", pic_entity.get(position * 2).getTitle());
                intent.putExtra("price", pic_entity.get(position * 2).getPrice());
                intent.putExtra("details", pic_entity.get(position * 2).getDetail());
                intent.putExtra("imagelist", pic_entity.get(position * 2).getImages());
                intent.putExtra("cnumber", pic_entity.get(position * 2).getCnumber());
                intent.setClass(context, GoodDetailsActivity.class);
                context.startActivity(intent);
            }
        });

        viewHolder.item_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("title", pic_entity.get(position * 2 + 1).getTitle());
                intent.putExtra("price", pic_entity.get(position * 2 + 1).getPrice());
                intent.putExtra("details", pic_entity.get(position * 2 + 1).getDetail());
                intent.putExtra("imagelist", pic_entity.get(position * 2 + 1).getImages());
                intent.putExtra("cnumber", pic_entity.get(position * 2 + 1).getCnumber());
                intent.setClass(context, GoodDetailsActivity.class);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        ImageView item_image1;
        ImageView item_image2;
        TextView item_title1;
        TextView item_title2;
    }
}
