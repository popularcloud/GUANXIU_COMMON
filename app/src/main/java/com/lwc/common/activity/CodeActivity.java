package com.lwc.common.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lwc.common.R;
import com.lwc.common.bean.MachineBean;
import com.lwc.common.utils.AsyncImageloader;
import com.lwc.common.utils.AsyncImageloader.ImageCallback;
import com.lwc.common.utils.DisplayUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.view.TitleView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 二维码展示
 * 
 * @Description TODO
 * @author 何栋
 * @date 2016年1月9日
 * @Copyright: lwc
 */
public class CodeActivity extends BaseActivity {
	private TitleView view_title;
	private RelativeLayout rl_content;
	private ImageView img;
	/** 添加设备 */
	private TextView txt_right;
	private int width;
	private int hieght;
	private TextView txt_name;
	private TextView txt_type;

	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_code;
	}

	@Override
	protected void findViews() {


		view_title = (TitleView) findViewById(R.id.view_title);
		view_title.setActivityFinish(this);
		view_title.setTitle("二维码");
		txt_right = view_title.getTxt_right();
		txt_right.setText("保存");
		rl_content = (RelativeLayout) findViewById(R.id.rl_content);
		img = (ImageView) findViewById(R.id.img_code);
		txt_name = (TextView) findViewById(R.id.txt_name);
		txt_type = (TextView) findViewById(R.id.txt_type);
		LayoutParams layoutParams = img.getLayoutParams();
		layoutParams.width = DisplayUtil.getScreenWidth(this) - DisplayUtil.dip2px(this, 120);
		layoutParams.height = DisplayUtil.getScreenWidth(this) - DisplayUtil.dip2px(this, 120);
		img.setLayoutParams(layoutParams);

		ViewTreeObserver vto = rl_content.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				width = rl_content.getMeasuredWidth();
				hieght = rl_content.getMeasuredHeight();
				return true;
			}
		});
	}

	@Override
	protected void init() {
	}

	@Override
	protected void initGetData() {
		Bundle bundle = getIntent().getExtras();
		MachineBean t = (MachineBean) bundle.getSerializable(getString(R.string.intent_key_path));
		if (!TextUtils.isEmpty(t.getQrcode())) {
			new AsyncImageloader(1, R.drawable.code_error, 0, 0).loadImageBitmap(this, 0, t.getQrcode(), true, new ImageCallback() {

				@Override
				public void imageLoaded(int position, Bitmap bitmap, String imagePath) {
					if (bitmap != null) {
						img.setImageBitmap(bitmap);
					}
				}
			});
		} else {
			ToastUtil.showToast(CodeActivity.this, "二维码获取失败！");
			finish();
		}
		txt_name.setText("名称：" + t.getDname());
		txt_type.setText("类型：" + t.getDtype_name());
	}

	@Override
	protected void widgetListener() {
		txt_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				savePic();
			}
		});
	}

	protected void savePic() {
		if (width > 0) {
			Bitmap loadBitmapFromView = loadBitmapFromView(rl_content);
			if (loadBitmapFromView != null) {
				saveImageToGallery(CodeActivity.this, loadBitmapFromView);
			} else {
				ToastUtil.showToast(CodeActivity.this, "图片保存失败！");
			}
		}
	}

	public void saveImageToGallery(Context context, Bitmap bmp) {
		// 首先保存图片
		File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 其次把文件插入到系统图库
		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 最后通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
		ToastUtil.showToast(CodeActivity.this, "图片保存成功！");
	}

	public static Bitmap loadBitmapFromView(View v) {
		if (v == null) {
			return null;
		}
		Bitmap screenshot;
		screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
		Canvas c = new Canvas(screenshot);
		c.translate(-v.getScrollX(), -v.getScrollY());
		v.draw(c);
		return screenshot;
	}
}
