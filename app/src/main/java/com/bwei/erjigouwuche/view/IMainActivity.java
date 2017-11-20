package com.bwei.erjigouwuche.view;

import com.bwei.erjigouwuche.bean.GwcBean;

import java.util.List;



public interface IMainActivity {
    void showList(List<GwcBean.DataBean> groupList, List<List<GwcBean.DataBean.DatasBean>> childList);


}
