package com.bwei.erjigouwuche.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.bwei.erjigouwuche.Adapter.MyAdapter;
import com.bwei.erjigouwuche.R;
import com.bwei.erjigouwuche.bean.EventBut;
import com.bwei.erjigouwuche.bean.GwcBean;
import com.bwei.erjigouwuche.bean.PriceAndCountEvent;
import com.bwei.erjigouwuche.presenter.Mainpresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private ExpandableListView mElv;
    private CheckBox mMainCb;
    /**
     * 0
     */
    private TextView mTvPrice;
    /**
     * 结算(0)
     */
    private TextView mTvNum;
    private MyAdapter adapter;
    private int totlcount =0;
    private int totlprice =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        initView();
        Mainpresenter mainpresenter = new Mainpresenter(MainActivity.this);
        mainpresenter.getGwc();
        mMainCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.SelectAllGroupListChecked(mMainCb.isChecked());
            }
        });
    }

    private void initView() {
        mElv = (ExpandableListView) findViewById(R.id.elv);
        mMainCb = (CheckBox) findViewById(R.id.main_cb);
        mTvPrice = (TextView) findViewById(R.id.tv_price);
        mTvNum = (TextView) findViewById(R.id.tv_num);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showList(List<GwcBean.DataBean> groupList, List<List<GwcBean.DataBean.DatasBean>> childList) {
        adapter = new MyAdapter(groupList, childList, MainActivity.this);
        mElv.setAdapter(adapter);
//        mElv.setGroupIndicator(null);
        //默认让其全部展开
        for (int i = 0; i < groupList.size(); i++) {
            mElv.expandGroup(i);
        }
    }
    @Subscribe
    public void MessageEvent(EventBut event){
        mMainCb.setChecked(event.isChecked());

    }

    @Subscribe
    public void MessageEvent(PriceAndCountEvent event){
        totlcount += event.getCount();
        totlprice += event.getPrice();
        mTvNum.setText("结算(" + totlcount + ")");
        mTvPrice.setText(totlprice + "");
    }

}
