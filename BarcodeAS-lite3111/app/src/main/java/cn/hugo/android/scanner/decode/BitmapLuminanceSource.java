package cn.hugo.android.scanner.decode;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.LuminanceSource;

public class BitmapLuminanceSource extends LuminanceSource {

	private byte bitmapPixels[];

	protected BitmapLuminanceSource(Bitmap bitmap) {
		super(bitmap.getWidth(), bitmap.getHeight());

		// 首先，要取得该图片的像素数组内容
		int[] data = new int[bitmap.getWidth() * bitmap.getHeight()];

		Log.e("xyc","BitmapLuminanceSource   =1=   "+data.length+"   data "+data.toString());

		this.bitmapPixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
		Log.e("xyc","BitmapLuminanceSource   ==   "+bitmapPixels.length+"   bitmapPixels "+bitmapPixels.toString());
		/**
		 *
		 */
		bitmap.getPixels(data, 0, getWidth(), 0, 0, getWidth(), getHeight());
//		Log.e("xyc","BitmapLuminanceSource   =2=   "+data.length+"   data "+data.toString());
		// 将int数组转换为byte数组，也就是取像素值中蓝色值部分作为辨析内容
		for (int i = 0; i < data.length; i++) {
			this.bitmapPixels[i] = (byte) data[i];
//			Log.e("xyc","BitmapLuminanceSource   ="+i+"=   "+this.bitmapPixels[i]);
		}

	}

	@Override
	public byte[] getMatrix() {
		// 返回我们生成好的像素数据
		Log.e("xyc","BitmapLuminanceSource getMatrix ="+bitmapPixels.toString());
		return bitmapPixels;
	}

	@Override
	public byte[] getRow(int y, byte[] row) {
		// 这里要得到指定行的像素数据
		System.arraycopy(bitmapPixels, y * getWidth(), row, 0, getWidth());
		Log.e("xyc","BitmapLuminanceSource = bitmapPixels="+bitmapPixels+"y * getWidth()=="+y * getWidth()+"row =="+row+"0"+" getWidth()== "+getWidth());
		return row;
	}
}