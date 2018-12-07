package cn.linghouse.UI;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;

import cn.linghouse.leisure.Adapter.Fragment_Adapter;
import cn.linghouse.leisure.App.ActivityController;
import cn.linghouse.leisure.Fragment.PersonalFragment;
import cn.linghouse.leisure.Fragment.IndexFragment;
import cn.linghouse.leisure.Fragment.Recommend_Fragment;
import cn.linghouse.leisure.Fragment.MyCollectionFragment;
import cn.linghouse.leisure.R;
import cn.linghouse.leisure.Util.NoScrollViewPager;

public class IndexActivity extends AppCompatActivity implements View.OnClickListener{
    private NoScrollViewPager vpPager;
    private ImageView ivadd;
    private RadioButton rbIndex;
    private RadioButton rbSecondPage;
    private RadioButton rbThirdPage;
    private RadioButton rbFourthPage;
    private RadioGroup rgBottomAll;
    private Fragment_Adapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityController.addActivity(this);
        //沉浸式状态栏
        ImmersionBar.with(IndexActivity.this).init();
        initView();
        vpPager.setCurrentItem(0);
    }

    private void initView() {
        vpPager = findViewById(R.id.vp_pager);
        ivadd = findViewById(R.id.iv_add);
        rbIndex = findViewById(R.id.rb_index);
        rbSecondPage = findViewById(R.id.rb_second_page);
        rbThirdPage = findViewById(R.id.rb_third_page);
        rbFourthPage = findViewById(R.id.rb_fourth_page);
        rgBottomAll = findViewById(R.id.rg_bottom_all);
        ivadd.setOnClickListener(this);
        vpPager.setNoScroll(true);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new IndexFragment());
        fragments.add(new Recommend_Fragment());
        fragments.add(new MyCollectionFragment());
        fragments.add(new PersonalFragment());
        adapter = new Fragment_Adapter(getSupportFragmentManager());
        adapter.setList(fragments);
        vpPager.setAdapter(adapter);
        vpPager.setOffscreenPageLimit(4);

        rgBottomAll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_index:
                        vpPager.setCurrentItem(0);
                        rbIndex.setChecked(true);
                        break;
                    case R.id.rb_second_page:
                        vpPager.setCurrentItem(1);
                        rbSecondPage.setChecked(true);
                        break;
                    case R.id.rb_third_page:
                        vpPager.setCurrentItem(2);
                        rbThirdPage.setChecked(true);
                        break;
                    case R.id.rb_fourth_page:
                        vpPager.setCurrentItem(3);
                        rbFourthPage.setChecked(true);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add:
                startActivity(new Intent(IndexActivity.this,ReleaseGoodsActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}
