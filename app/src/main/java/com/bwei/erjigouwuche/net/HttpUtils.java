package com.bwei.erjigouwuche.net;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;


public class HttpUtils {
    private static HttpUtils httpUtils;
    private final OkHttpClient client;

    public HttpUtils() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
    }
    public static HttpUtils getHttpUtils(){
        if(httpUtils == null){
            synchronized (HttpUtils.class){
                if(httpUtils == null){
                    httpUtils = new HttpUtils();
                }
            }
        }
        return httpUtils;
    }
    public void doGet(String url, Callback callback){
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }

}
