package com.kaisa.yql;

import android.app.Application;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class MyApplication extends Application {
    public static final String APP_ID = "wx685dee45aa3ff2b3";
    //public static final String APP_ID = "wxd930ea5d5a258f4f";
    private static IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        registerWeChat();
    }

    private void registerWeChat() {
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.registerApp(APP_ID);
    }
    public static IWXAPI getWeChatApi() {
        return api;
    }
}
