package cn.linghouse.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.linghouse.leisure.R;

public class Recommend_Fragment extends Fragment {
    @BindView(R.id.rl_recommend)
    RecyclerView rlRecommend;
    Unbinder unbinder;
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recommend, container, false);
        unbinder = ButterKnife.bind(this, view);
        ImmersionBar.with(this).init();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        ImmersionBar.with(this).destroy();
    }
}
