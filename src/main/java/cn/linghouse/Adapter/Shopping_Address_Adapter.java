package cn.linghouse.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.linghouse.App.Config;
import cn.linghouse.Entity.Shopping_Address_Entity;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;
import okhttp3.Call;

import static android.content.Context.MODE_PRIVATE;

public class Shopping_Address_Adapter extends RecyclerView.Adapter<Shopping_Address_Adapter.MyHolder> {
    private Context context;
    ArrayList<Integer> check = new ArrayList<>();
    private String sessionid;
    private List<Shopping_Address_Entity> address_entity;

    public Shopping_Address_Adapter(Context context, List<Shopping_Address_Entity> entities) {
        SharedPreferences share = context.getSharedPreferences("Session", MODE_PRIVATE);
        sessionid = share.getString("sessionid", "null");
        this.context = context;
        this.address_entity = entities;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.shopping_address_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Shopping_Address_Entity entity = address_entity.get(position);
        holder.item_address.setText(entity.getAddress());
        holder.name.setText(entity.getName());
        holder.cellphone.setText(entity.getCellphone());
        holder.itemView.setTag(position);
        holder.item_detail_address.setText(entity.getDetail_address());
        if (check.contains(position)) {
            holder.ckdefault.setChecked(true);
        } else {
            holder.ckdefault.setChecked(false);
        }
        holder.ckdefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.contains(position)) {
                    notifyDataSetChanged();
                    holder.ckdefault.setClickable(false);
                } else {
                    notifyDataSetChanged();
                    check.clear();
                    check.add(position);
                    holder.ckdefault.setClickable(true);
                }
                if (holder.ckdefault.isChecked() == true) {
                    //setDefaultAddress(position, holder);
                    //ToastUtil.ShowShort(position+"、"+entity.getId());
                }
            }
        });
        holder.deleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeData(position);
            }
        });
        holder.setdefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultAddress(position, holder);
                holder.isdefault.setText(entity.getIsdefault());
            }
        });
    }

    public void setDefaultAddress(int position, MyHolder holder) {
        OkHttpUtils.post()
                .url(Config.setDefaultAddress)
                .addHeader("cookie", sessionid)
                .addParams("id", address_entity.get(position).getId())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                address_entity.get(position).setIsdefault("已设置");
            }
        });
    }

    @Override
    public int getItemCount() {
        return address_entity.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 删除item
     *
     * @param position：被删除item的position
     */
    public void removeData(int position) {
        address_entity.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView item_detail_address;
        private TextView deleteAddress;
        private CheckBox ckdefault;
        private TextView name;
        private TextView isdefault;
        private TextView setdefault;
        private TextView cellphone;
        private TextView item_address;

        public MyHolder(View itemView) {
            super(itemView);
            ckdefault = itemView.findViewById(R.id.ck_default);
            item_detail_address = itemView.findViewById(R.id.tv_item_detail_address);
            item_address = itemView.findViewById(R.id.tv_address);
            name = itemView.findViewById(R.id.tv_item_reaper_name);
            isdefault = itemView.findViewById(R.id.tv_item_isdefault);
            setdefault = itemView.findViewById(R.id.tv_item_setdefault);
            cellphone = itemView.findViewById(R.id.tv_item_reaper_phone);
            deleteAddress = itemView.findViewById(R.id.tv_delete_address);
        }
    }
}
