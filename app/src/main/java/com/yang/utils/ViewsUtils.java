package com.yang.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.*;
import android.text.method.LinkMovementMethod;
import android.text.style.*;
import android.util.Log;
import android.view.*;
import android.view.View.OnTouchListener;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.yang.App;

/**
 * View操作工具类
 * 
 *
 */
public class ViewsUtils {

	private static final String TAG = ViewsUtils.class.getName();

	private static int COMPOUND_DRAWABLE_PADDING = 5; // drawable和文字的距离

	/**
	 * 设置Hint在获得焦点时隐藏
	 * 
	 * @param view
	 *            需要设置的View
	 */
	public static void setHintHidden(TextView view) {
		view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				TextView tv = (TextView) v;
				if (!hasFocus) {
					tv.setHint(tv.getTag().toString());
				} else {
					String hint = tv.getHint().toString();
					tv.setTag(hint);
					tv.setHint("");
				}
			}
		});
	}

	/**
	 * 设置部分高亮文字
	 * 
	 * @param view
	 *            需要设置的View
	 * @param text
	 *            设置的文字内容
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @param color
	 *            高亮文字的颜色
	 */
	public static void setSpan(TextView view, String text, int start, int end,
			int color) {
		if (start < 0 || start > text.length() || end < 0
				|| end > text.length() || start > end) {
			return;
		}
		SpannableStringBuilder styleStr = new SpannableStringBuilder(text);
		styleStr.setSpan(new ForegroundColorSpan(color), start, end,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		view.setText(styleStr);
	}

	/**
	 * 设置部分高亮文字
	 * 
	 * @param context
	 * @param view
	 *            需要设置的View
	 * @param text
	 *            设置的文字内容
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @param colorResId
	 *            高亮文字的颜色资源ID
	 */
	public static void setSpan(Context context, TextView view, String text,
			int start, int end, int colorResId) {
		if (start < 0 || start > text.length() || end < 0
				|| end > text.length() || start > end) {
			return;
		}
		SpannableStringBuilder styleStr = new SpannableStringBuilder(text);
		styleStr.setSpan(new ForegroundColorSpan(context.getResources()
				.getColor(colorResId)), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(styleStr);
	}

	/**
	 * 设置走马灯效果
	 * 
	 * @param view
	 */
	public static void setMarquee(TextView view) {
		view.setSingleLine(true);
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		view.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		view.setMarqueeRepeatLimit(-1);
	}

	/**
	 * 设置下划线链接 eg. "tel:4155551212" "mailto:webmaster@google.com"
	 * "http://www.baidu.com" "sms:4155551212" "mms:4155551212"
	 * "geo:38.899533,-77.036476"
	 * 
	 * @param view
	 *            需要设置的View
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @param url
	 *            链接地址
	 * @param isLink
	 *            是否超级链接，点击后是否触发事件
	 */
	public static void setURL(TextView view, int start, int end, String url,
			boolean isLink) {
		setURL(view, start, end, url, isLink, Color.BLACK);
	}

	public static void setURL(TextView view, int start, int end, String url,
			boolean isLink, int color) {
		CharSequence text = view.getText();
		if (!isValid(text, start, end)) {
			return;
		}
		SpannableStringBuilder ssbText = new SpannableStringBuilder(text);
		ssbText.setSpan(new URLSpan(url), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		if (color != -1) {
			ssbText.setSpan(new ForegroundColorSpan(color), start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		view.setText(ssbText);
		if (isLink) {
			view.setMovementMethod(LinkMovementMethod.getInstance());
		}
	}

	public static void setURL(TextView view, int start, int end,
			ClickableSpan span) {
		CharSequence text = view.getText();
		if (!isValid(text, start, end)) {
			return;
		}
		SpannableStringBuilder ssbText = new SpannableStringBuilder(text);
		ssbText.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssbText.setSpan(new ForegroundColorSpan(Color.BLACK), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(ssbText);
		view.setMovementMethod(LinkMovementMethod.getInstance());
	}

	/**
	 * 检查设置是否合法
	 * 
	 * @param text
	 * @param start
	 * @param end
	 * @return
	 */
	private static boolean isValid(CharSequence text, int start, int end) {
		if (start < 0 || start >= text.length() || end < 0
				|| end > text.length() || start >= end) {
			return false;
		}
		return true;
	}

	/**
	 * 向一个textview中追加一个文字内容为text的ClickableSpan
	 * 
	 * @param view
	 * @param text
	 * @param span
	 */
	public static void appendSpanToTextView(TextView view, String text,
			ClickableSpan span) {
		if (view == null || span == null || text == null || text.equals("")) {
			return;
		}
		SpannableStringBuilder ssbText = new SpannableStringBuilder(
				view.getText());
		ssbText.append(text);
		int start = ssbText.length() - text.length();
		int end = ssbText.length();
		ssbText.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssbText.setSpan(new ForegroundColorSpan(Color.BLACK), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(ssbText);
		view.setMovementMethod(LinkMovementMethod.getInstance());
	}

	/**
	 * 插入图像
	 * 
	 * @param context
	 * @param view
	 * @param position
	 * @param bitmap
	 */
	public static void insertImage(Context context, TextView view,
			int position, Bitmap bitmap) {
		CharSequence text = view.getText();
		if (position < 0 || position >= text.length()) {
			return;
		}
		SpannableStringBuilder ssbText = new SpannableStringBuilder(text);
		ssbText.insert(position, "a");
		ImageSpan imageSpan = new ImageSpan(context, bitmap,
				ImageSpan.ALIGN_BASELINE);
		ssbText.setSpan(imageSpan, position, position + 1,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(ssbText);
	}

	/**
	 * 插入图像
	 * 
	 * @param view
	 * @param position
	 * @param drawable
	 */
	public static void insertImage(TextView view, int position,
			Drawable drawable) {
		CharSequence text = view.getText();
		if (position < 0 || position >= text.length()) {
			return;
		}
		SpannableStringBuilder ssbText = new SpannableStringBuilder(text);
		ssbText.insert(position, "a");
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
		ssbText.setSpan(imageSpan, position, position + 1,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		view.setText(ssbText);
	}

	public static void setUnderLine(TextView view, int start, int end) {
		CharSequence text = view.getText();
		if (!isValid(text, start, end)) {
			return;
		}
		SpannableStringBuilder ssbText = new SpannableStringBuilder(text);
		ssbText.setSpan(new UnderlineSpan(), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(ssbText);
	}

	/**
	 * 设置部分高亮文字
	 * 
	 * @param context
	 * @param colorResId
	 *            高亮文字的颜色资源ID
	 * @param view
	 *            需要设置的View
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 */
	public static void setHighlight(Context context, int colorResId,
			TextView view, int start, int end) {
		int color = context.getResources().getColor(colorResId);
		setHighlight(color, view, start, end);
	}

	/**
	 * 设置部分高亮文字
	 * 
	 * @param color
	 *            高亮文字的颜色
	 * @param view
	 *            需要设置的View
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 */
	public static void setHighlight(int color, TextView view, int start, int end) {
		CharSequence text = view.getText();
		if (!isValid(text, start, end)) {
			return;
		}
		SpannableStringBuilder ssbText = new SpannableStringBuilder(text);
		ssbText.setSpan(new ForegroundColorSpan(color), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(ssbText);
	}

	/**
	 * 设置部分高亮文字
	 * 
	 * @param color
	 *            高亮文字的颜色
	 * @param view
	 *            需要设置的View
	 * @param list
	 *            需要高亮的部分的下标集合
	 */
	public static void setHighlight(int color, TextView view, List<int[]> list) {

		try {

			CharSequence text = view.getText();
			if (list.size() == 0) {
				return;
			}
			SpannableStringBuilder ssbText = new SpannableStringBuilder(text);
			/*
			 * for(int i=0;i<list.size();i++) { Log.e("bug",
			 * i+"点："+list.get(i)[0]+": "+list.get(i)[1]); }
			 */
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i)[1] <= text.length()) {
					ssbText.setSpan(new ForegroundColorSpan(color),
							list.get(i)[0], list.get(i)[1] + 1,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				} else {
					ssbText.setSpan(new ForegroundColorSpan(color),
							list.get(i)[0], text.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}

				view.setText(ssbText);
			}

		} catch (Exception e) {
		}

	}

	/**
	 * 设置中划线
	 * 
	 * @param view
	 *            需要设置的View
	 */
	public static void setStrikethrough(TextView view) {
		setStrikethrough(view, 0, view.getText().length());
	}

	/**
	 * 设置中划线
	 * 
	 * @param view
	 *            需要设置的View
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 */
	public static void setStrikethrough(TextView view, int start, int end) {
		CharSequence text = view.getText();
		if (!isValid(text, start, end)) {
			return;
		}
		SpannableStringBuilder ssbText = new SpannableStringBuilder(text);
		ssbText.setSpan(new StrikethroughSpan(), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(ssbText);
	}

	public static void setHightlightKeywords(TextView view, String text) {
		try {
			if (TextUtils.isEmpty(text)) {
				return;
			}
			String outText = text.replace("<b>", "<font color='red'>");
			outText = outText.replace("</b>", "</font>");
			view.setText(Html.fromHtml(outText));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setTwoGrayKeywords(TextView view, String text) {
		try {
			if (TextUtils.isEmpty(text)) {
				return;
			}
			String outText = text.replace("<a>", "<font color=#6f6f6f>");
			outText = outText.replace("</a>", "</font>");
			outText = outText.replace("<b>", "<font color=#BEBEBE>");
			outText = outText.replace("</b>", "</font>");
			view.setText(Html.fromHtml(outText));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将一个ScrollView变成底部可以弹性拉拽。并且拉拽时可以触发一个回调。
	 * 
	 * @param sv
	 *            要添加特性的ScrollView
	 *            当拉拽底部时触发的回调
	 * @param enableIt
	 *            是否启用弹性拉拽。如果传false可以取消弹性效果。
	 */
	public static void makeScrollViewElastic(final ScrollView sv,
			final Runnable callbackWhenPull,
			final Runnable callbackWhenReleasePull, boolean enableIt) {
		if (sv == null) {
			return;
		}
		//
		if (!enableIt) {
			sv.setOnTouchListener(null);
			return;
		}
		// --
		sv.setOnTouchListener(new OnTouchListener() {
			// --初始坐标
			float startX = 0;
			float startY = 0;
			// --总行程
			float dx = 0;
			float dy = 0;
			// --是否自动回滚中
			boolean isMoving = false;
			Thread timer = null;
			// --最大行程
			int MAX_OFFSET_X = 200;
			boolean isTriggered = false;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (sv.getChildCount() == 0) {
					return false;
				}
				// --
				if (timer != null) {
					timer.interrupt();
					timer = null;
				}

				if (event.getAction() == MotionEvent.ACTION_MOVE) {

					if (sv.getChildAt(0).getHeight() == sv.getScrollY()
							+ sv.getHeight()) {

						if (!isMoving) {
							startY = event.getY();
							isMoving = true;
						}
						this.dy = event.getY() - startY;
						this.dy = Math.abs(this.dy) > MAX_OFFSET_X ? (this.dy >= 0 ? MAX_OFFSET_X
								: -MAX_OFFSET_X)
								: this.dy;
						if (this.dy > 0) {
							this.dy = 0;
						}
						sv.getChildAt(0).scrollTo((int) -dx, (int) -dy);
						if (callbackWhenPull != null && !isTriggered) {
							callbackWhenPull.run();
							isTriggered = true;
						}
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					if (isMoving) {
						//
						timer = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									while (Math.abs((int) dy) > 0) {
										Thread.sleep(1);
										dy = dy * 0.99f;
										sv.post(new Runnable() {
											@Override
											public void run() {
												sv.getChildAt(0).scrollTo(
														(int) -dx, (int) -dy);
											}
										});
									}
								} catch (InterruptedException e) {
								}
							}
						});
						timer.start();
						isMoving = false;
						isTriggered = false;
						if (callbackWhenReleasePull != null) {
							callbackWhenReleasePull.run();
						}
					}
				}
				return false;
			}
		});
	}

	public static String captureScreen( View v){
		WindowManager wm = (WindowManager) App.app
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		System.out.println("width:" + width + "height:"+height);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",Locale.CHINA);
		String fname = Environment.getExternalStorageDirectory().getPath()+"/" +sdf.format(new Date())+ ".png";
		View view = v.getRootView();
		// 获取状态栏高度
		Rect rect = new Rect();
		view.getWindowVisibleDisplayFrame(rect);
		int statusBarHeights = rect.top;
		System.out.println("statusBarHeights:" + statusBarHeights);
//		view.setMinimumHeight(height-statusBarHeights);
//		view.setMinimumWidth(width-getDaoHangHeight(App.app));
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		// 去掉状态栏
		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(),0,
				statusBarHeights, width , height - statusBarHeights );
//		Bitmap bitmap = view.getDrawingCache();
		if (null != bmp){
			System.out.println("bitmap got!");
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(fname);
				if (out != null) {
					// 第一参数是图片格式，第二个是图片质量，第三个是输出流
					bmp.compress(Bitmap.CompressFormat.PNG,100,out);
					System.out.println("file" + fname + "output done");
					// 用完关闭
					out.flush();
					out.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(false);
		v.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(fname))));//立即通知图库刷新
		return fname;
	}
	/**
	 * 获取导航栏高度
	 * @param context
	 * @return
	 */
	private static int getDaoHangHeight(Context context) {
		int result = 0;
		int resourceId=0;
		int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
		if (rid!=0){
			resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
			Log.i("height_13", "id："+resourceId);
			Log.i("height_14", "高度："+context.getResources().getDimensionPixelSize(resourceId) +"");
			return context.getResources().getDimensionPixelSize(resourceId);
		}else
			return 0;

	}

	public static String captureView(View v){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",Locale.CHINA);
		String fname = Environment.getExternalStorageDirectory().getPath()+"/" +sdf.format(new Date())+ ".png";
//			v.clearFocus(); //
//			v.setPressed(false); //
//// 能画缓存就返回false
//			boolean willNotCache = v.willNotCacheDrawing();
//			v.setWillNotCacheDrawing(false);
//			int color = v.getDrawingCacheBackgroundColor();
//			v.setDrawingCacheBackgroundColor(0);
//			if (color != 0) {
//				v.destroyDrawingCache();
//			}
		v.setDrawingCacheEnabled(true);
			v.buildDrawingCache(true);
			Bitmap cacheBitmap = v.getDrawingCache();
			if (cacheBitmap == null) {
				return null;
			}

			Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
// Restore the view
//			v.destroyDrawingCache();
//			v.setWillNotCacheDrawing(willNotCache);
//			v.setDrawingCacheBackgroundColor(color);
		v.setDrawingCacheEnabled(false);
		if (null != bitmap){
			System.out.println("bitmap got!");
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(fname);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
			System.out.println("file" + fname + "output done");
		}
		v.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(fname))));//立即通知图库刷新
			return fname;
	}

	public static  void captureScreenByCommnad(){
		//need permission     <uses-permission android:name="android.permission.READ_FRAME_BUFFER"/>
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",Locale.CHINA);
		String mSavedPath = Environment.getExternalStorageDirectory()+"/" +sdf.format(new Date()) +".png";
		try {
			Runtime. getRuntime().exec("screencap -p " + mSavedPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  防止控件被连续误点击的实用方法，传入要保护的时间，在此时间内将不可被再次点击
	 * @param v
	 * @param protectionMilliseconds
	 */
	public static void preventViewMultipleClick(final View v,
			int protectionMilliseconds) {
		v.setClickable(false);
		v.postDelayed(new Runnable() {
			@Override
			public void run() {
				v.setClickable(true);
			}
		}, protectionMilliseconds);
	}

	/**
	 * 防止控件被连续误点击的实用方法，传入要保护的时间，在此时间内将不可被再次点击
	 * @param v
	 * @param isProtection
	 */
	public static void preventViewMultipleClick(final View v,
			boolean isProtection) {

		v.setClickable(isProtection);

	}

	/**
	 * 隐藏软键盘(并非对所有机型有效)
	 * 
	 * @param context
	 * @param view
	 */
	public static void hideSoftInput(Context context, View view) {
		try {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		} catch (Exception e) {
		}
	}

	/**
	 * 显示软键盘(并非对所有机型有效)
	 * 
	 * @param context
	 * @param view
	 */
	public static void showSoftInput(Context context, View view) {
		try {
			view.requestFocus();
			view.requestFocusFromTouch();
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(view, 0);
		} catch (Exception e) {
		}
	}

	public static void rotateView(View view, float toDegrees) {
		rotateView(view, 0, toDegrees, 0.5f, 0.5f, 1000);
	}

	public static void rotateView(View view, float toDegrees, int durationMillis) {
		rotateView(view, 0, toDegrees, 0.5f, 0.5f, durationMillis);
	}

	public static void rotateView(View view, float fromDegrees,
			float toDegrees, float pivotX, float pivotY, int durationMillis) {
		RotateAnimation anim = new RotateAnimation(fromDegrees, toDegrees,
				pivotX, pivotY);
		anim.setFillAfter(true);
		anim.setDuration(durationMillis);
		view.startAnimation(anim);
	}

	/**
	 * 滚动到选择的列表项
	 * 
	 * @param listView
	 * @param position
	 */
	public static void setSelection(final AdapterView<?> listView,
			final int position) {
		listView.requestFocus();
		listView.requestFocusFromTouch();
		listView.postDelayed(new Runnable() {

			@Override
			public void run() {
				listView.setSelection(position);
			}
		}, 200);
	}

	/**
	 * 动态改变View的背景，可保持原先的padding设置
	 * 
	 * @param view
	 * @param resId
	 */
	public static void setBackgroudResource(View view, int resId) {
		if (view == null) {
			return;
		}
		int bottom = view.getPaddingBottom();
		int top = view.getPaddingTop();
		int right = view.getPaddingRight();
		int left = view.getPaddingLeft();
		view.setBackgroundResource(resId);
		view.setPadding(left, top, right, bottom);
	}

	/**
	 * 
	 * @Title: getStatusBarHeight
	 * @Description: 获取状态栏(这里用一句话描述这个方法的作用)
	 * @param: @param context
	 * @param: @return
	 * @return: int
	 * @throws
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		java.lang.reflect.Field field = null;
		int x = 0;
		int statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
			return statusBarHeight;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusBarHeight;
	}

}
