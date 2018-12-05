package cn.linghouse.leisure.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import cn.linghouse.leisure.R;
import cn.linghouse.leisure.UI.LoginActivity;

public class MyCollectionFragment extends Fragment implements View.OnClickListener{
    private View view;
    private TextView jumplogin;
    private LinearLayout shoppingempty,linenotlogged;//购物车为空显示的视图
    private ListView lvshoppingcar;//购物车非空显示的视图
    private SharedPreferences sp;

    /**
     * 通过这个方法来判断当前是哪个fragment，
     * 如果是当前这个fragment，就获取一次sp里面的值
     * @param isVisibleToUser：是否是当前页面
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            sp = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
            String name = sp.getString("username", "false");
            if (name.equals("false")) {
                lvshoppingcar.setVisibility(View.GONE);
                shoppingempty.setVisibility(View.GONE);
                linenotlogged.setVisibility(View.VISIBLE);
            }else {
                //这里根据购物车是否为空进行对应视图的隐藏与显示,这里先将listview隐藏
                lvshoppingcar.setVisibility(View.GONE);
                linenotlogged.setVisibility(View.GONE);
                shoppingempty.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_collection_fragment, container, false);
        /**
         * 视图显示问题：
         * 1、当用户未登录时，所展示的界面应该是提示用户登录
         * 2、当用户登录后，向后端发送网络请求获取该用户名下对应的购物车内的物品,展示lvshoppingcar视图
         * 3、如果后后端返回该用户名下的购物车为空，再展示shoppingempty该视图
         */
        shoppingempty = view.findViewById(R.id.ll_shopping_empty);
        lvshoppingcar = view.findViewById(R.id.lv_shopping_car);
        linenotlogged = view.findViewById(R.id.line_not_logged_shopping_car);
        jumplogin = view.findViewById(R.id.tv_click_jump_login);
        jumplogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_click_jump_login:
                startActivity(new Intent(getContext(),LoginActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    /**
     * 获取登录用户名下面所对应的购物车里面的内容
     */
    private void GetCar(){
        //网络请求

    }
}
