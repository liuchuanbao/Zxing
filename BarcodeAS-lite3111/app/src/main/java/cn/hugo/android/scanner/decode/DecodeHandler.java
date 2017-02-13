/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hugo.android.scanner.decode;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import cn.hugo.android.scanner.CaptureActivity;
import cn.hugo.android.scanner.R;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

/**
 * .DecodeHandler 定义了解码的handler，用来对扫描的数据进行解码，
 * 由com.google.zxing.client.android.camera.PreviewCallback 调用
 *
 *
 *
 *
 */
final class DecodeHandler extends Handler {

	private static final String TAG = DecodeHandler.class.getSimpleName();

	private final CaptureActivity activity;

	private final MultiFormatReader multiFormatReader;

	private boolean running = true;

	DecodeHandler(CaptureActivity activity, Map<DecodeHintType, Object> hints) {
		multiFormatReader = new MultiFormatReader();
		multiFormatReader.setHints(hints);
		this.activity = activity;
	}

	/**
	 *
	 * 通过传递 handler, 当有消息时,会自动跳转到 public void handleMessage(Message message) {}处执行.  第二部
	 *
	 * @param message
     */
	@Override
	public void handleMessage(Message message) {
		if (!running) {
			return;
		}

		switch (message.what) {
			case R.id.decode:
				decode((byte[]) message.obj, message.arg1, message.arg2);
				break;
			case R.id.quit:
				running = false;
				Looper.myLooper().quit();
				break;
		}
	}

	/**
	 * Decode the data within the viewfinder rectangle, and time how long it
	 * took. For efficiency, reuse the same reader objects from one decode to
	 * the next.
	 * *解码取景器矩形内的数据,和时间多长时间
	 *。为了提高效率,重用相同的读者对象从一个解码
	 *
	 * 过程③,这里实现对数据的解码,如果解码不成功,则进行下次解码,否则将会发送消息给CaptureActivityHandler 对象.  第三步
	 *
	 * @param data
	 *            The YUV preview frame.YUV预览帧。
	 * @param width
	 *            The width of the preview frame.
	 * @param height
	 *            The height of the preview frame.
	 */
	private void decode(byte[] data, int width, int height) {
		long start = System.currentTimeMillis();
		Result rawResult = null;

		byte[] rotatedData = new byte[data.length];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++)
				rotatedData[x * height + height - y - 1] = data[x + y * width];
		}
		int tmp = width;
		width = height;
		height = tmp;


/**
 * *这个对象扩展LuminanceSource返回数组的YUV数据从相机驱动,
 *选择作物一个矩形内的全部数据。这可以用来排除
 *多余的像素周围的周长,并加快解码。
 *
 *它适用于任何像素格式Y通道平面和最先出现,包括
 * YCbCr_420_SP YCbCr_422_SP。
 */
		PlanarYUVLuminanceSource source = activity.getCameraManager()
				.buildLuminanceSource(rotatedData, width, height);
		if (source != null) {
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			try {
				// 预览界面最终取到的是个bitmap，然后对其进行解码
				/**
				 * 解码图像使用状态设置通过调用setHints()之前。连续扫描
				 *客户将< b > < / b >大速度增加使用这个代替decode()。
				 */
				rawResult = multiFormatReader.decodeWithState(bitmap);
				Log.e("xyc","DecoderHander rawResult = "+rawResult);
				Log.e("xyc","DecoderHander rawResult.toString() = "+rawResult.toString());
			}
			catch (ReaderException re) {
				// continue
			}
			finally {
				multiFormatReader.reset();
			}
		}

		Handler handler = activity.getHandler();
		if (rawResult != null) {
			// Don't log the barcode contents for security.
			long end = System.currentTimeMillis();
			Log.d(TAG, "Found barcode in " + (end - start) + " ms");
			if (handler != null) {
				Message message = Message.obtain(handler,
						R.id.decode_succeeded, rawResult);

				Bundle bundle = new Bundle();
				bundleThumbnail(source, bundle);

				message.setData(bundle);
				message.sendToTarget();
			}
		}
		else {
			if (handler != null) {
				Message message = Message.obtain(handler, R.id.decode_failed);
				message.sendToTarget();
			}
		}
	}

	private static void bundleThumbnail(PlanarYUVLuminanceSource source,
			Bundle bundle) {
		int[] pixels = source.renderThumbnail();
		int width = source.getThumbnailWidth();
		int height = source.getThumbnailHeight();
		Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height,
				Bitmap.Config.ARGB_8888);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
		bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
		bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, (float) width
				/ source.getWidth());
	}

}
