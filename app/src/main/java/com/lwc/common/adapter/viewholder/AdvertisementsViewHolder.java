package com.lwc.common.adapter.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.lwc.common.R;
import com.lwc.common.module.bean.IndexAdBean;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvertisementsViewHolder extends BaseViewHolder{

    @BindView(R.id.iv_bottom)
    ImageView iv_bottom;
    @BindView(R.id.iv_top)
    ImageView iv_top;
    @BindView(R.id.iv_right)
    ImageView iv_right;
    @BindView(R.id.iv_left)
    ImageView iv_left;

    public AdvertisementsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void bindViewHolder(Object data, Context context) {
        List<IndexAdBean> indexAdBeans = (List<IndexAdBean>) data;
        for(int i = 0;i< 4;i++){
            IndexAdBean indexAdBean = indexAdBeans.get(i);

            String imgUrl = indexAdBean.getLeaseUrl();

            if(indexAdBean.getImageLocalhost() == 1){
                ImageLoaderUtil.getInstance().displayFromNetD(context,imgUrl,iv_top,R.drawable.img_default_load);
            }else if(indexAdBean.getImageLocalhost() == 2){
                ImageLoaderUtil.getInstance().displayFromNetD(context,imgUrl,iv_bottom,R.drawable.img_default_load);
            }else if(indexAdBean.getImageLocalhost() == 3){
                ImageLoaderUtil.getInstance().displayFromNetD(context,imgUrl,iv_left,R.drawable.img_default_load);
            }else if(indexAdBean.getImageLocalhost() == 4){
                ImageLoaderUtil.getInstance().displayFromNetD(context,imgUrl,iv_right,R.drawable.img_default_load);
            }
        }
    }

}
