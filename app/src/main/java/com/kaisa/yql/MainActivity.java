package com.kaisa.yql;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int THUMB_WIDTH_SIZE = 300;
    private static final int THUMB_HEIGHT_SIZE = 240;

    private String url = "http://www.cnr.cn/junshi/ztl/leifeng/smlf/201202/W020120226838451234901.jpg";
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_share).setOnClickListener(this);
        img = findViewById(R.id.img);
        loadImage();
    }


    @Override
    public void onClick(View v) {
        share();
    }

    private void share() {
        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
        miniProgramObj.webpageUrl = "http://www.qq.com"; // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = "gh_ed9ea2c45602";     // 小程序原始id
//        miniProgramObj.path = "/pages/media";            //小程序页面路径
        miniProgramObj.path = "";            //小程序页面路径
        WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
        msg.title = "来砍价！一起低价抢购超值商品！"; // 小程序消息title
        //msg.description = "小程序消息Desc";               // 小程序消息desc

        // 设置消息的缩略图
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.sss);
        Log.e("图片", "width : " + bmp.getWidth());
        Log.e("图片", "height : " + bmp.getHeight());
        Log.e("图片", "原图片大小 : " + Bitmap2Bytes(bmp).length / 1024 + " kb");

        //调整图片的比例（微信分享要求图片的比例：5：4）
        Bitmap thumbBmp = drawWXMiniBitmap(bmp, bmp.getHeight() * 5 / 4, bmp.getHeight());
        Log.e("图片", "调整比例图片大小 : " + Bitmap2Bytes(thumbBmp).length / 1024 + " kb");
        bmp.recycle();
        //把图片缩放到目标尺寸
        thumbBmp = Bitmap.createScaledBitmap(thumbBmp, THUMB_WIDTH_SIZE, THUMB_HEIGHT_SIZE, true);
        Log.e("图片", "缩放图片大小 : " + Bitmap2Bytes(thumbBmp).length / 1024 + " kb");
        if (isOverSize(thumbBmp, 128)) {
            Toast.makeText(this, "图片大小超过128k", Toast.LENGTH_SHORT).show();
            //thumbBmp = resizeBitmap(thumbBmp, THUMB_WIDTH_SIZE, THUMB_HEIGHT_SIZE);
            return;
        }
        Log.e("图片", "分享图片大小 : " + Bitmap2Bytes(thumbBmp).length / 1024 + " kb");

        msg.thumbData = bmpToByteArray(thumbBmp, true);// 小程序消息封面图片，小于128k
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("miniProgram");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前只支持会话
        MyApplication.getWeChatApi().sendReq(req);
    }

    private String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static Bitmap drawWXMiniBitmap(Bitmap bitmap, int width, int height) {
        Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 用这个Bitmap生成一个Canvas,然后canvas就会把内容绘制到上面这个bitmap中
        Canvas mCanvas = new Canvas(mBitmap);
        // 绘制画笔
        Paint mPicturePaint = new Paint();
        // 绘制背景图片
        mCanvas.drawBitmap(mBitmap, 0.0f, 0.0f, mPicturePaint);
        // 绘制图片的宽、高
        int width_head = bitmap.getWidth();
        int height_head = bitmap.getHeight();
        // 绘制图片－－保证其在水平方向居中
        mCanvas.drawBitmap(bitmap, (width - width_head) / 2, (height - height_head) / 2, mPicturePaint);
        // 保存绘图为本地图片
        mCanvas.save();
        mCanvas.restore();
        return mBitmap;
    }

    /**
     * 把Bitmap转Byte
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap 是否超出目标大小
     *
     * @param bitmap
     * @param targetSize
     */
    public static boolean isOverSize(Bitmap bitmap, int targetSize) {
        int size = Bitmap2Bytes(bitmap).length / 1024;
        return size > targetSize;
    }


    public static Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    private void loadImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = ImageUtil.getBitmap(url);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
    }
}

