package cn.linghouse.leisure.Util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;



public class CodeUtils{
    private static CodeUtils utils;
    private int mpaddingleft,mpaddingtop;
    private StringBuilder mBuilder = new StringBuilder();
    private Random mRandom = new Random();
    private static final int DEFAULT_CODE_LENGTH = 6;//验证码的长度  这里是6位
    private static final int DEFAULT_FONT_SIZE = 70;//字体大小
    private static final int DEFAULT_LINE_NUMBER = 5;//多少条干扰线
    private static final int BASE_PADDING_LEFT = 20; //左边距
    private static final int RANGE_PADDING_LEFT = 30;//左边距范围值
    private static final int BASE_PADDING_TOP = 70;//上边距
    private static final int RANGE_PADDING_TOP = 15;//上边距范围值
    private static final int DEFAULT_WIDTH = 300;//默认宽度.图片的总宽
    private static final int DEFAULT_HEIGHT = 100;//默认高度.图片的总高
    private static final int DEFAULT_COLOR = 0xDF;//默认背景颜色值
    public static  String code;

    public static CodeUtils getInstance(){
        if (utils==null){
            utils = new CodeUtils();
        }
        return utils;
    }

    public Bitmap createBitmap(String msg){
        mpaddingleft = 7;
        mpaddingtop = 3;
        Bitmap bitmap = Bitmap.createBitmap(DEFAULT_WIDTH,DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        code = createcode(msg);
        canvas.drawColor(Color.rgb(DEFAULT_COLOR,DEFAULT_COLOR,DEFAULT_COLOR));
        Paint paint = new Paint();
        randomTextStyle(paint);
        randomPadding();
        paint.setTextSize(DEFAULT_FONT_SIZE);
        paint.setStrokeWidth(2);
        canvas.drawText(msg,mpaddingleft,mpaddingtop,paint);
        for (int i=0;i<DEFAULT_LINE_NUMBER;i++){
            drawLine(canvas,paint);
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bitmap;
    }
    public String createcode(String msg){
        mBuilder.append(msg);
        return mBuilder.toString();
    }
    //生成干扰线
    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = mRandom.nextInt(DEFAULT_WIDTH);
        int startY = mRandom.nextInt(DEFAULT_HEIGHT);
        int stopX = mRandom.nextInt(DEFAULT_WIDTH);
        int stopY = mRandom.nextInt(DEFAULT_HEIGHT);
        paint.setStrokeWidth(3);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }
    //随机颜色
    private int randomColor() {
        mBuilder.delete(0, mBuilder.length()); //使用之前首先清空内容

        String haxString;
        for (int i = 0; i < 3; i++) {
            haxString = Integer.toHexString(mRandom.nextInt(0xFF));
            if (haxString.length() == 1) {
                haxString = "0" + haxString;
            }
            mBuilder.append(haxString);
        }

        return Color.parseColor("#" + mBuilder.toString());
    }
    //随机文本样式
    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(mRandom.nextBoolean());  //true为粗体，false为非粗体
        float skewX = mRandom.nextInt(7)/3;
        skewX = mRandom.nextBoolean() ? skewX : -skewX;
        paint.setTextSkewX(skewX); //float类型参数，负数表示右斜，整数左斜
        paint.setUnderlineText(true); //true为下划线，false为非下划线
        paint.setStrikeThruText(true); //true为删除线，false为非删除线
    }
    //随机间距
    private void randomPadding() {
        mpaddingleft += BASE_PADDING_LEFT + mRandom.nextInt(RANGE_PADDING_LEFT);
        mpaddingtop = BASE_PADDING_TOP + mRandom.nextInt(RANGE_PADDING_TOP);
    }
}