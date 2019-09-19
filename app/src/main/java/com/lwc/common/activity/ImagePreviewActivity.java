package com.lwc.common.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.lwc.common.R;
import com.lwc.common.configs.FileConfig;
import com.lwc.common.configs.ServerConfig;
import com.lwc.common.utils.AsyncImageloader;
import com.lwc.common.utils.AsyncImageloader.ImageCallback;
import com.lwc.common.utils.ExecutorServiceUtil;
import com.lwc.common.utils.HandlerUtil;
import com.lwc.common.utils.ImageUtil;
import com.lwc.common.utils.ProgressDialogUtil;
import com.lwc.common.view.TitleView;

/**
 * 图片预览界面
 * 
 * @Description 图片压缩预览
 * @author 何栋
 * @version 1.0
 * @date 2013-11-6
 * @Copyright: Copyright (c) 2013 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 * 
 */
public class ImagePreviewActivity extends BaseActivity implements OnGestureListener, OnTouchListener {

	/** 初始显示图片对象 */
	private ImageView imageView;
	/** 位图对象 */
	private Bitmap bitmap;
	/** 手势监听对象 */
	private GestureDetector mGestureDetector;

	/** 用于缩放的矩阵 */
	private Matrix matrix;
	/** 缩放之后保存的矩阵状态 */
	private Matrix savedMatrix;

	/** 文本输入框 */
	private EditText edit_Message;

	// 手势缩放时候图片的状态
	/** 开始缩放 */
	private static final int NONE = 0;
	/** 滑动查看 */
	private static final int DRAG = 1;
	/** 双指缩放模式 */
	private static final int ZOOM = 2;
	/** 双击模式 */
	private static final int MOVE = 3;

	/** 图片当前模式 */
	private int mode = NONE;

	/** 在图片区域内第一个被按下去的点 */
	private final PointF start = new PointF();
	/** 两指之间的中点 */
	private final PointF mid = new PointF();

	/** 初始位移，处理图片移动查看 */
	private float oldDist = 1f;
	/** 新的位移，处理图片移动查看 */
	// private float newDist = 0;

	/** 图片的最小缩放比例 */
	private float minScaleR;
	/** 图片的最大缩放比例 */
	private static final float MAX_SCALE = 4f;//

	/** 图片是否处于放大模式，用于处理双击放大事件 */
	public boolean isZOOM = false;

	/** 图片显示区域 */
	private RelativeLayout view_Screen;
	/** 标题栏 */
	private TitleView view_Title;

	/** 图片显示区域视图是否加载完毕 */
	private boolean hasMeasured = false;

	/** 取消按钮 */
	private Button btn_Cancel;
	/** 确定按钮 */
	private Button btn_Enter;

	/** 显示图片区域高度 */
	private int screenHeight = 0;
	/** 显示图片区域宽度 */
	private int screenWidth = 0;

	/** 原图路径 */
	private String imageSrcPath;
	/** 图片名称 */
	private String imageName;
	/** 是否添加备注 */
	private boolean isRemark;

	/**
	 * 计算两点之间的距离
	 * 
	 * @version 1.0
	 * @createTime 2013-11-6,上午11:38:43
	 * @updateTime 2013-11-6,上午11:38:43
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param event
	 * @return
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * 计算两指之间的中点坐标
	 * 
	 * @version 1.0
	 * @createTime 2013-11-6,上午11:40:24
	 * @updateTime 2013-11-6,上午11:40:24
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param point
	 * @param event
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/**
	 * 初始化矩阵视图
	 * 
	 * @version 1.0
	 * @createTime 2013-11-6,上午11:40:29
	 * @updateTime 2013-11-6,上午11:40:29
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	private void InitMatrix() {

		matrix = new Matrix();
		savedMatrix = new Matrix();
		matrix.set(imageView.getImageMatrix());
		minZoom();
		center();
		imageView.setImageMatrix(matrix);
		matrix.setScale(minScaleR, minScaleR);
		CheckView();
	}

	/**
	 * 限制最大最小缩放比例，自动居中
	 */
	private void CheckView() {
		float p[] = new float[9];
		matrix.getValues(p);
		if (mode == ZOOM) {
			if (p[0] < minScaleR) {
				matrix.setScale(minScaleR, minScaleR);
				mode = MOVE;
			}
			if (p[0] > MAX_SCALE) {
				matrix.setScale(MAX_SCALE, MAX_SCALE);
			}
		}
		center();
	}

