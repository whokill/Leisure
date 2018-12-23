package cn.linghouse.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.linghouse.App.ActivityController;
import cn.linghouse.App.Config;
import cn.linghouse.UI.AllOrderActivity;
import cn.linghouse.UI.LoginActivity;
import cn.linghouse.UI.MyBuyActivity;
import cn.linghouse.UI.MyReleaseActivity;
import cn.linghouse.UI.MySellActivity;
import cn.linghouse.UI.ShoppingAddressActivity;
import cn.linghouse.UI.SafeSettingActivity;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;
import okhttp3.Call;

import static android.content.Context.MODE_PRIVATE;

public class PersonalFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_login_name)
    TextView tvLoginName;
    @BindView(R.id.tv_person_message)
    TextView tvPersonMessage;
    @BindView(R.id.tv_person_change_account)
    TextView tvPersonChangeAccount;
    @BindView(R.id.tv_person_shopping_address)
    TextView tvPersonShoppingAddress;
    @BindView(R.id.tv_person_safe_setting)
    TextView tvPersonSafeSetting;
    @BindView(R.id.iv_collection)
    ImageView ivCollection;
    @BindView(R.id.tv_person_exit_app)
    TextView tvPersonExitApp;
    @BindView(R.id.line_login)
    LinearLayout lineLogin;
    @BindView(R.id.tv_click_login)
    TextView tvClickLogin;
    @BindView(R.id.line_not_logged)
    LinearLayout lineNotLogged;
    Unbinder unbinder;
    @BindView(R.id.tv_my_release)
    TextView tvMyRelease;
    @BindView(R.id.tv_my_sold)
    TextView tvMySold;
    @BindView(R.id.tv_my_buy)
    TextView tvMyBuy;
    @BindView(R.id.tv_my_all_order)
    TextView tvMyAllOrder;
    private View view;
    private String name;
    private Dialog feedbackdialog;
    private SharedPreferences sp;
    private EditText feedbackcontent;
    private ImageView ivfeedback,ivfeedbackok;
    private LinearLayout llfeedbacksuccess,llfeedback;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            sp = getContext().getSharedPreferences("data", MODE_PRIVATE);
            name = sp.getString("username", "false");
            if (name.equals("false")) {
                //用户未登录
                lineLogin.setVisibility(View.GONE);
                ivCollection.setVisibility(View.GONE);
                lineNotLogged.setVisibility(View.VISIBLE);
            } else {
                //用户已经登录
                ivCollection.setVisibility(View.VISIBLE);
                lineLogin.setVisibility(View.VISIBLE);
                lineNotLogged.setVisibility(View.GONE);
                tvLoginName.setText(name);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal, container, false);
        unbinder = ButterKnife.bind(this, view);
        ImmersionBar.with(this).init();
        initview();
        return view;
    }

    private void initview() {
        tvMyRelease.setOnClickListener(this);
        tvMyAllOrder.setOnClickListener(this);
        tvMyBuy.setOnClickListener(this);
        tvMySold.setOnClickListener(this);
        tvPersonMessage.setOnClickListener(this);
        tvPersonChangeAccount.setOnClickListener(this);
        tvClickLogin.setOnClickListener(this);
        tvPersonSafeSetting.setOnClickListener(this);
        tvPersonShoppingAddress.setOnClickListener(this);
        tvPersonExitApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击登录跳转到登录界面
            case R.id.tv_click_login:
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                getActivity().finish();
                break;
            //用户反馈
            case R.id.tv_person_message:
                initFeedbackDialog();
                break;
            //我的发布
            case R.id.tv_my_release:
                startActivity(new Intent(getContext(), MyReleaseActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            //我卖出的
            case R.id.tv_my_sold:
                startActivity(new Intent(getContext(), MySellActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            //我买到的
            case R.id.tv_my_buy:
                startActivity(new Intent(getContext(), MyBuyActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            //我的全部交易
            case R.id.tv_my_all_order:
                startActivity(new Intent(getContext(), AllOrderActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            //注销登录
            case R.id.tv_person_change_account:
                //调用切换账号的网络请求
                chagneAccount();
                break;
            //我的收货地址
            case R.id.tv_person_shopping_address:
                startActivity(new Intent(getContext(), ShoppingAddressActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            //安全设置
            case R.id.tv_person_safe_setting:
                startActivity(new Intent(getContext(), SafeSettingActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.tv_person_exit_app:
                ActivityController.finishAll();
                //杀死该app的进程,彻底退出app
                Process.killProcess(Process.myPid());
                break;
            default:
                break;
        }
    }

    //意见反馈Dialog相关属性设置
    private void initFeedbackDialog(){
        if (feedbackdialog==null){
            feedbackdialog = new Dialog(getContext(),R.style.FeedBack_Dialog_Fullscreen);
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.feedback_dialog,null);
        feedbackdialog.setContentView(view);
        getActivity().getWindow().setLayout((ViewGroup.LayoutParams.MATCH_PARENT), ViewGroup.LayoutParams.MATCH_PARENT);
        llfeedback = feedbackdialog.findViewById(R.id.ll_feedback);
        llfeedbacksuccess = feedbackdialog.findViewById(R.id.ll_feedback_success);
        feedbackcontent = feedbackdialog.findViewById(R.id.edt_feedback_content);
        ivfeedback = feedbackdialog.findViewById(R.id.iv_feedback_back);
        ivfeedbackok = feedbackdialog.findViewById(R.id.iv_feedback_ok);
        llfeedbacksuccess.setVisibility(View.GONE);
        ivfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                feedbackdialog.dismiss();
            }
        });
        ivfeedbackok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llfeedback.setVisibility(View.GONE);
                ivfeedbackok.setVisibility(View.GONE);
                llfeedbacksuccess.setVisibility(View.VISIBLE);
            }
        });



        setDialogWindowAttr(feedbackdialog,getContext(),Gravity.BOTTOM);
    }


    public static void setDialogWindowAttr(Dialog dlg, Context ctx, int gravity) {
        Window window = dlg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = gravity;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dlg.getWindow().setAttributes(lp);
        dlg.show();
    }

    //注销当前用户
    private void chagneAccount() {
        OkHttpUtils.get().url(Config.chagneAccountUrl)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        sp = getContext().getSharedPreferences("data", MODE_PRIVATE);
                        sp.edit().putString("username", "false").commit();
                        sp.getString("username", "");
                        ToastUtil.ShowLong(sp.getString("username", "未获取到"));
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getActivity().overridePendingTransition(android.R.anim.fade_in
                                , android.R.anim.fade_out);
                        getActivity().finish();
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.unbinder.unbind();
    }
}
