package cn.linghouse.leisure.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;

import java.util.ArrayList;
import java.util.List;
import cn.linghouse.leisure.Adapter.ImagePickerAdapter;
import cn.linghouse.leisure.R;
import cn.linghouse.leisure.Util.KeyboardUtil;
import cn.linghouse.leisure.Util.RadioGroupUtils;
import cn.linghouse.leisure.Util.ToastUtil;
import cn.qqtheme.framework.picker.OptionPicker;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class ReleaseGoodsActivity extends AppCompatActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener, View.OnClickListener {
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 4;//允许选择图片最大数
    private Dialog dialog, classifydialog, pricedialog,labeldialog;
    private Button btnrelease,labeladd;
    private TextView tvcancel, tvensure, tvuserclassify,
            userprice, userway,tvuserlabel,tvlabelconfirm,tvlabelcancel;
    private ImageView ivback;
    private CheckBox pinkage;
    private RadioGroupUtils rghead, rgone, rgwo, rgthree;
    private EditText etbabytitle, etbabydescribe, etseprice,etlabel;
    private boolean pinkagechecked;//价格弹窗中用户是否选中包邮
    private SharedPreferences sp;
    private TagContainerLayout taglayout;
    private List<String> list;
    private View labelview = null;
    private View priceview = null;
    private OptionPicker picker;
    private KeyboardUtil util = null;
    private String finalclassify;
    private String finalprice;//价格弹窗中用户输入的价格
    private String finalway;
    private LinearLayout babyclassify, babypice, choiceway,choicelabel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_goods);
        //沉浸式状态栏
        ImmersionBar.with(this).init();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        initView();
        sp = getSharedPreferences("data", MODE_PRIVATE);
    }

    private void initView() {
        btnrelease = findViewById(R.id.btn_release);
        btnrelease.bringToFront();
        RecyclerView recyclerView = findViewById(R.id.recyclerView1);
        ivback = findViewById(R.id.iv_back);
        etbabytitle = findViewById(R.id.et_baby_title);
        etbabydescribe = findViewById(R.id.et_baby_describe);
        babyclassify = findViewById(R.id.ll_baby_classify);
        babypice = findViewById(R.id.ll_baby_pice);
        choiceway = findViewById(R.id.ll_choice_way);
        choicelabel = findViewById(R.id.ll_label);
        tvuserclassify = findViewById(R.id.tv_user_classify);
        userprice = findViewById(R.id.tv_user_classify_price);
        userway = findViewById(R.id.tv_user_way);
        tvuserlabel = findViewById(R.id.tv_user_label);

        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ivback.setOnClickListener(this);
        babyclassify.setOnClickListener(this);
        babypice.setOnClickListener(this);
        choiceway.setOnClickListener(this);
        choicelabel.setOnClickListener(this);
        btnrelease.setOnClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                //添加图片
                showAddPicDialog();
                break;
            default:
                //预览图片
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    private void showAddPicDialog() {
        dialog = new Dialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.bottom_dialog, null);
        dialog.setContentView(v);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogAnimation);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        wl.width = getResources().getDisplayMetrics().widthPixels;
        v.measure(0, 0);
        wl.height = v.getMeasuredHeight();
        wl.alpha = 9f;
        window.setAttributes(wl);
        dialog.show();

        //打开相机拍照
        v.findViewById(R.id.tv_open_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent = new Intent(ReleaseGoodsActivity.this, ImageGridActivity.class);
                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true);
                startActivityForResult(intent, REQUEST_CODE_SELECT);
            }
        });

        //打开相册选择图片
        v.findViewById(R.id.tv_choice_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent1 = new Intent(ReleaseGoodsActivity.this, ImageGridActivity.class);
                startActivityForResult(intent1, REQUEST_CODE_SELECT);
            }
        });
        //取消
        v.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    ArrayList<ImageItem> images = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                //1、不压缩直接显示
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
               /* //2、开启子线程压缩后再显示
                  new Thread(new Runnable() {
                      @Override
                      public void run() {
                          for (ImageItem item : images) {
                              String sourceImagePath = item.path;
                              File file = new File(item.path);
                              String targetImagePath = getExternalFilesDir(null).getAbsolutePath() + "/Pic/" + file.getName();
                              boolean save = PicCompressUtil.imageCompress(sourceImagePath, targetImagePath, 200);//压缩程度
                              if (save) {
                                  Message message = Message.obtain();
                                  message.obj = targetImagePath;
                                  message.what = 1;
                                  handler.sendMessage(message);
                              }
                          }
                      }
                  }).start();*/
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                ReleaseGoodsActivity.this.finish();
                break;

            //物品分类
            case R.id.ll_baby_classify:
                classifydialog = new Dialog(this);
                View view = LayoutInflater.from(this).inflate(R.layout.releasegoods_classify_dialog, null);
                classifydialog.setContentView(view);
                tvcancel = classifydialog.findViewById(R.id.tv_classify_dialog_cancel);
                tvensure = classifydialog.findViewById(R.id.tv_classify_dialog_ensure);
                rghead = classifydialog.findViewById(R.id.rg_head);
                rgone = classifydialog.findViewById(R.id.rg_one);
                rgwo = classifydialog.findViewById(R.id.rg_two);
                rgthree = classifydialog.findViewById(R.id.rg_three);
                tvcancel.setOnClickListener(this);
                tvensure.setOnClickListener(this);
                setDialogWindowAttr(classifydialog, this, Gravity.CENTER);
                classifydialog.show();
                break;

            //物品价格
            case R.id.ll_baby_pice:
                if (pricedialog == null) {
                    pricedialog = new Dialog(ReleaseGoodsActivity.this, R.style.Dialog_Fullscreen);
                }
                if (priceview == null){
                    priceview = LayoutInflater.from(this).inflate(R.layout.releasegoods_price_dialog, null);
                    pricedialog.setContentView(priceview);
                    etseprice = pricedialog.findViewById(R.id.se_et_price);
                    pinkage = pricedialog.findViewById(R.id.cb_pinkage);
                    setDialogWindowAttr(pricedialog, this, Gravity.BOTTOM);
                }if (util==null){
                util = new KeyboardUtil(getApplicationContext(), pricedialog);
                util.attachTo(etseprice);
                }
                util.showSoftKeyboard();
                pricedialog.show();
                //软键盘里面确定按钮的点击事件
                util.setOnOkClick(new KeyboardUtil.OnOkClick() {
                    @Override
                    public void onOkClick() {
                        pinkagechecked = pinkage.isChecked();//是否选中包邮
                        if (TextUtils.isEmpty(etseprice.getText().toString())) {
                            userprice.setText("开个价吧");
                            etseprice.setText("");
                            pinkage.setChecked(pinkagechecked);
                            pricedialog.dismiss();
                        } else {
                            finalprice = etseprice.getText().toString();//商品价格
                            pinkage.setChecked(pinkagechecked);
                            userprice.setText(finalprice + "元");
                            etseprice.setText(finalprice);
                            pricedialog.dismiss();
                        }
                    }
                });

                //设置点击键盘上的隐藏按钮的点击事件
                util.setOnCancelClick(new KeyboardUtil.onCancelClick() {
                    @Override
                    public void onCancellClick() {
                        pricedialog.dismiss();
                    }
                });

                //价格输入框的点击事件，弹出自定义的键盘
                etseprice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        util.showSoftKeyboard();
                    }
                });

                if (TextUtils.isEmpty(etseprice.getText().toString())) {
                } else {
                    etseprice.setSelection(etseprice.length());
                    etseprice.requestFocus();
                }
                break;

            /**
             * 交易方式有三种：
             * 邮寄、同城自提、见面交易
             * 当用户选择了包邮后，交易方式自动选择为邮寄
             * 当然了，用户也可以自己再选择交易方式
             */
            case R.id.ll_choice_way:
                if (picker==null) {
                    picker = new OptionPicker(this, new String[]{
                            "邮寄", "同城自提", "见面交易"
                    });
                }
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        for (int i =0;i<=index;i++){
                            finalway = item;
                            userway.setText(finalway);
                        }
                    }
                });
                picker.show();
                break;
            /**
             * 用户选择了大概分类后，再次进行具体的
             * 商品标签选择，每个商品最多有5个标签
             */
            case R.id.ll_label:
                if (list == null)
                    list = new ArrayList<>(5);
                if (labeldialog == null)
                    labeldialog = new Dialog(ReleaseGoodsActivity.this);
                if (labelview == null) {
                    labelview = LayoutInflater.from(ReleaseGoodsActivity.this).inflate(R.layout.releasegoods_lable_dialog, null);
                    labeldialog.setContentView(labelview);
                    taglayout = labeldialog.findViewById(R.id.taglayout);
                    labeladd = labeldialog.findViewById(R.id.btn_add_label);
                    etlabel = labeldialog.findViewById(R.id.et_label);
                    tvlabelconfirm = labeldialog.findViewById(R.id.tv_label_confirm);
                    tvlabelcancel = labeldialog.findViewById(R.id.tv_label_cancel);
                }

                setDialogWindowAttr(labeldialog,ReleaseGoodsActivity.this,Gravity.CENTER);
                labeladd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(etlabel.getText().toString())&&taglayout.getChildCount()<5){
                            ToastUtil.ShowShort("标签名不能为空哦");
                        }else if (taglayout.getChildCount()<5){
                            list.add(etlabel.getText().toString());
                            etlabel.setText("");
                            taglayout.setTags(list);
                        }else{
                            ToastUtil.ShowShort("最多只能添加5个标签哦");
                        }
                    }
                });

                taglayout.setOnTagClickListener(new TagView.OnTagClickListener() {
                    @Override
                    public void onTagClick(int position, String text) {

                    }

                    @Override
                    public void onTagLongClick(final int position, String text) {
                        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(ReleaseGoodsActivity.this)
                                .setTitle("删除标签")
                                .setMessage("确定要删除  "+text+"  这个标签吗")
                                .setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        taglayout.removeTag(position);
                                        list.remove(position);
                                    }
                                }).setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                    }

                    @Override
                    public void onTagCrossClick(int position) {

                    }
                });
                tvlabelconfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.size()>0){
                            tvuserlabel.setText("标签详情");
                        }else{
                            tvuserlabel.setText("添加标签");
                        }
                        labeldialog.dismiss();
                    }
                });
                tvlabelcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvuserlabel.getText().toString().equals("标签详情")){
                            labeldialog.dismiss();
                        }else if (tvuserlabel.getText().toString().equals("添加标签")){
                            for (int i =0;i<list.size();i++){
                                list.clear();
                                taglayout.removeAllTags();
                            }
                            labeldialog.dismiss();
                        }
                    }
                });
                labeldialog.show();
                break;

            //分类弹窗中的确定按钮
            case R.id.tv_classify_dialog_ensure:
                //取值radiobutton，然后将值设置给tvuserclassify
                for (int i = 0; i < rghead.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) rgone.getChildAt(i);
                    RadioButton rb1 = (RadioButton) rgwo.getChildAt(i);
                    RadioButton rb2 = (RadioButton) rgthree.getChildAt(i);
                    if (rb.isChecked()) {
                        tvuserclassify.setText(rb.getText().toString());
                        finalclassify = rb.getText().toString();
                    } else if (rb1.isChecked()) {
                        tvuserclassify.setText(rb1.getText().toString());
                        finalclassify = rb1.getText().toString();
                    } else if (rb2.isChecked()) {
                        tvuserclassify.setText(rb2.getText().toString());
                        finalclassify = rb2.getText().toString();
                    }
                    classifydialog.dismiss();
                }
                break;

            //分类弹窗中的取消按钮
            case R.id.tv_classify_dialog_cancel:
                classifydialog.dismiss();
                break;

            /**
             * 发布,这里需要判断sp里面的username是否为false
             * 如果为false的话，则表示用户未登录，这时候点击发布按钮
             * 应该提示用户先登录，当用户登录后,sp里面的值应该为当前登录
             * 用户的用户名，这个时候才能调用网络请求将商品发布到后端
             */
            case R.id.btn_release:
                //判断商品的各个参数是否为空
                if (TextUtils.isEmpty(etbabytitle.getText().toString())){
                    etbabytitle.setError("商品标题不能为空");
                    ToastUtil.ShowLong("商品标题不能为空");
                }else if (TextUtils.isEmpty(etbabydescribe.getText().toString())){
                    etbabydescribe.setError("商品描述不能为空");
                    ToastUtil.ShowLong("商品描述不能为空");
                }else if (TextUtils.isEmpty(finalclassify)){
                    //没有选择分类
                    ToastUtil.ShowLong("分类为空");
                }else if (TextUtils.isEmpty(finalprice)){
                    //没有输入价格
                    ToastUtil.ShowLong("价格为空");
                }else if (TextUtils.isEmpty(finalway)){
                    //没有选择交易方式
                    ToastUtil.ShowLong("交易方式为空");
                }else if (list==null || list.size()==0){
                    //list为空，也就是没有添加任何标签
                    ToastUtil.ShowLong("没有标签");
                }else{
                    //判断商品信息后，调用网络请求，将物品信息发布到后端
                    uploadImage(images,finalclassify,finalprice,finalway,list);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置Dialog的基本样式以及对齐方式
     *
     * @param dlg：Dialog
     * @param ctx:上下文环境
     * @param gravity：对齐方式
     */
    public static void setDialogWindowAttr(Dialog dlg, Context ctx, int gravity) {
        Window window = dlg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = gravity;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dlg.getWindow().setAttributes(lp);
    }

    /**
     * 上传商品相关数据到后台
     * @param classify：商品分类
     * @param price：商品价格
     * @param way：商品交易方式
     * @param label：商品标签
     */
    private void uploadImage(ArrayList<ImageItem> ImageList,String classify,String price,String way,List<String> label) {
        ToastUtil.ShowLong(classify+"、"+price+"、"+way+"、"+label.size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
