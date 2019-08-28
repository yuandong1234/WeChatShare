package com.kaisa.yql.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kaisa.yql.MyApplication;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(WXEntryActivity.this, MyApplication.APP_ID, true);
        api.registerApp(MyApplication.APP_ID);
        try {
            boolean result = api.handleIntent(getIntent(), this);
            if (!result) {
                Log.e(TAG, "onCreate: " + "参数不合法，未被SDK处理，退出");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.e(TAG, "onResp: " + "baseResp--B:" + baseResp.errStr + "," + baseResp.openId + "," + baseResp.transaction + "," + baseResp.errCode);
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.e(TAG, "onResp: " + "发送成功");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.e(TAG, "onResp: " + "发送取消");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.e(TAG, "onResp: " + "发送被拒绝");
                break;
            case BaseResp.ErrCode.ERR_BAN:
                Log.e(TAG, "onResp: " + "签名错误");
                break;
            default:
                break;
        }
        finish();
    }
}
