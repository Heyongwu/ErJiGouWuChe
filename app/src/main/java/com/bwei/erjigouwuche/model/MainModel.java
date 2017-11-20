package com.bwei.erjigouwuche.model;


import com.bwei.erjigouwuche.bean.BeanModel;
import com.bwei.erjigouwuche.bean.GwcBean;
import com.bwei.erjigouwuche.model.Imodel.IMainModel;
import com.bwei.erjigouwuche.net.Api;
import com.bwei.erjigouwuche.net.HttpUtils;
import com.bwei.erjigouwuche.net.OnNetListener;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainModel extends BeanModel implements IMainModel{


    @Override
    public void getGwc(final OnNetListener<GwcBean> onNetListener) {
        HttpUtils.getHttpUtils().doGet(Api.url, new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onNetListener.Onfailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                final GwcBean gwcBean = new Gson().fromJson(string, GwcBean.class);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onNetListener.OnSuccess(gwcBean);
                    }
                });
            }
        });
    }
}
