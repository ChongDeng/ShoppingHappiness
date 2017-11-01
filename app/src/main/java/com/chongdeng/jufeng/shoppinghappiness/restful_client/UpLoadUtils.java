package com.chongdeng.jufeng.shoppinghappiness.restful_client;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fqyang on 10/28/2017.
 */

public class UpLoadUtils {
    private static UpLoadUtils instance;

    private long TotalLen = 0;
    private long CurLen = 0;

    //key is fileindex, value is the total lenghth of this file
    private Map<Integer, Long> FileLenMaps = null;

    private UpLoadUtils(){
        FileLenMaps =  new HashMap<Integer, Long>();
    }
    public void SetTotalLen(long Len){
        TotalLen = Len;
    }
    public void UpdateTotalLen(long Len){
            TotalLen += Len;
    }
    public long getTotalLen( ){
        return TotalLen;
    }

    public int UpdateAndGetUploadPercent(long NewWrittenLen, int FileIndex){

        FileLenMaps.put(FileIndex, NewWrittenLen);

        long UploadedLen = 0;
        for (int FileNo : FileLenMaps.keySet() ){
            UploadedLen += FileLenMaps.get(FileNo);
        }
        CurLen = UploadedLen;

        int percent = (int) (CurLen * 100 / TotalLen);
        if(percent >=100) reset();
        return percent;
    }

    public void reset(){
        TotalLen = 0;
        CurLen = 0;
        FileLenMaps = null;
    }

    public static UpLoadUtils getInstance(){
        if (instance == null){
            instance = new UpLoadUtils();
        }
        return instance;
    }
}
