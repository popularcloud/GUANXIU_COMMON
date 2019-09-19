package com.lwc.common.module.integral.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lwc.common.R;
import com.lwc.common.interf.OnItemClickCallBack;
import com.lwc.common.module.integral.bean.IntegralLuckBean;
import com.lwc.common.utils.ImageLoaderUtil;

import java.util.List;

/**
 * @author younge
 * @date 2019/3/27 0027
 * @email 2276559259@qq.com
 */
public class LuckAdapter extends RecyclerView.Adapter<LuckAdapter.ViewHolder>{

    private List<IntegralLuckBean.GoodsBean> lists;
    private Context context;
    private OnItemClickCallBack onItemClickCallBack;
    boolean isLuck = false;

    public LuckAdapter(Context context, List<IntegralLuckBean.GoodsBean> lists, OnItemClickCallBack onItemClickCallBack){
        this.lists = lists;
        this.context = context;
        this.onItemClickCallBack = onItemClickCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = View.inflate(context, R.layout.item_luck, null);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        IntegralLuckBean.GoodsBean bean = lists.get(position);
        if(position == 4){
            ImageLoaderUtil.getInstance().displayFromLocal(context,holder.iv_header,R.drawable.ic_luck_right);
        }else{
            ImageLoaderUtil.getInstance().displayFromNetD(context,bean.getLuckyImage(),holder.iv_header);
        }

        if(isLuck){ //开始抽奖

        }

        if(position != 4){
            holder.itemView.setAlpha(0.5f);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickCallBack != null){
                    onItemClickCallBack.onItemClick(position);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return lists == null?0:lists.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_header;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_header = (ImageView) itemView.findViewById(R.id.item_image);
        }
    }
}
