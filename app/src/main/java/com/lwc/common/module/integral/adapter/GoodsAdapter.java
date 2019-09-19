package com.lwc.common.module.integral.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.interf.OnItemClickCallBack;
import com.lwc.common.module.integral.bean.IntegralgoodsBean;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.widget.ZQImageViewRoundOval;

import java.util.List;

/**
 * @author younge
 * @date 2019/3/27 0027
 * @email 2276559259@qq.com
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder>{

    private List<IntegralgoodsBean> lists;
    private Context context;
    private OnItemClickCallBack onItemClickCallBack;

    public GoodsAdapter(Context context, List<IntegralgoodsBean> lists, OnItemClickCallBack onItemClickCallBack){
        this.lists = lists;
        this.context = context;
        this.onItemClickCallBack = onItemClickCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = View.inflate(context, R.layout.goods_grid_item, null);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        IntegralgoodsBean integralgoodsBean = lists.get(position);
        if(integralgoodsBean.getIntegralName() != null){
            holder.tv_title.setText(integralgoodsBean.getIntegralName());
        }

        holder.tv_integral.setText(Utils.chu(String.valueOf(integralgoodsBean.getIntegralNum()),"100")+" 积分");
        holder.iv_header.setType(ZQImageViewRoundOval.TYPE_ROUND);
        ImageLoaderUtil.getInstance().displayFromNetD(context,integralgoodsBean.getIntegralCover(),holder.iv_header);
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
        private TextView tv_title;
        private TextView tv_integral;
        private ZQImageViewRoundOval iv_header;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.item_title);
            iv_header = (ZQImageViewRoundOval) itemView.findViewById(R.id.item_image);
            tv_integral = (TextView) itemView.findViewById(R.id.item_integral);
        }
    }
}
