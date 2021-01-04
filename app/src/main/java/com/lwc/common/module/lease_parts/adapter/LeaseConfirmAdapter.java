package com.lwc.common.module.lease_parts.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.lease_parts.bean.ShopCarBean;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

public class LeaseConfirmAdapter extends SuperAdapter<ShopCarBean> {

    private Context context;
    public LeaseConfirmAdapter(Context context, List<ShopCarBean> items, int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final ShopCarBean item) {

        ImageView iv_display = holder.itemView.findViewById(R.id.iv_display);
        TextView tv_title = holder.itemView.findViewById(R.id.tv_title);
        TextView tv_spece = holder.itemView.findViewById(R.id.tv_spece);
        TextView tv_prices = holder.itemView.findViewById(R.id.tv_prices);
        TextView tv_sum = holder.itemView.findViewById(R.id.tv_sum);
        TextView tv_pay_method = holder.itemView.findViewById(R.id.tv_pay_method);
        EditText et_message = holder.itemView.findViewById(R.id.et_message);

        ImageLoaderUtil.getInstance().displayFromNetDCircular(context,item.getGoodsImg(),iv_display,R.drawable.image_default_picture);

        String goodsName = item.getGoodsName();
        String goodsNameStr = "租赁  " + goodsName;
        SpannableStringBuilder showGoodsName = Utils.getSpannableStringBuilder(0, 2, mContext.getResources().getColor(R.color.transparent), goodsNameStr, 10, true);
        tv_title.setText(showGoodsName);

        //tv_title.setText(item.getGoodsName());

        tv_spece.setText(item.getLeaseSpecs()+","+item.getLeaseMonTime()+"个月" + ","+("1".equals(item.getPayType())?"按月付":"按季付"));

        String money = Utils.getMoney(Utils.chu(item.getGoodsPrice(),"100"));
        SpannableStringBuilder spannableStringBuilder = Utils.getSpannableStringBuilder(1,money.length() - 2,"￥"+ money,18,true);

        tv_prices.setText(spannableStringBuilder);
        tv_sum.setText("x"+item.getGoodsNum());
        if("1".equals(item.getPayType())){
            tv_pay_method.setText("付款    按月支付");
        }else{
            tv_pay_method.setText("付款    按季支付");
        }

        et_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                item.setRemark(str);
            }
        });

    }
}
