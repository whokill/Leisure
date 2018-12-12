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

import java.util.ArrayList;
import java.util.List;

import cn.linghouse.Entity.Shopping_Address_Entity;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;

public class Shopping_Address_Adapter extends RecyclerView.Adapter<Shopping_Address_Adapter.MyHolder> {
    private Context context;
    ArrayList<Integer> check = new ArrayList<>();
    private List<Shopping_Address_Entity> address_entity;

    public Shopping_Address_Adapter(Context context, List<Shopping_Address_Entity> entities) {
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
        holder.item_province.setText(entity.getProvince());
        holder.item_city.setText(entity.getCity());
        holder.item_area.setText(entity.getArea());
        holder.itemView.setTag(position);
        holder.item_detail_address.setText(entity.getDetail_address());
        if (check.contains(position)){
            holder.ckdefault.setChecked(true);
        }else{
            holder.ckdefault.setChecked(false);
        }
        holder.ckdefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.contains(position)){
                    notifyDataSetChanged();
                    holder.ckdefault.setClickable(false);
                }else{
                    notifyDataSetChanged();
                    check.clear();
                    check.add(position);
                    holder.ckdefault.setClickable(true);
                }
                if(holder.ckdefault.isChecked()==true){
                    ToastUtil.ShowLong(""+position);
                }
            }
        });
        holder.deleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeData(position);
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
        private LinearLayout ll_line;
        private TextView deleteAddress;
        private CheckBox ckdefault;
        private TextView item_province, item_city, item_area;

        public MyHolder(View itemView) {
            super(itemView);
            ll_line = itemView.findViewById(R.id.ll_line);
            ckdefault = itemView.findViewById(R.id.ck_default);
            item_detail_address = itemView.findViewById(R.id.tv_item_detail_address);
            item_province = itemView.findViewById(R.id.tv_address_province);
            item_city = itemView.findViewById(R.id.tv_address_city);
            item_area = itemView.findViewById(R.id.tv_address_area);
            deleteAddress = itemView.findViewById(R.id.tv_delete_address);
        }
    }
}
