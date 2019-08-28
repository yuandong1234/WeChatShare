package com.kaisa.yql;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

/**
 * 倒计时工具类
 */
public class CountDownHelper {
    private Handler handler;
    private long seconds;
    private boolean isRun;
    private CountDownCallback callback;
    private CountDownTask task;

    public CountDownHelper(CountDownCallback callback) {
        this.callback = callback;
        handler = new CountDownHandler();
    }


    private class CountDownTask extends Thread {
        @Override
        public void run() {

            while (isRun) {
                try {
                    Thread.sleep(1000);

                    if (seconds == 0) {
                        isRun = false;
                        return;
                    }
                    seconds--;

                    if (handler != null) {
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private class CountDownHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long[] time = secondToTime(seconds);
                    if (callback != null) {
                        callback.callback(time);
                    }
                    break;
            }
        }
    }

    public interface CountDownCallback {
        void callback(long[] time);
    }


    /**
     * 将秒数转换为日时分秒，
     *
     * @param second
     * @return
     */
    private long[] secondToTime(long second) {
        long days = second / 86400;            //转换天数
        second = second % 86400;            //剩余秒数
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second / 60;            //转换分钟
        second = second % 60;//剩余秒数

        return new long[]{days, hours, minutes, second};
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public void start() {
        isRun = true;
        task = new CountDownTask();
        task.start();
    }

    public void stop() {
        isRun = false;
        task = null;
        seconds = 0;
    }

}
