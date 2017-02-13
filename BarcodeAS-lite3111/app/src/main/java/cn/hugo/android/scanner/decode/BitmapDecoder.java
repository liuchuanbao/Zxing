package cn.hugo.android.scanner.decode;

import java.util.Hashtable;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

/**
 * 从bitmap解码
 * 
 * @author hugo
 * 
 */
public class BitmapDecoder {

	MultiFormatReader multiFormatReader;

	public BitmapDecoder(Context context) {

		multiFormatReader = new MultiFormatReader();

		// 解码的参数
		/**DecodeHintType
		 * 封装的一种暗示,调用者可以通过条形码阅读器来帮助它
		 *更快或准确解码。它是由实现决定什么,
		 *如果有的话,与提供的信息。
		 */
		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(
				2);
		// 可以解析的编码类型
		/**
		 * BarcodeFormat> 列举了条形码格式这个包。请按字母顺序排序。
		 */
		Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
		if (decodeFormats == null || decodeFormats.isEmpty()) {
			decodeFormats = new Vector<BarcodeFormat>();
			// 这里设置可扫描的类型，我这里选择了都支持
			//// TODO: 2016/10/19
//			decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
//			decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
		}
		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

		// 设置继续的字符编码格式为UTF8
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8");

		// 设置解析配置参数
		multiFormatReader.setHints(hints);

	}

	/**
	 * 获取解码结果
	 * 
	 * @param bitmap
	 * @return
	 */
	public Result getRawResult(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}

		try {
//			解码图像使用状态设置通过调用setHints()之前。连续*客户将< b > < / b >大速度增加使用这个代替decode()。

			/**
			 * MultiFormatReader   一个方便的类,主入口点到图书馆对于大多数用途。
			 *默认情况下它试图解码库支持的所有条码格式。另外,您
			 *可以提供一个提示对象请求不同的行为,例如只有解码QR码。
			 *
			 * BinaryBitmap   这个类使用的核心位图类zx代表1位数据。读者对象
			 *接受BinaryBitmap并试图破解它
			 *
			 * HybridBinarizer  这个类实现了一个局部阈值算法,虽然比慢GlobalHistogramBinarizer,相当有效。它是专为
			 *高频图像的条形码用黑色白色背景数据
			 *
			 *
			 *BitmapLuminanceSource   他这个类层次结构的目的是抽象不同的位图实现
			 *平台成为一个标准接口请求灰度亮度值。的接口
			 *只提供不变的方法,因此作物和旋转创建副本
			 *
			 */

			Result  results  =multiFormatReader.decodeWithState(new BinaryBitmap(
					new HybridBinarizer(new BitmapLuminanceSource(bitmap))));
			Log.e("xyc","BitMapDecoder + getRawResult="+results);


//			return multiFormatReader.decodeWithState(new BinaryBitmap(
//					new HybridBinarizer(new BitmapLuminanceSource(bitmap))));
			return  results;
		}
		catch (NotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
}
