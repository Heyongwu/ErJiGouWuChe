package com.bwei.erjigouwuche.presenter;

import com.bwei.erjigouwuche.bean.GwcBean;
import com.bwei.erjigouwuche.model.Imodel.IMainModel;
import com.bwei.erjigouwuche.model.MainModel;
import com.bwei.erjigouwuche.net.OnNetListener;
import com.bwei.erjigouwuche.view.IMainActivity;

import java.util.ArrayList;
import java.util.List;



public class Mainpresenter {
    private  final IMainActivity iMainActivity;
    private final IMainModel imainModel;

    public Mainpresenter(IMainActivity iMainActivity) {
        this.iMainActivity = iMainActivity;
        imainModel = new MainModel();
    }
    public void getGwc(){
        imainModel.getGwc(new OnNetListener<GwcBean>() {
            @Override
            public void Onfailure(Exception e) {

            }

            @Override
            public void OnSuccess(GwcBean gwcBean) {
                List<GwcBean.DataBean> dataBeen = gwcBean.getData();
                ArrayList<List<GwcBean.DataBean.DatasBean>> childList = new ArrayList<>();
                for(int i = 0 ; i<dataBeen.size() ; i++){
                    List<GwcBean.DataBean.DatasBean> datas = dataBeen.get(i).getDatas();
                    childList.add(datas);
                }
                iMainActivity.showList(dataBeen,childList);
            }
        });
    }
}
