package com.lwc.common.module.order.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.module.integral.activity.IntegralMainActivity;
import com.lwc.common.module.repairs.ui.activity.ApplyForMaintainActivity;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 套餐详情
 * 
 * @author 何栋
 * @date 2018年8月21日
 */
public class PackageDetailActivity extends BaseActivity {

	@BindView(R.id.tv_count)
	TextView tvCount;
	@BindView(R.id.tv_time)
	TextView tvTime;
	@BindView(R.id.tv_titel)
	TextView tvTitel;
	@BindView(R.id.tv_desc)
	TextView tvDesc;
	@BindView(R.id.tv_district)
	TextView tvDistrict;
	@BindView(R.id.tv_money)
	TextView tv_money;
	@BindView(R.id.btn_gm)
	Button btnGm;
	private PackageBean pack;

	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_package_detail;
	}

	@Override
	protected void findViews() {
		ButterKnife.bind(this);
		setTitle("套餐详情");
	}

	public void onBack(View v){
		onBackPressed();
	}

	@Override
	protected void init() {
		pack = (PackageBean)getIntent().getSerializableExtra("package");
		int type = getIntent().getIntExtra("type", 0);

		tvDesc.setText(pack.getRemark());
		String count;
		if (pack.getRemissionCount() == 0) {
			count = "无限";
		} else {
			count = pack.getRemissionCount()+"";
		}
		tvCount.setText("减免上门费："+count+"次");
		tvTitel.setText(pack.getPackageName());
		if (pack.getPackageType() == 1) {
			tvCount.setText("减免上门费："+count+"次");
		} else if (pack.getPackageType() == 2) {
			tvCount.setText("减免维修费："+count+"次");
		} else if (pack.getPackageType() == 3) {
			tvCount.setText("减免上门维修费："+count+"次");
		}
		tvDistrict.setText("减免地区："+pack.getTownNames());
		if (type != 1) {
			tvTime.setText("有效期："+pack.getValidDay()+"天（购买后显示日期）");
//			btnGm.setVisibility(View.VISIBLE);
			tv_money.setVisibility(View.VISIBLE);

			String content="¥ "+Utils.getMoney(pack.getPackagePrice());
			SpannableStringBuilder stringBuilder=new SpannableStringBuilder(content);
			AbsoluteSizeSpan ab=new AbsoluteSizeSpan(Utils.dip2px(this,6),true);
			//文本字体绝对的大小
			stringBuilder.setSpan(ab,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			tv_money.setText(stringBuilder);

			btnGm.setText("立即购买");
			btnGm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					//判断是否为机关用户(套餐仅个人用户可使用)
					if("3".equals(SharedPreferencesUtils.getInstance(PackageDetailActivity.this).loadString("user_role"))){
						ToastUtil.showToast(PackageDetailActivity.this,"套餐仅限个人用户使用");
						return;
					}

					Bundle bundle2 = new Bundle();
					bundle2.putString("packageId", pack.getPackageId());
					bundle2.putString("money", Utils.cheng(pack.getPackagePrice(), "100"));
					IntentUtil.gotoActivityForResult(PackageDetailActivity.this, PayActivity.class, bundle2, 1602);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				}
			});
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
			SimpleDateFormat sdf2 = new SimpleDateFormat( "yyyy.MM.dd" );
			try {
				Date date = sdf.parse(pack.getCreateTime());
				tvTime.setText("有效时间："+sdf2.format(date)+" - "+ sdf2.format(sdf.parse(pack.getExpirationTime())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			btnGm.setText("立即使用");
			tv_money.setVisibility(View.INVISIBLE);
			btnGm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					IntentUtil.gotoActivityForResult(PackageDetailActivity.this, ApplyForMaintainActivity.class, 1520);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if(requestCode == 1602){
				String getIntegral = (String) SharedPreferencesUtils.getParam(PackageDetailActivity.this,"get_integral","0");
				DialogUtil.dialog(PackageDetailActivity.this, "温馨提示", "前往兑换", "下次再说", "支付成功,本次获得" + Utils.chu(getIntegral,"100") + "积分,可前往积分商城兑换礼品",
						new View.OnClickListener() { // sure
							@Override
							public void onClick(View v) {
								IntentUtil.gotoActivity(PackageDetailActivity.this, IntegralMainActivity.class);
							}
						}, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								onBackPressed();
							}
						}, true);
			}else{
				onBackPressed();
			}

		}
	}

	@Override
	protected void initGetData() {
	}

	@Override
	protected void widgetListener() {
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
