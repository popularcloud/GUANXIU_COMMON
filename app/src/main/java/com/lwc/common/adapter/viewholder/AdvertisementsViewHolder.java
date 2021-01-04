package com.lwc.common.adapter.viewholder;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.lwc.common.R;
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.module.bean.IndexAdBean;
import com.lwc.common.module.lease_parts.activity.LeaseGoodsDetailActivity;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvertisementsViewHolder extends BaseViewHolder{

    @BindView(R.id.iv_bottom)
    ImageView iv_bottom;
    @BindView(R.id.iv_top)
    ImageView iv_top;
    @BindView(R.id.iv_right)
    ImageView iv_right;
    @BindView(R.id.iv_left)
    ImageView iv_left;

    private Context mContext;

    public AdvertisementsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void bindViewHolder(Object data, Context context) {

        List<IndexAdBean> indexAdBeans = (List<IndexAdBean>) data;

        mContext = context;

        for(int i = 0;i< indexAdBeans.size();i++){
            IndexAdBean indexAdBean = indexAdBeans.get(i);

            String imgUrl = indexAdBean.getLeaseImageUrl();

            if(indexAdBean.getImageLocalhost() == 1){
                ImageLoaderUtil.getInstance().displayFromNetD(context,imgUrl,iv_top,R.drawable.img_default_load);
                iv_top.setTag(R.id.image_target,indexAdBean);
            }else if(indexAdBean.getImageLocalhost() == 2){
                ImageLoaderUtil.getInstance().displayFromNetD(context,imgUrl,iv_bottom,R.drawable.img_default_load);
                iv_bottom.setTag(R.id.image_target,indexAdBean);
            }else if(indexAdBean.getImageLocalhost() == 3){
                ImageLoaderUtil.getInstance().displayFromNetD(context,imgUrl,iv_left,R.drawable.img_default_load);
                iv_left.setTag(R.id.image_target,indexAdBean);
            }else if(indexAdBean.getImageLocalhost() == 4){
                ImageLoaderUtil.getInstance().displayFromNetD(context,imgUrl,iv_right,R.drawable.img_default_load);
                iv_right.setTag(R.id.image_target,indexAdBean);
            }
        }
    }

    @OnClick({R.id.iv_bottom,R.id.iv_top,R.id.iv_right,R.id.iv_left,})
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.iv_bottom:
            case R.id.iv_top:
            case R.id.iv_right:
            case R.id.iv_left:
                IndexAdBean indexAdBean = (IndexAdBean) view.getTag(R.id.image_target);
                if(indexAdBean != null){
                    if("1".equals(indexAdBean.getUrlType())){ //0 无链接 1租赁商品详情 2外部链接
                        if (Utils.gotoLogin(MainActivity.user,MainActivity.activity)) {
                            Bundle bundle = new Bundle();
                            bundle.putString("goodId",indexAdBean.getLeaseUrl());
                            IntentUtil.gotoActivity(mContext, LeaseGoodsDetailActivity.class,bundle);
                        }
                    }else if("2".equals(indexAdBean.getUrlType())){  //其他为外部链接 直接打开网页
                        Bundle bundle = new Bundle();
                        if (TextUtils.isEmpty(indexAdBean.getLeaseUrl())){
                            return;
                        }
                        bundle.putString("url", indexAdBean.getLeaseUrl());
                        if (!TextUtils.isEmpty(indexAdBean.getLeaseTitle()))
                            bundle.putString("title", indexAdBean.getLeaseTitle());
                        IntentUtil.gotoActivity(MainActivity.activity, InformationDetailsActivity.class, bundle);
                    }
                }
                break;
        }
    }

}
