package com.lwc.common.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lwc.common.R;
import com.lwc.common.utils.ImageLoaderUtil;

import java.util.List;

/**
 * GridView加载数据的适配器
 * @author Administrator
 *
 */
public class MyGridViewPhotoAdpter extends BaseAdapter{

    private Context context;
    private List<String> lists;//数据源
    private boolean isShowDel = true;


    public MyGridViewPhotoAdpter(Context context, List<String> lists) {
        this.context = context;
        this.lists = lists;
    }

    public List<String> getLists() {
        return lists;
    }

    public void setLists(List<String> list) {
        lists = list;
    }

    /**
     * 先判断数据及的大小是否显示满本页lists.size() > (mIndex + 1)*mPagerSize
     * 如果满足，则此页就显示最大数量lists的个数
     * 如果不够显示每页的最大数量，那么剩下几个就显示几个
     */
    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public String getItem(int arg0) {
        return lists.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    public void setIsShowDel(boolean isShowDel) {
        this.isShowDel = isShowDel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.photo_grid_item, null);
            holder.delete_img = (ImageView)convertView.findViewById(R.id.delete_img);
            holder.iv_nul = (ImageView)convertView.findViewById(R.id.add_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
       if (!TextUtils.isEmpty(lists.get(position))) {
           if (lists.get(position).startsWith("http")) {
               ImageLoaderUtil.getInstance().displayFromNet(context, lists.get(position), holder.iv_nul);
           } else {
               ImageLoaderUtil.getInstance().displayFromLocal(context, holder.iv_nul, lists.get(position));
           }
           if (this.isShowDel) {
               holder.delete_img.setVisibility(View.VISIBLE);
               //添加item监听
               holder.delete_img.setTag(position);
               holder.delete_img.setOnClickListener(new View.OnClickListener() {

                   @Override
                   public void onClick(View arg0) {
                       int pion = (Integer) arg0.getTag();
                       lists.remove(pion);
                       if (!lists.get(lists.size() - 1).equals("")) {
                           lists.add("");
                       }
                       notifyDataSetChanged();
                   }
               });
           }
       } else {
           ImageLoaderUtil.getInstance().displayFromLocal(context, holder.iv_nul, R.drawable.add_img);
           holder.delete_img.setVisibility(View.GONE);
       }
        return convertView;
    }
    static class ViewHolder{
        private ImageView delete_img;
        private ImageView iv_nul;
    }
}