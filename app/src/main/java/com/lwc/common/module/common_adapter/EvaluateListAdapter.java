package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.hedgehog.ratingbar.RatingBar;
import com.lwc.common.R;
import com.lwc.common.module.bean.Evaluate;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.widget.CircleImageView;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 评价
 */
public class EvaluateListAdapter extends SuperAdapter<Evaluate> {

    private Context context;
    public EvaluateListAdapter(Context context, List<Evaluate> items, int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, Evaluate item) {
        RatingBar ratingBar = holder.findViewById(R.id.ratingBar);
        String grade =  String.valueOf(item.getSynthesizeGrade());
        if (!TextUtils.isEmpty(grade)) {
            Float avgservice = Float.parseFloat(grade);
            ratingBar.setStarCount(5);
            ratingBar.setStar(avgservice);
        } else {
            ratingBar.setStar(0);
        }
        holder.setText(R.id.txtDate, item.getCreateTime());  //订单号
        holder.setText(R.id.txtContent, item.getCommentContent());
        if (!TextUtils.isEmpty(item.getOrderContactName())) {
            holder.setText(R.id.txtName, item.getOrderContactName().substring(0, 1)+"**");
        }

     /*   CircleImageView imageView = holder.getView(R.id.imgHead);
        ImageLoaderUtil.getInstance().displayFromNet(context,item.getMaintenanceHeadImage(),imageView);*/
    }
}
