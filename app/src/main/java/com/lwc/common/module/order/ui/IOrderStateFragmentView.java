package com.lwc.common.module.order.ui;

import com.lwc.common.module.bean.OrderState;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 订单状态
 */
public interface IOrderStateFragmentView {

    void notifyData(List<OrderState> orderStates);

    /**
     * 设置显示状态的按钮文字
     * @param state
     */
    void setStateBtnText(String state);


    BGARefreshLayout getBGARefreshLayout();

    /**
     * 切换界面底部两个布局操作的逻辑
     * @param isShowBototnDoubleButton ture 显示双按钮   flase  显示单按钮
     */
    void cutBottomButton(boolean isShowBototnDoubleButton);

    /**
     * 设置FinishLayout 布局文字
     * @param leftName  左边文字
     * @param rightName 右边文字
     */
    void setFinishLayoutBtnName(String leftName, String rightName);

    /**
     * 设置底部颜色
     * @param colour
     */
    void setBottomButtonColour(int colour);
}
