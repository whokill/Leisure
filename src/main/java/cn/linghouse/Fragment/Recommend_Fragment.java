package cn.linghouse.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.linghouse.UI.LoginActivity;
import cn.linghouse.leisure.R;

public class Recommend_Fragment extends Fragment {
    @BindView(R.id.rl_recommend)
    RecyclerView rlRecommend;
    Unbinder unbinder;
    @BindView(R.id.line_commend_loggin)
    LinearLayout lineCommendLoggin;
    @BindView(R.id.tv_recommend_click_login)
    TextView tvRecommendClickLogin;
    @BindView(R.id.line_commend_not_logged)
    LinearLayout lineCommendNotLogged;
    private View view;
    private String isloggin;
    private SharedPreferences sp;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recommend, container, false);
        unbinder = ButterKnife.bind(this, view);
        ImmersionBar.with(this).init();
        sp = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        isloggin = sp.getString("username", "false");
        if (isloggin.equals("false")) {
            //用户未登录
            lineCommendLoggin.setVisibility(View.GONE);
            lineCommendNotLogged.setVisibility(View.VISIBLE);
        } else {
            //用户已登录
            lineCommendNotLogged.setVisibility(View.GONE);
            lineCommendLoggin.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        ImmersionBar.with(this).destroy();
    }

    @OnClick({R.id.tv_recommend_click_login})
    public void onViewClicked() {
        startActivity(new Intent(getContext(),LoginActivity.class));
        getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
