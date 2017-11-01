package com.chongdeng.jufeng.shoppinghappiness.restful_client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "http://192.168.1.88:5000";
    private static Retrofit retrofit = null;

    public static Retrofit FileUpLoadRetrofit;
    private static OkHttpClient okHttpClient = new OkHttpClient();

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static Retrofit getFileUpLoadRetrofitClient() {
        if (FileUpLoadRetrofit == null) {
            synchronized (ApiClient.class) {
                Retrofit.Builder builder = new Retrofit.Builder();
                FileUpLoadRetrofit = builder.baseUrl("http://192.168.1.88:5000")
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
            }
        }
        return FileUpLoadRetrofit;
    }

//    public static <T> T createService(Class<T> clazz) {
//        if (FileUpLoadRetrofit == null) {
//            synchronized (RetrofitUtil.class) {
//                Retrofit.Builder builder = new Retrofit.Builder();
//                FileUpLoadRetrofit = builder.baseUrl("http://192.168.1.88:5000")
//                        .client(okHttpClient)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                        .build();
//            }
//        }
//        return FileUpLoadRetrofit.create(clazz);
//    }
}
