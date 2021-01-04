package com.lwc.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.lease_parts.bean.LeaseSpecsBean;
import com.lwc.common.utils.DisplayUtil;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义对话框
 * 
 */
public class SelectGoodTypeDialog extends Dialog implements View.OnClickListener{

	/** 上下文 */
	private Context context;
	CallBack callBack;
	private List<LeaseSpecsBean> specsBeanList;
	private String money;
	private String img;
	private LinearLayout ll_space;

	private List<TextView> spaceTextViews = new ArrayList<>();
	private List<TextView> spaceTimeTextViews = new ArrayList<>();
	private List<TextView> payTypeTextViews = new ArrayList<>();

	//提交的数据
	private int sum = 1;
	private LeaseSpecsBean selSpecsBean;
	private Integer selLeaseTime;
	private Integer selLeaseType;
	private ImageView iv_header;
	private TextView tv_price;
	private EditText tv_time06;

	private TextView tv_payType01;
	private TextView tv_payType02;

	private String presentGoodsId;

	public SelectGoodTypeDialog(Context context, CallBack callBack, List<LeaseSpecsBean> specsBeanList, String money, String img,String presentGoodsId) {
		super(context, R.style.BottomDialogStyle);
		// 拿到Dialog的Window, 修改Window的属性
		Window window = getWindow();
		window.getDecorView().setPadding(0, 0, 0, 0);
		// 获取Window的LayoutParams
		WindowManager.LayoutParams attributes = window.getAttributes();
		attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
		attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		// 一定要重新设置, 才能生效
		window.setAttributes(attributes);
		this.callBack = callBack;
		this.specsBeanList = specsBeanList;
		this.money = money;
		this.img = img;
		this.presentGoodsId = presentGoodsId;
		init(context);
	}

	/**
	 *
	 * @param context
	 * @param theme
     */
	public SelectGoodTypeDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public SelectGoodTypeDialog(Context context) {
		super(context, R.style.dialog_style);
		init(context);
	}