	/**
	 * 最小缩放比例，最大为100%
	 * 
	 * @version 1.0
	 * @createTime 2013-11-6,上午11:37:35
	 * @updateTime 2013-11-6,上午11:37:35
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	private void minZoom() {
		minScaleR = Math.min((float) screenWidth / (float) bitmap.getWidth(), (float) screenHeight / (float) bitmap.getHeight());
		matrix.setScale(minScaleR, minScaleR);
	}

	/**
	 * 图片居中显示
	 * 
	 * @version 1.0
	 * @createTime 2013-11-6,上午11:31:30
	 * @updateTime 2013-11-6,上午11:31:30
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	private void center() {
		center(true, true);
	}

	/**
	 * 横向、纵向居中
	 * 
	 * @version 1.0
	 * @createTime 2013-11-6,上午11:33:38
	 * @updateTime 2013-11-6,上午11:33:38
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param horizontal
	 *        是否横向居中
	 * @param vertical
	 *        是否垂直居中
	 */
	private void center(boolean horizontal, boolean vertical) {

		Matrix m = new Matrix();
		m.set(matrix);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {

			if (height < screenHeight) {
				deltaY = (screenHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < screenHeight) {
				deltaY = imageView.getHeight() - rect.bottom;
			}
		}

		if (horizontal) {
			if (width < screenWidth) {
				deltaX = (screenWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < screenWidth) {
				deltaX = screenWidth - rect.right;
			}
		}
		matrix.postTranslate(deltaX, deltaY);

	}

	/**
	 * 获取图形矩形区域
	 * 
	 * @version 1.0
	 * @createTime 2013-11-6,上午11:34:30
	 * @updateTime 2013-11-6,上午11:34:30
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @return
	 */
	private RectF getRectF() {
		Matrix m = new Matrix();
		m.set(matrix);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		m.mapRect(rect);
		return rect;
	}

	/**
	 * touch 事件处理
	 * 
	 * @version 1.0
	 * @createTime 2013-11-6,上午11:34:56
	 * @updateTime 2013-11-6,上午11:34:56
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		imageView = (ImageView) v;
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:// 第一只手指按下屏幕
			matrix.set(imageView.getImageMatrix());
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			if ((int) getRectF().width() <= screenWidth && (int) getRectF().height() <= screenHeight) {
				mode = MOVE;
				isZOOM = false;
			} else {
				mode = DRAG;
			}

			break;
		case MotionEvent.ACTION_POINTER_DOWN: // 第二只手指按下屏幕
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				isZOOM = true;
			}
			break;
		case MotionEvent.ACTION_UP:// 第一只手指离开屏幕

			break;
		case MotionEvent.ACTION_POINTER_UP:// 第二只手指离开屏幕
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:// 手指在屏幕中移动
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
				center();
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			} else if (mode == MOVE) {
				return this.mGestureDetector.onTouchEvent(event);
			}
			break;
		}
		imageView.setImageMatrix(matrix);
		CheckView();
		// 将事件继续传递给手势监听处理
		return this.mGestureDetector.onTouchEvent(event);
	}

	/**
	 * 双击事件监听
	 * 
	 * @Description 监听屏幕双击事件
	 * @author 何栋
	 * @version 1.0
	 * @date 2013-11-6
	 * @Copyright: Copyright (c) 2013 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
	 * 
	 */
	private class OnDoubleTapListener implements GestureDetector.OnDoubleTapListener {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {

			return false;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {

			if (isZOOM) {
				matrix.setScale(minScaleR, minScaleR);
				isZOOM = false;
			} else {
				matrix.setScale(MAX_SCALE, MAX_SCALE);
				isZOOM = true;
			}
			imageView.setImageMatrix(matrix);
			CheckView();
			savedMatrix.set(matrix);
			return false;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {

			return false;
		}

	}

	@Override
	public boolean onDown(MotionEvent e) {

		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {

		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

		return false;
	}

	/**
	 * 异步处理handler对象
	 */
	private final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ServerConfig.WHAT_COMPRESS_IMAGE_RESPONSE:
				ProgressDialogUtil.dismissDialog();
				// 获取到宽度和高度后，可用于计算
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
					InitMatrix();
				} else {// 生成缩略图失败
						// TODO
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 发送图片
	 * 
	 * @version 1.0
	 * @createTime 2013-11-6,下午10:55:07
	 * @updateTime 2013-11-6,下午10:55:07
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	private void sendImage() {

		// 发送图片返回原图路径和缩略图路径
		Intent intent = new Intent();
		intent.putExtra(getString(R.string.intent_key_image), FileConfig.PATH_USER_IMAGE + imageName);
		intent.putExtra(getString(R.string.intent_key_thumbnail), FileConfig.PATH_USER_THUMBNAIL + imageName);
		intent.putExtra(getString(R.string.intent_key_content), edit_Message.getText().toString().trim());
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_image_preview;
	}

	@Override
	protected void findViews() {
		mGestureDetector = new GestureDetector(this);
		mGestureDetector.setOnDoubleTapListener(new OnDoubleTapListener());

		view_Screen = (RelativeLayout) findViewById(R.id.view_img_screen);
		view_Title = (TitleView) findViewById(R.id.view_title);

		edit_Message = (EditText) findViewById(R.id.edit_message);
		if (isRemark) {
			edit_Message.setVisibility(View.VISIBLE);
		} else {
			edit_Message.setVisibility(View.GONE);
		}

		btn_Cancel = (Button) findViewById(R.id.btn_cancel);
		btn_Enter = (Button) findViewById(R.id.btn_enter);

		imageView = (ImageView) findViewById(R.id.img_zoom);
		imageView.setImageBitmap(bitmap);

		imageView.setOnTouchListener(this); //
		imageView.setScaleType(ScaleType.MATRIX);
		widgetListener();

		ProgressDialogUtil.showDialog(this, getString(R.string.process_loading_wait));

		getScreenSize();
	}

	@Override
	protected void init() {

	}

	@Override
	protected void initGetData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		imageSrcPath = bundle.getString(getString(R.string.intent_key_image_path));
		imageName = bundle.getString(getString(R.string.intent_key_image_name));
		isRemark = bundle.getBoolean(getString(R.string.intent_key_isremark), false);

	}

	@Override
	protected void widgetListener() {
		// 按钮监听
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.btn_cancel:
					finish();
					break;
				case R.id.btn_enter:
					sendImage();
					break;
				}

			}
		};
		// 取消按钮
		btn_Cancel.setOnClickListener(listener);
		// 确定按钮
		btn_Enter.setOnClickListener(listener);

		// 返回按钮
		view_Title.setActivityFinish(this);
		// // 发送原图
		// view_Title.setRightButton(getString(R.string.activity_image_preview_send_src),
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// }
		// });

	}

	/**
	 * 获取显示区域大小
	 * 
	 * @version 1.0
	 * @createTime 2013-11-6,下午3:11:57
	 * @updateTime 2013-11-6,下午3:11:57
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	private void getScreenSize() {

		ViewTreeObserver vto = view_Screen.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {

				if (!hasMeasured) {
					screenHeight = view_Screen.getMeasuredHeight();
					screenWidth = view_Screen.getMeasuredWidth();

					getBitmap();

					hasMeasured = true;
				}
				return true;
			}
		});
	}

	/**
	 * 获取位图
	 * 
	 * @version 1.0
	 * @createTime 2013-11-6,下午3:12:53
	 * @updateTime 2013-11-6,下午3:12:53
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	private void getBitmap() {

		if (TextUtils.isEmpty(imageSrcPath)) {// 文件路径为空
			return;
		}

		if (imageSrcPath.contains("http")) {
			new AsyncImageloader(1, R.drawable.default_portrait_100, 0, 0).loadImageBitmap(this, 0, imageSrcPath, true, new ImageCallback() {

				@Override
				public void imageLoaded(int position, Bitmap bitmap, String imagePath) {
					if (bitmap != null) {
						ImagePreviewActivity.this.bitmap = bitmap;
						HandlerUtil.sendMessage(handler, ServerConfig.WHAT_COMPRESS_IMAGE_RESPONSE);
					}
				}
			});
			return;
		}
		Runnable runnable = new Runnable() {

			@Override
			public void run() {

				String pathName = new ImageUtil().compressImage(imageSrcPath, imageName);
				bitmap = BitmapFactory.decodeFile(pathName);
				HandlerUtil.sendMessage(handler, ServerConfig.WHAT_COMPRESS_IMAGE_RESPONSE);
			}
		};
		ExecutorServiceUtil.getInstance().execute(runnable);
	}

	@Override
	protected void onResume() {

		super.onResume();
		// InitMatrix();
	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	@Override
	protected void onPause() {
		if (bitmap != null) {
			bitmap.isRecycled();
		}
		super.onPause();
	}

}
