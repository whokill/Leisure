package cn.linghouse.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.linghouse.App.ActivityController;
import cn.linghouse.App.Config;
import cn.linghouse.Entity.MessageEvent;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;
import okhttp3.Call;

public class GoodDetailsActivity extends AppCompatActivity {
    //商品价格
    @BindView(R.id.tv_goods_details_price)
    TextView tvGoodsDetailsPrice;
    //商品标题
    @BindView(R.id.tv_goods_details_title)
    TextView tvGoodsDetailsTitle;
    //商品具体描述
    @BindView(R.id.tv_goods_details)
    TextView tvGoodsDetails;
    //标题栏的返回图标
    @BindView(R.id.iv_goods_details_back)
    ImageView ivGoodsDetailsBack;
    @BindView(R.id.iv_details_collection)
    ImageView ivDetailsCollection;
    @BindView(R.id.iv_details_chat)
    ImageView ivDetailsChat;
    @BindView(R.id.tv_goods_details_sortname)
    TextView tvGoodsDetailsSortname;
    //商品轮播图
    @BindView(R.id.con_goods_banner)
    ConvenientBanner conGoodsBanner;
    @BindView(R.id.btn_shopping)
    Button btnShopping;
    //接收的images数组，作为轮播图的数据源
    private String[] image;
    //接收到的分类
    private String sortname;
    //接收到的详细描述
    private String detail;
    //接收到的商品价格
    private String price;
    //接收到的商品名称
    private String title;
    //接收到的商品编号
    private String cnumber;
    private boolean iscoll;
    private List<String> list;
    private String sessionid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ActivityController.addActivity(this);
        ImmersionBar.with(GoodDetailsActivity.this).init();
        ButterKnife.bind(this);
        title = getIntent().getStringExtra("title");
        price = getIntent().getStringExtra("price");
        detail = getIntent().getStringExtra("details");
        sortname = getIntent().getStringExtra("sortname");
        cnumber = getIntent().getStringExtra("cnumber");
        image = getIntent().getStringArrayExtra("imagelist");
        tvGoodsDetailsTitle.setText(title);
        tvGoodsDetailsPrice.setText(price);
        tvGoodsDetails.setText(detail);
        tvGoodsDetailsSortname.setText(sortname);
        iscollection();
        list = Arrays.asList(image);
        conGoodsBanner.setPointViewVisible(true);
        conGoodsBanner.setPageIndicator(new int[]{R.mipmap.unselect, R.mipmap.select});
        conGoodsBanner.setMotionEventSplittingEnabled(true);
        conGoodsBanner.startTurning(1500);
        conGoodsBanner.setPages(new CBViewHolderCreator<NetworkImageLoader>() {
            @Override
            public NetworkImageLoader createHolder() {
                return new NetworkImageLoader();
            }
        }, list);
    }

    private void iscollection() {
        SharedPreferences share = getSharedPreferences("Session", MODE_PRIVATE);
        sessionid = share.getString("sessionid", "null");
        OkHttpUtils.post()
                .url(Config.isCollection)
                .addHeader("cookie", sessionid)
                .addParams("CN", cnumber)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    iscoll = jsonObject.getBoolean("data");
                    if (iscoll == true) {
                        ivDetailsCollection.setImageResource(R.mipmap.keep_checked);
                    } else if (iscoll == false) {
                        ivDetailsCollection.setImageResource(R.mipmap.keep);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.iv_details_collection, R.id.iv_details_chat, R.id.iv_goods_details_back, R.id.btn_shopping})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //收藏图标
            case R.id.iv_details_collection:
                if (iscoll == true) {
                    ivDetailsCollection.setImageResource(R.mipmap.keep);
                    joinCollection();
                } else if (iscoll == false) {
                    ivDetailsCollection.setImageResource(R.mipmap.keep_checked);
                    joinCollection();
                }
                break;
            //咨询图标
            case R.id.iv_details_chat:
                ToastUtil.ShowLong("咨询");
                break;
            //返回图标
            case R.id.iv_goods_details_back:
                GoodDetailsActivity.this.finish();
                break;
            //立即购买
            case R.id.btn_shopping:
                String picurl = list.get(0);
                Intent intent = new Intent();
                intent.putExtra("picurl",picurl);
                intent.putExtra("title",title);
                intent.putExtra("price",price);
                intent.putExtra("cnumber",cnumber);
                intent.setClass(GoodDetailsActivity.this,BuyNowActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            default:
                break;
        }
    }

    private void joinCollection() {
        OkHttpUtils.post()
                .url(Config.joinCollection)
                .addHeader("cookie", sessionid)
                .addParams("CN", cnumber)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    EventBus.getDefault().post(new MessageEvent("开始更新"));
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("取消收藏")) {
                        ivDetailsCollection.setImageResource(R.mipmap.keep);
                        ToastUtil.ShowLong(message);
                    } else if (message.equals("加入收藏")) {
                        ivDetailsCollection.setImageResource(R.mipmap.keep_checked);
                        ToastUtil.ShowLong(message);
                    } else if (message.equals("商品都是你的还收藏什么？")) {
                        ivDetailsCollection.setImageResource(R.mipmap.keep);
                        ToastUtil.ShowLong(message);
                    }else{
                        ivDetailsCollection.setImageResource(R.mipmap.keep);
                        ToastUtil.ShowShort(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class NetworkImageLoader implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            Glide.with(context).load(data).into(imageView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(GoodDetailsActivity.this).destroy();
        ActivityController.removeActivity(this);
    }
}