	/**
	 * 初始话对话框
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param context
	 * 
	 */
	protected void init(final Context context) {
		this.context = context;
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
		this.setContentView(R.layout.dialog_select_goods_type_layout);

		ImageView ic_close = findViewById(R.id.ic_close);
		iv_header = findViewById(R.id.iv_header);
		TextView tv_submit = findViewById(R.id.tv_submit);
		tv_price = findViewById(R.id.tv_price);
		ImageView tv_reduce = findViewById(R.id.tv_reduce);
		ImageView tv_add = findViewById(R.id.tv_add);
		final EditText et_sum = findViewById(R.id.et_sum);
		ll_space = findViewById(R.id.ll_space);
		tv_time06 = findViewById(R.id.tv_time06);

		tv_payType01 = findViewById(R.id.tv_payType01);
		tv_payType02 = findViewById(R.id.tv_payType02);

		//默认选中第一个规格
		//selSpecsBean = specsBeanList.get(0);

		spaceTextViews.clear();
		spaceTimeTextViews.clear();
		payTypeTextViews.clear();

		showGoodSpace();
		showLeaseTime();
		showPayType();

		ImageLoaderUtil.getInstance().displayFromNetDCircular(context, img, iv_header,R.drawable.image_default_picture);// 使用ImageLoader对图片进行加装！

		String goodsPrice = Utils.chu(money, "100");
		String goodsPriceStr = "￥" + goodsPrice + "/月";
		SpannableStringBuilder showPrices = Utils.getSpannableStringBuilder(1, goodsPrice.length()+1,context.getResources().getColor(R.color.red_money), goodsPriceStr, 24, true);
		tv_price.setText(showPrices);

		ic_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		et_sum.setText(String.valueOf(sum));
		et_sum.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(TextUtils.isEmpty(s.toString()) || Integer.parseInt(s.toString()) < 1 || Integer.parseInt(s.toString()) > 200){
					ToastUtil.showToast(context,"请输入1-200件商品");
					et_sum.setText("1");
					return;
				}

				sum = Integer.parseInt(s.toString());
			}
		});
		tv_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(callBack != null){
					if(selSpecsBean == null){
						ToastUtil.showToast(context,"请选择规格!");
						return;
					}
					if(selLeaseTime == null){
						ToastUtil.showToast(context,"请选择租用时间!");
						return;
					}
					if(selLeaseType == null){
						ToastUtil.showToast(context,"请选择付款类型!");
						return;
					}

					Map<String,String> params = new HashMap<>();
					params.put("goods_id",selSpecsBean.getGoodsId());
					params.put("pay_type",String.valueOf(selLeaseType));
					params.put("lease_mon_time",String.valueOf(selLeaseTime));
					params.put("goods_num",String.valueOf(sum));
					params.put("leaseSpace",selSpecsBean.getLeaseSpecs());
					params.put("goodsName",selSpecsBean.getGoodsName());
					params.put("goodsPrice",String.valueOf(selSpecsBean.getGoodsPrice()));

					callBack.onSubmit(params);
				}
			}
		});

		tv_reduce.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int reduceSum = sum - 1;
				if(reduceSum == 0){
					ToastUtil.showToast(context,"至少选择一件商品！");
					return;
				}

				sum = reduceSum;
				et_sum.setText(String.valueOf(sum));
			}
		});

		tv_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sum = sum + 1;
				et_sum.setText(String.valueOf(sum));
			}
		});

		tv_time06.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				if(!TextUtils.isEmpty(s)){
					Integer leaseData = Integer.parseInt(s.toString());
					if(1 > leaseData){
						ToastUtil.showToast(context,"租期至少为1个月");
						tv_time06.setText("1");
						selLeaseTime = 1;
					}
					selLeaseTime = leaseData;
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		tv_time06.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					tv_time06.setHint("请输入要租用的月份");
					showInput(tv_time06);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_time01:
			case R.id.tv_time02:
			case R.id.tv_time03:
			case R.id.tv_time04:
			case R.id.tv_time05:
			case R.id.tv_time06:
				for(int j = 0;j<spaceTimeTextViews.size();j++){
					if(v == spaceTimeTextViews.get(j)){
						spaceTimeTextViews.get(j).setBackgroundResource(R.drawable.button_red_round_shape);
						tv_time06.setText("");
						switch (j){
							case 0:
								selLeaseTime = 1;
								tv_payType02.setEnabled(false);
								tv_payType02.setTextColor(getContext().getResources().getColor(R.color.gray_99));

								tv_payType02.setBackgroundResource(R.drawable.button_gray_round_shape_f0);
								tv_payType01.setBackgroundResource(R.drawable.button_red_round_shape);
								selLeaseType = 1;
								break;
							case 1:
								selLeaseTime = 3;
								tv_payType02.setEnabled(true);
								tv_payType02.setTextColor(getContext().getResources().getColor(R.color.black));
								break;
							case 2:
								selLeaseTime = 6;
								tv_payType02.setEnabled(true);
								tv_payType02.setTextColor(getContext().getResources().getColor(R.color.black));
								break;
							case 3:
								selLeaseTime = 9;
								tv_payType02.setEnabled(true);
								tv_payType02.setTextColor(getContext().getResources().getColor(R.color.black));
								break;
							case 4:
								selLeaseTime = 12;
								tv_payType02.setEnabled(true);
								tv_payType02.setTextColor(getContext().getResources().getColor(R.color.black));
								break;
							case 5:
								selLeaseTime = null;
								tv_payType02.setEnabled(true);
								tv_payType02.setTextColor(getContext().getResources().getColor(R.color.black));
								break;
						}
					}else{
						spaceTimeTextViews.get(j).setBackgroundResource(R.drawable.button_gray_round_shape_f0);
					}
				}
				break;
			case R.id.tv_payType01:
			case R.id.tv_payType02:
				for(int j = 0;j<payTypeTextViews.size();j++){
					if(v == payTypeTextViews.get(j)){
						payTypeTextViews.get(j).setBackgroundResource(R.drawable.button_red_round_shape);
						selLeaseType = j+1;
					}else{
						payTypeTextViews.get(j).setBackgroundResource(R.drawable.button_gray_round_shape_f0);
					}
				}
				break;
		}
	}

	public void showInput(final EditText et) {
		et.requestFocus();
		InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
	}

	protected void hideInput() {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
		View v = getWindow().peekDecorView();
		if (null != v) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	public interface CallBack {
		void onSubmit(Object o);
	}


	private void showGoodSpace(){
		if(specsBeanList != null){
			for(int i =0;i<specsBeanList.size();i++){

				LeaseSpecsBean leaseSpecsBean = specsBeanList.get(i);

				TextView textView = new TextView(context);
				textView.setTextColor(context.getResources().getColor(R.color.black));
				textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
				textView.setTag(specsBeanList.get(i));
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				params.setMargins(0,DisplayUtil.dip2px(context,10),0,0);
				textView.setPadding(DisplayUtil.dip2px(context,10),DisplayUtil.dip2px(context,5),DisplayUtil.dip2px(context,10),DisplayUtil.dip2px(context,5));

				if(presentGoodsId.equals(leaseSpecsBean.getGoodsId())){
					selSpecsBean = leaseSpecsBean;
					textView.setBackgroundResource(R.drawable.button_red_round_shape);
				}else{
					textView.setBackgroundResource(R.drawable.button_gray_round_shape_f0);
				}

				textView.setText(leaseSpecsBean.getLeaseSpecs());
				textView.setLayoutParams(params);

				spaceTextViews.add(textView);

				ll_space.addView(textView);

				textView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						for(int j = 0;j<spaceTextViews.size();j++){
							if(v == spaceTextViews.get(j)){
								spaceTextViews.get(j).setBackgroundResource(R.drawable.button_red_round_shape);
								selSpecsBean = specsBeanList.get(j);
							}else{
								spaceTextViews.get(j).setBackgroundResource(R.drawable.button_gray_round_shape_f0);
							}
						}

						LeaseSpecsBean obj = (LeaseSpecsBean) v.getTag();
						//ImageLoaderUtil.getInstance().displayFromNetDCircular(context, img, iv_header,R.drawable.image_default_picture);// 使用ImageLoader对图片进行加装！

						String goodsPrice = Utils.chu(String.valueOf(obj.getGoodsPrice()), "100");
						String goodsPriceStr = "￥" + goodsPrice + "/月";
						SpannableStringBuilder showPrices = Utils.getSpannableStringBuilder(1, goodsPrice.length()+1,context.getResources().getColor(R.color.red_money), goodsPriceStr, 24, true);
						tv_price.setText(showPrices);
					}
				});
			}
		}
	}

	private void showLeaseTime() {

		TextView tv_time01 = findViewById(R.id.tv_time01);
		TextView tv_time02 = findViewById(R.id.tv_time02);
		TextView tv_time03 = findViewById(R.id.tv_time03);
		TextView tv_time04 = findViewById(R.id.tv_time04);
		TextView tv_time05 = findViewById(R.id.tv_time05);

		tv_time01.setOnClickListener(this);
		tv_time02.setOnClickListener(this);
		tv_time03.setOnClickListener(this);
		tv_time04.setOnClickListener(this);
		tv_time05.setOnClickListener(this);
		tv_time06.setOnClickListener(this);

		spaceTimeTextViews.add(tv_time01);
		spaceTimeTextViews.add(tv_time02);
		spaceTimeTextViews.add(tv_time03);
		spaceTimeTextViews.add(tv_time04);
		spaceTimeTextViews.add(tv_time05);
		spaceTimeTextViews.add(tv_time06);

	}

	private void showPayType() {

		TextView tv_payType01 = findViewById(R.id.tv_payType01);
		TextView tv_payType02 = findViewById(R.id.tv_payType02);

		tv_payType01.setOnClickListener(this);
		tv_payType02.setOnClickListener(this);


		payTypeTextViews.add(tv_payType01);
		payTypeTextViews.add(tv_payType02);

	}
}
