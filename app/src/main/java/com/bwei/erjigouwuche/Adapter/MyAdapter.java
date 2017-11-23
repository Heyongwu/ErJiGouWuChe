package com.bwei.erjigouwuche.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bwei.erjigouwuche.R;
import com.bwei.erjigouwuche.bean.EventBut;
import com.bwei.erjigouwuche.bean.GwcBean;
import com.bwei.erjigouwuche.bean.PriceAndCountEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class MyAdapter extends BaseExpandableListAdapter {
    private List<GwcBean.DataBean> groupList;
    private List<List<GwcBean.DataBean.DatasBean>> childList;
    private Context context;
    private final LayoutInflater inflater;


    public MyAdapter(List<GwcBean.DataBean> groupList, List<List<GwcBean.DataBean.DatasBean>> childList, Context context) {
        this.groupList = groupList;
        this.childList = childList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
    

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        final MyViewHolder_group holder_group;
        if (convertView == null) {
            holder_group = new MyViewHolder_group();

            view = inflater.inflate(R.layout.group_item, null);
            holder_group.group_cb = (CheckBox) view.findViewById(R.id.group_cb);
            holder_group.group_tv = (TextView) view.findViewById(R.id.group_tv);
            view.setTag(holder_group);
        } else {
            view = convertView;
            holder_group = (MyViewHolder_group) view.getTag();
        }
        final GwcBean.DataBean group_dataBean = groupList.get(groupPosition);
        holder_group.group_cb.setChecked(group_dataBean.isChecked());
        holder_group.group_tv.setText(group_dataBean.getTitle());
        holder_group.group_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PriceAndCountEvent priceAndCountEvent = computeEvenbut(holder_group.group_cb.isChecked(), groupPosition);
                //改变整个集合内的checked的选择状态
                group_dataBean.setChecked(holder_group.group_cb.isChecked());
                //如果单个一级列表选中
                if (holder_group.group_cb.isChecked()) {

                    //那么单个二级列表也要全部选中
                    isSelectChildCheck(groupPosition, true);
                    //如果一级列表全选那么就把全选状态变成true
                    setCountAndPrice(true,priceAndCountEvent.getPrice(),priceAndCountEvent.getCount());
                    if(isAllGroupListChecked()){
                        //改变全选状态为true
                        Qxa(true);
                    }
                }else {
                    //如果一级取消选中 那么二级也要取消
                    isSelectChildCheck(groupPosition, false);
                    setCountAndPrice(false,priceAndCountEvent.getPrice(),priceAndCountEvent.getCount());

                    Qxa(false);
                }
            }
        });
        return view;
    }



    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;

        final MyViewHodler_child hodler_child;
        if (convertView == null) {
            hodler_child = new MyViewHodler_child();
            view = inflater.inflate(R.layout.child_item, null);
            hodler_child.child_cb = (CheckBox) view.findViewById(R.id.child_cb);
            hodler_child.child_msg = (TextView) view.findViewById(R.id.child_msg);
            hodler_child.child_name = (TextView) view.findViewById(R.id.child_name);
            hodler_child.child_time = (TextView) view.findViewById(R.id.child_time);
            hodler_child.child_pri = (TextView) view.findViewById(R.id.child_pri);
            hodler_child.child_add = (TextView) view.findViewById(R.id.child_add);
            hodler_child.child_del = (TextView) view.findViewById(R.id.child_del);
            hodler_child.child_num = (TextView) view.findViewById(R.id.child_num);
            view.setTag(hodler_child);
        } else {
            view = convertView;
            hodler_child = (MyViewHodler_child) view.getTag();
        }
        final GwcBean.DataBean.DatasBean datasBean = childList.get(groupPosition).get(childPosition);
        hodler_child.child_cb.setChecked(datasBean.isChecked());
        hodler_child.child_pri.setText(datasBean.getPrice() + "");
        hodler_child.child_time.setText(datasBean.getAdd_time());
        hodler_child.child_name.setText(datasBean.getType_name());
        hodler_child.child_num.setText(datasBean.getNum()+"");
        hodler_child.child_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = datasBean.getNum();
                datasBean.setNum(++num);
                if(datasBean.isChecked()){
                    setCountAndPrice(true,datasBean.getPrice(),1);
                }
                notifyDataSetChanged();
            }
        });
        hodler_child.child_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = datasBean.getNum();
                if(num == 1){
                    return;
                }
                if(datasBean.isChecked()){
                    setCountAndPrice(false,datasBean.getPrice(),1);
                }
                datasBean.setNum(--num);
                notifyDataSetChanged();
            }
        });
        hodler_child.child_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //给holder.cbChild设置点击事件
                if (hodler_child.child_cb.isChecked()) {
                    datasBean.setChecked(true);
                        setCountAndPrice(true,datasBean.getPrice()*datasBean.getNum(),datasBean.getNum());
                    //if里面是判断其他的二级列表是否都选择 选择的话一级列表也都给true
                    if (isAllChildListChecked(groupPosition)) {
                        GwcBean.DataBean dataBean = groupList.get(groupPosition);
                        dataBean.setChecked(true);
                    }
                    //如何一级列表全选
                    if (isAllGroupListChecked()) {
                        Qxa(true);
                    }
                    notifyDataSetChanged();
                } else {
                    //取消后把二级列表选择false
                    datasBean.setChecked(false);
                    //把一级取消的false
                    GwcBean.DataBean dataBean = groupList.get(groupPosition);
                    dataBean.setChecked(false);
                    //把全选false
                    Qxa(false);
                    setCountAndPrice(false,datasBean.getPrice()*datasBean.getNum(),datasBean.getNum());
                    notifyDataSetChanged();
                }

            }
        });
        return view;
    }
    private PriceAndCountEvent computeEvenbut(boolean flag,int groupPostion){
        int count = 0;
        int price = 0;
        List<GwcBean.DataBean.DatasBean> datasBeen = childList.get(groupPostion);
        for (int i = 0; i < datasBeen.size(); i++) {
            GwcBean.DataBean.DatasBean datasBean = datasBeen.get(i);
           if(flag){
               if(!datasBean.isChecked()){
                   count++;
                   price+=datasBean.getPrice();
               }
           }else{
               if(datasBean.isChecked()){
                   count++;
                   price+=datasBean.getPrice();
               }
           }

        }
        PriceAndCountEvent priceAndCountEvent = new PriceAndCountEvent();
        priceAndCountEvent.setPrice(price);
        priceAndCountEvent.setCount(count);
        return priceAndCountEvent;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class MyViewHolder_group {
        CheckBox group_cb;
        TextView group_tv;
    }

    class MyViewHodler_child {
        CheckBox child_cb;
        TextView child_name;
        TextView child_msg;
        TextView child_time;
        TextView child_pri;
        TextView child_num;
        TextView child_del;
        TextView child_add;
    }

    /**
     * 遍历一个二级列表其他数据，判断其它的checkbox是否也都选中
     *
     * @return
     */
    public boolean isAllChildListChecked(int groupPostion) {
        List<GwcBean.DataBean.DatasBean> datasBeen = childList.get(groupPostion);
        for (int i = 0; i < datasBeen.size(); i++) {
            GwcBean.DataBean.DatasBean datasBean = datasBeen.get(i);
            if (!datasBean.isChecked()) {
                return false;
            }
        }
        return true;
    }


    /**
     * 改变MainActivity里的全选按钮状态
     * <p>
     * //     * @param isChecked
     */
    private void Qxa(boolean isChecked) {
        EventBut eventBut = new EventBut();
        eventBut.setChecked(isChecked);
        EventBus.getDefault().post(eventBut);
    };

    private void setCountAndPrice(boolean isAll,int price,int count){
        PriceAndCountEvent priceAndCountEvent = new PriceAndCountEvent();
        priceAndCountEvent.setCount(isAll ? count : -count);
        priceAndCountEvent.setPrice(isAll ? price : -price);
        EventBus.getDefault().post(priceAndCountEvent);
    }


    //判断一级了列表是否全部选择
    public boolean isAllGroupListChecked() {

        for (int i = 0; i < groupList.size(); i++) {
            GwcBean.DataBean dataBean = groupList.get(i);
            if (!dataBean.isChecked()) {
                return false;
            }
        }
        return true;
    }


    /**
     * 设置二级列表是否全选
     *
     * @param groupPosition
//     * @param isSelectAll   true 表示全选 false 表示全不选
     */
    private void isSelectChildCheck(int groupPosition, boolean b) {
        List<GwcBean.DataBean.DatasBean> datasBeen = childList.get(groupPosition);
        for (int i = 0; i <datasBeen.size() ; i++) {
            GwcBean.DataBean.DatasBean datasBean = datasBeen.get(i);
            datasBean.setChecked(b);
        }
        notifyDataSetChanged();
    }


    //判断一级是否全选 给主页面调用该方法
    public void SelectAllGroupListChecked(boolean isChecked) {
        int count = 0 ;
        int price = 0 ;
        for (int i = 0; i < childList.size(); i++) {
            List<GwcBean.DataBean.DatasBean> datasBeen = childList.get(i);
            count += datasBeen.size();
            for (int j = 0; j < datasBeen.size(); j++) {
                price += datasBeen.get(j).getPrice();
            }
        }
        setCountAndPrice(isChecked,price,count);


        for (int i = 0; i < groupList.size(); i++) {
            GwcBean.DataBean dataBean = groupList.get(i);
            //一级是否全选
            dataBean.setChecked(isChecked);
            //二级是否全选
            isSelectChildCheck(i,isChecked);
        }
        notifyDataSetChanged();

    }

}

