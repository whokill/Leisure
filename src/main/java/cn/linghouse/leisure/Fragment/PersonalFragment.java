package cn.linghouse.leisure.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.xuexiang.xqrcode.XQRCode;

import cn.linghouse.leisure.R;
import cn.linghouse.leisure.UI.LoginActivity;
import cn.linghouse.leisure.UI.ShoppingAddressActivity;
import cn.linghouse.leisure.Util.ToastUtil;

public class PersonalFragment extends Fragment implements View.OnClickListener{
    private View view;
    private String name,collectionurl;
    private static final int CHOOSE_PHOTO = 1;
    private TextView uploadcollection;
    private Dialog bigdialog;
    private SharedPreferences sp;
    private ImageView ivcollection,ivbigimage;
    private TextView click_login,loginname,loginout,shoppingaddress;
    private LinearLayout linelogin,linenotlogged,lltitle;
    //ALIPAY_SHOP：是根据用户上传的支付宝收款二维码通过XQRCode解析出来的一串地址
    public String ALIPAY_SHOP;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal,container,false);
        ImmersionBar.with(this).init();
        lltitle = view.findViewById(R.id.ll_title);
        linelogin = view.findViewById(R.id.line_login);
        linenotlogged = view.findViewById(R.id.line_not_logged);
        uploadcollection = view.findViewById(R.id.tv_upload_collection);
        ivcollection = view.findViewById(R.id.iv_collection);
        click_login = view.findViewById(R.id.tv_click_login);
        loginname = view.findViewById(R.id.tv_login_name);
        loginout = view.findViewById(R.id.tv_person_logout);
        shoppingaddress = view.findViewById(R.id.tv_shopping_address);

        loginout.setOnClickListener(this);
        click_login.setOnClickListener(this);
        uploadcollection.setOnClickListener(this);
        shoppingaddress.setOnClickListener(this);

        sp = getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        name = sp.getString("username","false");
        collectionurl = sp.getString("imageurl","false");
        if (name.equals("false")){
            //用户未登录
            lltitle.setVisibility(View.VISIBLE);
            linelogin.setVisibility(View.GONE);
            ivcollection.setVisibility(View.GONE);
            linenotlogged.setVisibility(View.VISIBLE);
        }else{
            //用户已经登录
            lltitle.setVisibility(View.GONE);
            ivcollection.setVisibility(View.VISIBLE);
            linelogin.setVisibility(View.VISIBLE);
            linenotlogged.setVisibility(View.GONE);
            loginname.setText(name);
        }
        if (collectionurl.equals("false")){
        }else{
            ivcollection.setImageURI(Uri.parse(collectionurl));
            ivcollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bigdialog = new Dialog(getContext());
                    bigdialog.setContentView(R.layout.dialog_big_image);
                    ivbigimage = bigdialog.findViewById(R.id.iv_big_image);
                    ivbigimage.setMinimumHeight(ivcollection.getMaxHeight());
                    ivbigimage.setMinimumWidth(ivcollection.getMaxWidth());
                    ivbigimage.setImageURI(Uri.parse(collectionurl));
                    bigdialog.setCancelable(true);
                    bigdialog.show();
                    ivbigimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bigdialog.dismiss();
                        }
                    });
                }
            });
        }
        return view;
    }

    //打开相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    ToastUtil.ShowShort("缺少必要权限");
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CHOOSE_PHOTO:
                handleImageOnKitKat(data);
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        if (data==null) {
            return;
        }
        String imagepath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getContext(),uri)){
            //如果是document类型的Uri,则通过document id处理
            String docid = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docid.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagepath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contenturi = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docid));
                imagepath = getImagePath(contenturi,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            imagepath = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagepath = uri.getPath();
        }
        diaplayImage(imagepath);
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContext().getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void diaplayImage(String imagePath){
        if (imagePath != null){
            final Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            //保存解析后的二维码内容
            String parseafter = XQRCode.analyzeQRCode(imagePath);
            //判断是否是支付宝收款二维码,用布尔类型来保存结果
            boolean iscode = parseafter.startsWith("HTTPS://QR.ALIPAY.COM");
            if (!iscode){
                ToastUtil.ShowShort("您选取的不是支付宝的收款二维码,请重新选择");
            }else{
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("imageurl",imagePath);
                editor.commit();
                ToastUtil.ShowLong(sp.getString("imageurl","查询失败"));
                ivcollection.setImageBitmap(bitmap);
                //点击查看大图
                ivcollection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bigdialog = new Dialog(getContext());
                        bigdialog.setContentView(R.layout.dialog_big_image);
                        ivbigimage = bigdialog.findViewById(R.id.iv_big_image);
                        ivbigimage.setMinimumHeight(ivcollection.getMaxHeight());
                        ivbigimage.setMinimumWidth(ivcollection.getMaxWidth());
                        ivbigimage.setImageBitmap(bitmap);
                        bigdialog.setCancelable(true);
                        bigdialog.show();
                        ivbigimage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bigdialog.dismiss();
                            }
                        });
                    }
                });
            }
        }else {
            Toast.makeText(getContext(), "选取图片错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击登录
            case R.id.tv_click_login:
                startActivity(new Intent(getContext(),LoginActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                getActivity().finish();
                onDestroyView();
                break;

                //注销登录
            case R.id.tv_person_logout:
                sp = getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
                sp.edit().putString("username","false").commit();
                sp.getString("username","");
                ToastUtil.ShowShort("注销成功");
                lltitle.setVisibility(View.VISIBLE);
                linelogin.setVisibility(View.GONE);
                linenotlogged.setVisibility(View.VISIBLE);
                break;

                //我的收货地址
            case R.id.tv_shopping_address:
                startActivity(new Intent(getContext(),ShoppingAddressActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;

                //我的收款二维码
            case R.id.tv_upload_collection:
                //打开相册，选取支付宝收款二维码并获取图片路径
                if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
                break;
                default:
                    break;
        }
    }
}
