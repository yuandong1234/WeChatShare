package com.kaisa.yql;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kaisa.yql.utils.BitmapUtil;
import com.kaisa.yql.utils.FileUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes = FileUtil.getBytes(url);
                if (bytes == null) return;
                final byte[] thumbData = WXShareUtil.getInstance().getThumbData(bytes,Bitmap.CompressFormat.PNG);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WXShareUtil.getInstance().shareMiniProgram("分享小程序测试", "", thumbData);
                    }
                });
            }
        }).start();
    }

    private void loadImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = BitmapUtil.getBitmap(url);

                byte[] bytes = FileUtil.getBytes(url);
                Log.e("下载图片", "原图片大小 : " + bytes.length / 1024 + " kb");
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

