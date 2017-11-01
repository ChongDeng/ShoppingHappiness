package com.chongdeng.jufeng.shoppinghappiness.restful_client;

public interface ProgressListener {
    void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish);
}
