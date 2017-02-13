/*
 * Copyright (C) 2008 ZXing authors
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

package cn.hugo.android.scanner;

/**
 * This class provides the constants to use when sending an Intent to Barcode Scanner.
 * These strings are effectively API and cannot be changed.
 *
 * 这个类提供了使用的常量在发送条形码扫描器的意图。
 *这些字符串实际上是API和无法改变的。
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class Intents {
  private Intents() {
  }

  public static final class Scan {
    /**
     * Send this intent to open the Barcodes app in scanning mode, find a barcode, and return
     * 把这个意图打开条形码应用在扫描模式中,找到一个条形码,并返回
     *结果。the results.
     *
     */
    public static final String ACTION = "com.google.zxing.client.android.SCAN";

    /**
     * By default, sending this will decode all barcodes that we understand. However it
     * may be useful to limit scanning to certain formats. Use
     * {@link android.content.Intent#putExtra(String, String)} with one of the values below.
     *默认情况下,这将解码所有的条形码,我们理解。然而,
     *可能是有用的限制扫描特定格式。使用
     * { @link android.content。意图# putExtra(字符串,字符串)}的值以下。
     *
     *设置这是有效简称设置显式格式与} { @link #格式。
     *是被设置。
     * /
     * Setting this is effectively shorthand for setting explicit formats with {@link #FORMATS}.
     * It is overridden by that setting.
     */
    public static final String MODE = "SCAN_MODE";

    /**
     * Decode only UPC and EAN barcodes. This is the right choice for shopping apps which get
     * prices, reviews, etc. for products.
     *
     * 解码只有UPC和EAN条形码。这是正确的选择购物应用程序
     *价格、评论等产品。
     */
    public static final String PRODUCT_MODE = "PRODUCT_MODE";

    /**
     * Decode only 1D barcodes.
     * 解码只有一维条形码。
     */
    public static final String ONE_D_MODE = "ONE_D_MODE";

    /**
     * Decode only QR codes.
     * 只有QR码解码。
     */
    public static final String QR_CODE_MODE = "QR_CODE_MODE";

    /**
     * Decode only Data Matrix codes.
     * 解码数据矩阵代码。
     */
    public static final String DATA_MATRIX_MODE = "DATA_MATRIX_MODE";

    /**
     * Decode only Aztec.
     * 解码只有阿兹特克。
     */
    public static final String AZTEC_MODE = "AZTEC_MODE";

    /**
     * Decode only PDF417.
     * 只有PDF417解码。
     */
    public static final String PDF417_MODE = "PDF417_MODE";

    /**
     * Comma-separated list of formats to scan for. The values must match the names of
     * {@link com.google.zxing.BarcodeFormat}s, e.g. {@link com.google.zxing.BarcodeFormat#EAN_13}.
     * Example: "EAN_13,EAN_8,QR_CODE". This overrides {@link #MODE}.
     * 以逗号分隔的格式扫描。的值必须匹配的名称
     * { @link com.google.zxing。BarcodeFormat },例如{ @link com.google.zxing.BarcodeFormat # EAN_13 }。
     *的例子:“EAN_13、EAN_8 QR_CODE”。这将覆盖} { @link #模式
     *
     */
    public static final String FORMATS = "SCAN_FORMATS";
	
    /**
     * Optional parameter to specify the id of the camera from which to recognize barcodes.
     * Overrides the default camera that would otherwise would have been selected.
     * If provided, should be an int.
     *
     * 可选参数指定的id识别条形码的相机。
     *覆盖默认的相机,否则会被选中。
     *如果提供,应该是一个int。
     */
    public static final String CAMERA_ID = "SCAN_CAMERA_ID";

    /**
     * @see com.google.zxing.DecodeHintType#CHARACTER_SET
     */
    public static final String CHARACTER_SET = "CHARACTER_SET";

    /**
     * Optional parameters to specify the width and height of the scanning rectangle in pixels.
     * The app will try to honor these, but will clamp them to the size of the preview frame.
     * You should specify both or neither, and pass the size as an int.
     * 可选参数指定扫描矩形的宽度和高度(以像素为单位)。
     *应用程序将尝试这些荣誉,但夹预览帧的大小。
     *你应该指定或没有,通过大小int。
     *
     */
    public static final String WIDTH = "SCAN_WIDTH";
    public static final String HEIGHT = "SCAN_HEIGHT";

    /**
     * Desired duration in milliseconds for which to pause after a successful scan before
     * returning to the calling intent. Specified as a long, not an integer!
     * For example: 1000L, not 1000.
     * 所需的时间,以毫秒为单位的暂停扫描成功后
     *返回给调用的意图。指定为一个长,不是一个整数!
     * 1000(例如:l,而不是1000年。
     *
     */
    public static final String RESULT_DISPLAY_DURATION_MS = "RESULT_DISPLAY_DURATION_MS";

    /**
     * Prompt to show on-screen when scanning by intent. Specified as a {@link String}.
     *
     * 提示显示屏幕扫描时的意图。指定为一个{ @link字符串}。
     */
    public static final String PROMPT_MESSAGE = "PROMPT_MESSAGE";

    /**
     * If a barcode is found, Barcodes returns {@link android.app.Activity#RESULT_OK} to
     * {@link android.app.Activity#onActivityResult(int, int, android.content.Intent)}
     * of the app which requested the scan via
     * {@link android.app.Activity#startActivityForResult(android.content.Intent, int)}
     * The barcodes contents can be retrieved with
     * {@link android.content.Intent#getStringExtra(String)}. 
     * If the user presses Back, the result code will be {@link android.app.Activity#RESULT_CANCELED}.
     *
     * 如果找到一个条形码,条形码返回{ @link android.app。活动# RESULT_OK }
     * { @link android.app。活动# onActivityResult(int,int,android.content.Intent)}
     *通过请求的应用程序扫描
     * { @link android.app.Activity # startActivityForResult(android.content。意图,int)}
     *条形码内容可以检索
     * { @link android.content.Intent # getStringExtra(字符串)}。
     *如果用户按下回来,结果代码将{ @link android.app.Activity # RESULT_CANCELED }。
     *
     */
    public static final String RESULT = "SCAN_RESULT";

    /**
     * Call {@link android.content.Intent#getStringExtra(String)} with {@link #RESULT_FORMAT}
     * to determine which barcode format was found.
     * See {@link com.google.zxing.BarcodeFormat} for possible values.
     * 叫{ @link android.content.Intent # getStringExtra(字符串)}与{ @link # RESULT_FORMAT }
     *来确定哪个条码格式被发现。
     *看到{ @link com.google.zxing。BarcodeFormat }可能值。
     *
     *
     */
    public static final String RESULT_FORMAT = "SCAN_RESULT_FORMAT";

    /**
     * Call {@link android.content.Intent#getStringExtra(String)} with {@link #RESULT_UPC_EAN_EXTENSION}
     * to return the content of any UPC extension barcode that was also found. Only applicable
     * to {@link com.google.zxing.BarcodeFormat#UPC_A} and {@link com.google.zxing.BarcodeFormat#EAN_13}
     * formats.
     *
     * 叫{ @link android.content.Intent # getStringExtra(字符串)}与{ @link # RESULT_UPC_EAN_EXTENSION }
     *返回任何UPC条形码延伸的内容也被发现。只适用
     * { @link com.google.zxing。BarcodeFormat # UPC_A }和{ @link com.google.zxing.BarcodeFormat # EAN_13 }
     *格式。
     */
    public static final String RESULT_UPC_EAN_EXTENSION = "SCAN_RESULT_UPC_EAN_EXTENSION";

    /**
     * Call {@link android.content.Intent#getByteArrayExtra(String)} with {@link #RESULT_BYTES}
     * to get a {@code byte[]} of raw bytes in the barcode, if available.
     *
     * 叫{ @link android.content.Intent # getByteArrayExtra(字符串)}与{ @link # RESULT_BYTES }
     *获得{ @code byte[]}原始字节的条码,如果可用。
     */
    public static final String RESULT_BYTES = "SCAN_RESULT_BYTES";

    /**
     * Key for the value of {@link com.google.zxing.ResultMetadataType#ORIENTATION}, if available.
     * Call {@link android.content.Intent#getIntArrayExtra(String)} with {@link #RESULT_ORIENTATION}.
     * 关键的价值{ @link com.google.zxing。ResultMetadataType #取向},如果可用。
     *叫{ @link android.content.Intent # getIntArrayExtra(字符串)}与{ @link # RESULT_ORIENTATION }。
     *
     */
    public static final String RESULT_ORIENTATION = "SCAN_RESULT_ORIENTATION";

    /**
     * Key for the value of {@link com.google.zxing.ResultMetadataType#ERROR_CORRECTION_LEVEL}, if available.
     * Call {@link android.content.Intent#getStringExtra(String)} with {@link #RESULT_ERROR_CORRECTION_LEVEL}.
     */
    public static final String RESULT_ERROR_CORRECTION_LEVEL = "SCAN_RESULT_ERROR_CORRECTION_LEVEL";

    /**
     * Prefix for keys that map to the values of {@link com.google.zxing.ResultMetadataType#BYTE_SEGMENTS},
     * if available. The actual values will be set under a series of keys formed by adding 0, 1, 2, ...
     * to this prefix. So the first byte segment is under key "SCAN_RESULT_BYTE_SEGMENTS_0" for example.
     * Call {@link android.content.Intent#getByteArrayExtra(String)} with these keys.
     */
    public static final String RESULT_BYTE_SEGMENTS_PREFIX = "SCAN_RESULT_BYTE_SEGMENTS_";

    /**
     * Setting this to false will not save scanned codes in the history. Specified as a {@code boolean}.
     */
    public static final String SAVE_HISTORY = "SAVE_HISTORY";

    private Scan() {
    }
  }

  public static final class History {

    public static final String ITEM_NUMBER = "ITEM_NUMBER";

    private History() {
    }
  }

  public static final class Encode {
    /**
     * Send this intent to encode a piece of data as a QR code and display it full screen, so
     * that another person can scan the barcode from your screen.
     */
    public static final String ACTION = "com.google.zxing.client.android.ENCODE";

    /**
     * The data to encode. Use {@link android.content.Intent#putExtra(String, String)} or
     * {@link android.content.Intent#putExtra(String, android.os.Bundle)}, 
     * depending on the type and format specified. Non-QR Code formats should
     * just use a String here. For QR Code, see Contents for details.
     */
    public static final String DATA = "ENCODE_DATA";

    /**
     * The type of data being supplied if the format is QR Code. Use
     * {@link android.content.Intent#putExtra(String, String)} with one of {@link Contents.Type}.
     */
    public static final String TYPE = "ENCODE_TYPE";

    /**
     * The barcode format to be displayed. If this isn't specified or is blank,
     * it defaults to QR Code. Use {@link android.content.Intent#putExtra(String, String)}, where
     * format is one of {@link com.google.zxing.BarcodeFormat}.
     */
    public static final String FORMAT = "ENCODE_FORMAT";

    /**
     * Normally the contents of the barcode are displayed to the user in a TextView. Setting this
     * boolean to false will hide that TextView, showing only the encode barcode.
     */
    public static final String SHOW_CONTENTS = "ENCODE_SHOW_CONTENTS";

    private Encode() {
    }
  }

  public static final class SearchBookContents {
    /**
     * Use Google Book Search to search the contents of the book provided.
     */
    public static final String ACTION = "com.google.zxing.client.android.SEARCH_BOOK_CONTENTS";

    /**
     * The book to search, identified by ISBN number.
     */
    public static final String ISBN = "ISBN";

    /**
     * An optional field which is the text to search for.
     */
    public static final String QUERY = "QUERY";

    private SearchBookContents() {
    }
  }

  public static final class WifiConnect {
    /**
     * Internal intent used to trigger connection to a wi-fi network.
     */
    public static final String ACTION = "com.google.zxing.client.android.WIFI_CONNECT";

    /**
     * The network to connect to, all the configuration provided here.
     */
    public static final String SSID = "SSID";

    /**
     * The network to connect to, all the configuration provided here.
     */
    public static final String TYPE = "TYPE";

    /**
     * The network to connect to, all the configuration provided here.
     */
    public static final String PASSWORD = "PASSWORD";

    private WifiConnect() {
    }
  }

  public static final class Share {
    /**
     * Give the user a choice of items to encode as a barcode, then render it as a QR Code and
     * display onscreen for a friend to scan with their phone.
     */
    public static final String ACTION = "com.google.zxing.client.android.SHARE";

    private Share() {
    }
  }
}
