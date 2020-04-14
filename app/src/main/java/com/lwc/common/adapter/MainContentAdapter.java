package com.lwc.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lwc.common.R;
import com.lwc.common.adapter.viewholder.AdvertisementsViewHolder;
import com.lwc.common.adapter.viewholder.BaseViewHolder;
import com.lwc.common.adapter.viewholder.FastNavigationViewHolder;
import com.lwc.common.adapter.viewholder.ImageWheelViewHolder;
import com.lwc.common.adapter.viewholder.LeaseTypeViewHolder;
import com.lwc.common.adapter.viewholder.MealTypeViewHolder;
import com.lwc.common.adapter.viewholder.RepairTypeViewHolder;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.bean.BroadcastBean;
import com.lwc.common.module.bean.IndexAdBean;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.module.bean.Repairs;

import java.util.List;

public class MainContentAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ADInfo> adInfos; //轮播图片集合
    private List<Repairs> repairsList; //所有维修类型集合
    private List<Repairs> leaseTypeData; //所有维修类型集合
    private  List<PackageBean> packageBeanList; //所有维修类型集合
    private  List<IndexAdBean> indexAdBeanList; //所有维修类型集合
    private  List<BroadcastBean> noticeBeanList; //所有通知类型

    public MainContentAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * 设置轮播图数据
     */
    public void setImageWheelData(List<ADInfo> adInfos){
        this.adInfos = adInfos;
        notifyItemChanged(0);
    }

    /**
     * 设置滚动通知
     */
    public void setNotices(List<BroadcastBean> noticesList){
        this.noticeBeanList = noticesList;
        notifyItemChanged(1);
    }

    /**
     * s设置维修类型
     * @param repairsList
     */
    public void setRepairTypeData(List<Repairs> repairsList){
        this.repairsList = repairsList;
        notifyItemChanged(2);
    }

    /**
     * 设置租赁类型
     * @param leaseTypeData
     */
    public void setLeaseTypeData(List<Repairs> leaseTypeData){
        this.leaseTypeData = leaseTypeData;
        notifyItemChanged(3);
    }

    /**
     * 设置租赁位置数据
     * @param indexAdBeanList
     */
    public void setIndexAdData(List<IndexAdBean> indexAdBeanList){
        this.indexAdBeanList = indexAdBeanList;
        notifyItemChanged(4);
    }

    /**
     * 设置套餐数据
     * @param packageBeanList
     */
    public void setMealTypeData(List<PackageBean> packageBeanList){
        this.packageBeanList = packageBeanList;
        notifyItemChanged(5);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View presentView;
        RecyclerView.ViewHolder presentViewHolder = null;
        switch (viewType){
            case 0:
                 presentView = LayoutInflater.from(mContext).inflate(R.layout.item_main_image_wheel,parent,false);
                 presentViewHolder = new ImageWheelViewHolder(presentView);
            break;
            case 1:
                presentView = LayoutInflater.from(mContext).inflate(R.layout.item_main_fast_navigation,parent,false);
                presentViewHolder = new FastNavigationViewHolder(presentView);
                break;
            case 2:
                presentView = LayoutInflater.from(mContext).inflate(R.layout.item_main_repair_type,parent,false);
                presentViewHolder = new RepairTypeViewHolder(presentView);
                break;
            case 3:
                presentView = LayoutInflater.from(mContext).inflate(R.layout.item_main_lease_type,parent,false);
                presentViewHolder = new LeaseTypeViewHolder(presentView);
                break;
            case 4:
                presentView = LayoutInflater.from(mContext).inflate(R.layout.item_main_advertisements,parent,false);
                presentViewHolder = new AdvertisementsViewHolder(presentView);
                break;
            case 5:
                presentView = LayoutInflater.from(mContext).inflate(R.layout.item_main_meal_type,parent,false);
                presentViewHolder = new MealTypeViewHolder(presentView);
                break;
        }
        return presentViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
        switch (position) {
            case 0:
                if (adInfos != null && adInfos.size() > 0) {
                    baseViewHolder.itemView.getRootView().setVisibility(View.VISIBLE);
                    baseViewHolder.bindViewHolder(adInfos, mContext);
                } else {
          /*          RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) baseViewHolder.itemView.getLayoutParams();
                    layoutParams.height = 0;
                    baseViewHolder.itemView.setLayoutParams(layoutParams);
                    baseViewHolder.itemView.getRootView().setVisibility(View.GONE);*/
                }
                break;
            case 1:
                    baseViewHolder.itemView.getRootView().setVisibility(View.VISIBLE);
                    baseViewHolder.bindViewHolder(noticeBeanList, mContext);
                break;
            case 2:
                if (repairsList != null && repairsList.size() > 0) {
                    baseViewHolder.itemView.getRootView().setVisibility(View.VISIBLE);
                    baseViewHolder.bindViewHolder(repairsList, mContext);
                } else {
                /*    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) baseViewHolder.itemView.getLayoutParams();
                    layoutParams.height = 0;
                    baseViewHolder.itemView.setLayoutParams(layoutParams);
                    baseViewHolder.itemView.getRootView().setVisibility(View.GONE);*/
                }
                break;
            case 3:
                //if (leaseTypeData != null && leaseTypeData.size() > 0) {
                if (false) {
                    baseViewHolder.bindViewHolder(leaseTypeData, mContext);

                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) baseViewHolder.itemView.getLayoutParams();
                    layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                    baseViewHolder.itemView.setLayoutParams(layoutParams);
                    baseViewHolder.itemView.getRootView().setVisibility(View.VISIBLE);
                } else {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) baseViewHolder.itemView.getLayoutParams();
                    layoutParams.height = 0;
                    baseViewHolder.itemView.setLayoutParams(layoutParams);
                    baseViewHolder.itemView.getRootView().setVisibility(View.GONE);
                }
                break;
            case 4:
                if (indexAdBeanList != null && indexAdBeanList.size() > 0) {
                    baseViewHolder.bindViewHolder(indexAdBeanList, mContext);
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) baseViewHolder.itemView.getLayoutParams();
                    layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                    baseViewHolder.itemView.setLayoutParams(layoutParams);
                    baseViewHolder.itemView.getRootView().setVisibility(View.VISIBLE);
                }else{
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) baseViewHolder.itemView.getLayoutParams();
                    layoutParams.height = 0;
                    baseViewHolder.itemView.setLayoutParams(layoutParams);
                    baseViewHolder.itemView.getRootView().setVisibility(View.GONE);
                }
                break;
            case 5:
                if (packageBeanList != null && packageBeanList.size() > 0) {
                    baseViewHolder.bindViewHolder(packageBeanList, mContext);
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) baseViewHolder.itemView.getLayoutParams();
                    layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                    baseViewHolder.itemView.setLayoutParams(layoutParams);
                    baseViewHolder.itemView.getRootView().setVisibility(View.VISIBLE);
                } else {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) baseViewHolder.itemView.getLayoutParams();
                    layoutParams.height = 0;
                    baseViewHolder.itemView.setLayoutParams(layoutParams);
                    baseViewHolder.itemView.getRootView().setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
