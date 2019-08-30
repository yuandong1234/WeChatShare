package com.kaisa.yql;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.kaisa.yql.utils.BitmapUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;

public class WXShareUtil {
    private final static String TAG = WXShareUtil.class.getSimpleName();
    private final static String MINI_PROGRAM_ID = "gh_ed9ea2c45602";
    private static final int THUMB_WIDTH_SIZE = 300;
    private static final int THUMB_HEIGHT_SIZE = 240;
    private static IWXAPI api;

    private WXShareUtil() {
        api = MyApplication.getWeChatApi();
    }

    private static class Singleton {
        private static final WXShareUtil INSTANCE = new WXShareUtil();
    }

    public boolean isWeChatAppInstalled() {
        return api.isWXAppInstalled();
    }

    public static WXShareUtil getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * 分享到小程序
     *
     * @param title
     * @param path
     * @param bmp
     */
    public void shareMiniProgram(String title, String path, byte[] bmp) {
        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
        miniProgramObj.webpageUrl = "http://www.qq.com"; // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = MINI_PROGRAM_ID;// 小程序id
        miniProgramObj.path = path; //小程序页面路径

        WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
        msg.title = title; // 小程序消息title
        //msg.description = "小程序消息Desc";               // 小程序消息desc
        msg.thumbData = bmp;   // 小程序消息封面图片，小于128k
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("miniProgram");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前只支持会话
        api.sendReq(req);
    }

    private String buildTransaction(String tag) {
        return (tag == null) ? String.valueOf(System.currentTimeMillis()) : tag + System.currentTimeMillis();
    }

    /**
     * 图片 是否超出分享图片限制大小 128
     *
     * @param bitmap
     */
    public boolean isOverWXSize(Bitmap bitmap, Bitmap.CompressFormat format) {
        int size = BitmapUtil.bitmapToByteArray(bitmap, format, false).length / 1024;
        return size > 128;
    }

    /**
     * 获取分享缩略图
     */
    public byte[] getThumbData(byte[] bytes, Bitmap.CompressFormat format) {
        Log.e(TAG, "实际图片大小  : " + bytes.length / 1024 + " kb");
        Bitmap bmp = BitmapUtil.byteArrayToBitmap(bytes);
        Log.e(TAG, "初始图片大小 : " + BitmapUtil.bitmapToByteArray(bmp, format, false).length / 1024 + " kb" + "  width : " + bmp.getWidth() + "  height : " + bmp.getHeight());
        //调整图片的比例（微信分享要求图片的比例：5：4）
        Bitmap thumbBmp = BitmapUtil.drawBitmap(bmp, bmp.getHeight() * 5 / 4, bmp.getHeight());
        Log.e(TAG, "调整比例图片大小 : " + BitmapUtil.bitmapToByteArray(thumbBmp, format, false).length / 1024 + " kb" + "  width : " + thumbBmp.getWidth() + "  height : " + thumbBmp.getHeight());
        bmp.recycle();
        //把图片缩放到目标尺寸
        thumbBmp = Bitmap.createScaledBitmap(thumbBmp, THUMB_WIDTH_SIZE, THUMB_HEIGHT_SIZE, true);
        byte[] newBytes = BitmapUtil.bitmapToByteArray(thumbBmp, format, false);
        Log.e(TAG, "缩放图片大小 : " + newBytes.length / 1024 + " kb" + "  width : " + thumbBmp.getWidth() + "  height : " + thumbBmp.getHeight());
        //压缩图片
        thumbBmp = compressBitmap(thumbBmp, format);
        byte[] thumbData = BitmapUtil.bitmapToByteArray(thumbBmp, format, false);
        Log.e(TAG, "分享图片大小 : " + thumbData.length / 1024 + " kb" + "  width : " + thumbBmp.getWidth() + "  height : " + thumbBmp.getHeight());
        return thumbData;
    }


    private Bitmap compressBitmap(Bitmap bitmap, Bitmap.CompressFormat format) {
        Bitmap thumbBmp = bitmap;
        while (isOverWXSize(thumbBmp, format)) {
            Log.e(TAG, "图片大小超过128k");
            int width = thumbBmp.getWidth();
            int newWidth = width - 5;
            int newHeight = newWidth * 4 / 5;
            thumbBmp = resizeBitmap(thumbBmp, newWidth, newHeight);
        }
        return thumbBmp;
    }

    private static Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

}
