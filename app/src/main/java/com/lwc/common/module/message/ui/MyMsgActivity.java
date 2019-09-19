package com.lwc.common.module.message.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.HasMsg;
import com.lwc.common.module.bean.MyMsg;
import com.lwc.common.module.common_adapter.MyMsgListAdapter;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.ToastUtil;

import org.byteam.superadapter.OnItemClickListener;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


/**
 * 我的消息
 *
 * @date 2018-03-27
 */
public class MyMsgActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    private ArrayList<MyMsg> myMsgs = new ArrayList<>();
    private List<HasMsg> hasMsg = new ArrayList<>();
    private MyMsgListAdapter adapter;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_my_msg_list;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        BGARefreshLayoutUtils.initRefreshLayout(this, mBGARefreshLayout);
        setTitle("我的消息");
        hasMsg = DataSupport.findAll(HasMsg.class);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initGetData() {
    }

    @Override
    protected void widgetListener() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyMsgListAdapter(this, myMsgs,hasMsg, R.layout.item_my_msg);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                MyMsg msg = adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("myMsg", msg);
             /*   for (int i=0; i<hasMsg.size(); i++) {
                    if (hasMsg.get(i).getType().equals(msg.getMessageType())) {
                        if (hasMsg.get(i).isHasMessage()) {
                            hasMsg.get(i).setHasMessage(false);
                            //readMsg(msg.getMessageType());
                        }
                        break;
                    }
                }*/
                IntentUtil.gotoActivity(MyMsgActivity.this, MsgListActivity.class, bundle);
            }
        });
        recyclerView.setAdapter(adapter);

        //刷新控件监听器
        mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                getMsgList();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                return false;
            }
        });
    }

/*    private void readMsg(String type) {
        HttpRequestUtils.httpRequest(this, "readMsg", RequestValue.READ_MESSAGE+type, null, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String result) {
            }
            @Override
            public void returnException(Exception e, String msg) {
            }
        });
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        getMsgList();
    }

    private void getMsgList() {
        HttpRequestUtils.httpRequest(this, "getMyMsgList", RequestValue.GET_MY_MESSAGE_LIST, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String result) {
                Common common = JsonUtil.parserGsonToObject(result, Common.class);
                if (common.getStatus().equals("1")) {
                    ArrayList<MyMsg> current = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(result, "data"), new TypeToken<ArrayList<MyMsg>>() {
                    });
                    if (current != null && current.size() > 0) {
                        myMsgs = current;
                    } else {
                        myMsgs = new ArrayList<>();
                    }
                   /* if (hasMsg != null && hasMsg.size() > 0 && myMsgs != null && myMsgs.size() > 0){
                        for (int j=0; j<myMsgs.size(); j++) {
                            for (int i=0; i<hasMsg.size(); i++) {
                                if (myMsgs.get(j).getMessageType().equals(hasMsg.get(i).getType())){
                                    myMsgs.get(j).setHasMessage(hasMsg.get(i).isHasMessage());
                                    break;
                                }
                            }
                        }
                    }*/
                    adapter.replaceAll(myMsgs);

                    hasMessage();

                    if (myMsgs.size() > 0) {
                        tv_msg.setVisibility(View.GONE);
                    } else {
                        tv_msg.setVisibility(View.VISIBLE);
                    }
                } else {
                    ToastUtil.showToast(MyMsgActivity.this, common.getInfo());
                }
                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showToast(MyMsgActivity.this, msg);
                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
            }
        });
    }

    public void hasMessage() {
        DataSupport.deleteAll(HasMsg.class);
        HttpRequestUtils.httpRequest(this, "hasMessage", RequestValue.HAS_MESSAGE, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if (common.getStatus().equals("1")) {
                    ArrayList<HasMsg> current = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<HasMsg>>() {
                    });
                    if (current != null && current.size() > 0) {
                        DataSupport.saveAll(current);
                        hasMsg.clear();
                        hasMsg.addAll(current);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
            }
        });
    }
}
