package com.bwei.erjigouwuche.model.Imodel;


import com.bwei.erjigouwuche.bean.GwcBean;
import com.bwei.erjigouwuche.net.OnNetListener;

public interface IMainModel {
    public void getGwc(OnNetListener<GwcBean> onNetListener);
}
