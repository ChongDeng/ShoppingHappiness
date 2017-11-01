package com.chongdeng.jufeng.shoppinghappiness.restful_client;

import android.os.Handler;
import android.os.Message;

public class DefaultProgressListener implements ProgressListener {
    private Handler mHandler;

    //多文件上传时，index作为上传的位置的标志
    private int mFileIndex;

    public DefaultProgressListener(Handler mHandler, int mFileIndex) {
        this.mHandler = mHandler;
        this.mFileIndex = mFileIndex;
    }
    @Override
    public void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish) {
        int percent = UpLoadUtils.getInstance().UpdateAndGetUploadPercent(hasWrittenLen, mFileIndex);
        if (percent > 100) percent = 100;
        if (percent < 0) percent = 0;

        Message msg = Message.obtain();
        msg.what = percent;
        msg.arg1 = mFileIndex;
        mHandler.sendMessage(msg);
    }
}
