package com.lwc.common.module.lease_parts.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.lease_parts.bean.MyOrderBean;
import com.lwc.common.module.lease_parts.inteface_callback.OnOrderBtnClick;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

public class LeaseOrderNeedPayAdapter extends SuperAdapter<MyOrderBean> {

    private Context context;
    private int pageType = 0;

    private OnOrderBtnClick onOrderBtnClick;

    public LeaseOrderNeedPayAdapter(Context context, List<MyOrderBean> items, int layoutResId, int pageType, OnOrderBtnClick onOrderBtnClick) {
        super(context, items, layoutResId);
        this.context = context;
        this.pageType = pageType;
        this.onOrderBtnClick = onOrderBtnClick;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, final MyOrderBean item) {

        TextView tv_btn01 = holder.itemView.findViewById(R.id.tv_btn01);
        TextView tv_btn02 = holder.itemView.findViewById(R.id.tv_btn02);
        TextView tv_btn05 = holder.itemView.findViewById(R.id.tv_btn05);
        TextView txtStatus = holder.itemView.findViewById(R.id.txtStatus);
        TextView tv_order_num = holder.itemView.findViewById(R.id.tv_order_num);
        ImageView iv_header = holder.itemView.findViewById(R.id.iv_header);
        TextView tv_title = holder.itemView.findViewById(R.id.tv_title);
        TextView tv_spece = holder.itemView.findViewById(R.id.tv_spece);
        TextView tv_price = holder.itemView.findViewById(R.id.tv_price);
        TextView tv_number = holder.itemView.findViewById(R.id.tv_number);
        TextView tv_total = holder.itemView.findViewById(R.id.tv_total);

        txtStatus.setText(item.getStatusName());
        tv_order_num.setText("订单号："+item.getOrderId());
        ImageLoaderUtil.getInstance().displayFromNetDCircular(context,item.getGoodsImg(),iv_header,R.drawable.image_default_picture);

        tv_spece.setText(item.getLeaseSpecs()+","+item.getLeaseMonTime()+"个月,"+ ("1".equals(item.getPayType())?"月付":"季付"));

        String goodsPrice = Utils.chu(item.getGoodsPrice(), "100");
        String goodsPriceStr = "￥" + goodsPrice;
        SpannableStringBuilder showPrices = Utils.getSpannableStringBuilder(1, goodsPrice.length()+1, context.getResources().getColor(R.color.black), goodsPriceStr, 24, true);
        tv_price.setText(Utils.getMoney(showPrices.toString()));

        String goodsName = item.getGoodsName();
        String goodsNameStr = "租赁  " + goodsName;
        SpannableStringBuilder showGoodsName = Utils.getSpannableStringBuilder(0, 2, mContext.getResources().getColor(R.color.transparent), goodsNameStr, 10, true);
        tv_title.setText(showGoodsName);

        tv_number.setText("X"+item.getGoodsNum());
        String totalMoney = "共计"+item.getGoodsNum()+"件商品 合计:"+ "￥" + Utils.chu(item.getPayPrice(),"100");
        tv_total.setText(Utils.getMoney(totalMoney));

        tv_btn01.setVisibility(View.VISIBLE);
        tv_btn05.setVisibility(View.VISIBLE);

        tv_btn01.setText("缴费");

        if(item.getGoodsRealNum() > 0){
            tv_btn02.setText("退租");
            tv_btn02.setVisibility(View.VISIBLE);
        }else{
            tv_btn02.setVisibility(View.GONE);
        }


        switch (item.getStatusId()){
            case "4": //租用中
                tv_btn01.setText("缴费");
                break;
            case "5": //已逾期
                tv_btn01.setText("缴费");
                break;
            case "9": //已到期
                tv_btn01.setText("续租");
                break;
        }

        tv_btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView myView = (TextView) v;
                if(onOrderBtnClick != null){
                    onOrderBtnClick.onItemClick(myView.getText().toString(),layoutPosition,item);
                }
            }
        });

        tv_btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView myView = (TextView) v;
                if(onOrderBtnClick != null){
                    onOrderBtnClick.onItemClick(myView.getText().toString(),layoutPosition,item);
                }
            }
        });

        tv_btn05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView myView = (TextView) v;
                if(onOrderBtnClick != null){
                    onOrderBtnClick.onItemClick(myView.getText().toString(),layoutPosition,item);
                }
            }
        });
    }
}
