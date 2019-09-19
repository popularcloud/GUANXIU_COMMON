package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.bean.Malfunction;
import com.lwc.common.module.bean.Order;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 我的订单
 */
public class OrderListAdapter extends SuperAdapter<Order> {

    private Context context;
    public OrderListAdapter(Context context, List<Order> items, int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, Order item) {
        TextView textView = holder.findViewById(R.id.txtId);
        holder.setText(R.id.txtId, "订单号： " + item.getOrderId());  //订单号
        holder.setText(R.id.txtTime, item.getCreateTime());
//        String type = item.getDeviceTypeName();
//        if (item.getReqairName() != null && !item.getReqairName().equals("")){
//            type = type +"->"+item.getReqairName();
//        }
        if (!TextUtils.isEmpty(item.getIsSecrecy()) && item.getIsSecrecy().equals("1")) {
            textView.setCompoundDrawables(null, null, Utils.getDrawable(this.context, R.drawable.renzheng), null);
        } else {
            textView.setCompoundDrawables(null, null, null, null);
        }
        List<Malfunction> orderRepairs = item.getOrderRepairs();
        if (orderRepairs != null && orderRepairs.size() > 0){
            String typeName = "";
            for (int i = 0; i< orderRepairs.size(); i++) {
                Malfunction ma = orderRepairs.get(i);
                if (i == orderRepairs.size()-1) {
                    typeName = typeName+ma.getDeviceTypeName()+"->"+ma.getReqairName();
                } else {
                    typeName = typeName+ma.getDeviceTypeName()+"->"+ma.getReqairName()+"\n";
                }
            }
            holder.setText(R.id.txtMachineType, typeName);
        } else {
            holder.setText(R.id.txtMachineType, item.getDeviceTypeName()+"->"+item.getReqairName());
        }
//        holder.setText(R.id.txtMachineType, type); //设备类型
        if (!TextUtils.isEmpty(item.getOrderDescription())) {
            holder.setText(R.id.txtReason, item.getOrderDescription());// 故障原因
            holder.setVisibility(R.id.rl_ms, View.VISIBLE);
        } else {
            holder.setText(R.id.txtReason, "暂无");// 故障原因
            holder.setVisibility(R.id.rl_ms, View.GONE);
        }

        if (item.getMaintenanceName() != null && !TextUtils.isEmpty(item.getMaintenanceName())) { //维修员名称
            holder.setText(R.id.txtOrderName, item.getMaintenanceName());
        } else {
            holder.setText(R.id.txtOrderName, "暂无");
        }

        if (item.getCompanyName() != null && !TextUtils.isEmpty(item.getCompanyName())) {  //维修员电话
            holder.setText(R.id.txtUnit, item.getCompanyName());
        } else {
            holder.setText(R.id.txtUnit, "暂无");
        }

        switch (item.getStatusId()){
            case "11":
                holder.setText(R.id.txtStatus,"待评价");
                break;
            case "12":
                holder.setText(R.id.txtStatus,"已评价");
                break;
            default:
                if (item.getStatusName()!=null && !item.getStatusName().trim().equals("")) {
                    holder.setText(R.id.txtStatus, item.getStatusName());
                } else {
                    holder.setVisibility(R.id.txtStatus, View.GONE);
                }
                break;
        }
    }
}
