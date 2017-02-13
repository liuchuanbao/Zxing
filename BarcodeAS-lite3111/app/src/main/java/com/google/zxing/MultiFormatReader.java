/*
 * Copyright 2007 ZXing authors
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

package com.google.zxing;

import android.util.Log;

import com.google.zxing.aztec.AztecReader;
import com.google.zxing.datamatrix.DataMatrixReader;
import com.google.zxing.maxicode.MaxiCodeReader;
import com.google.zxing.oned.MultiFormatOneDReader;
import com.google.zxing.pdf417.PDF417Reader;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * MultiFormatReader is a convenience class and the main entry point into the library for most uses.
 * By default it attempts to decode all barcode formats that the library supports. Optionally, you
 * can provide a hints object to request different behavior, for example only decoding QR codes.
 *
 * MultiFormatReader是一个方便的类,主入口点到图书馆对于大多数用途。
 *默认情况下它试图解码库支持的所有条码格式。另外,您
 *可以提供一个提示对象请求不同的行为,例如只有解码QR码。
 *
 * @author Sean Owen
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class MultiFormatReader implements Reader {

  private Map<DecodeHintType,?> hints;
  private Reader[] readers;

  /**
   * This version of decode honors the intent of Reader.decode(BinaryBitmap) in that it
   * passes null as a hint to the decoders. However, that makes it inefficient to call repeatedly.
   * Use setHints() followed by decodeWithState() for continuous scan applications.
   *这个版本的解码荣誉的意图Reader.decode(BinaryBitmap)它
   *传递null作为提示解码器。然而,使其低效率的重复调用。
   *使用setHints()随后decodeWithState()连续扫描应用程序。
   * @param image The pixel data to decode
   * @return The contents of the image
   * @throws NotFoundException Any errors which occurred
   */
  @Override
  public Result decode(BinaryBitmap image) throws NotFoundException {
    setHints(null);
    return decodeInternal(image);
  }

  /**
   * Decode an image using the hints provided. Does not honor existing state.
   *解码图像使用提供的线索。不尊重现有的状态。
   * @param image The pixel data to decode
   * @param hints The hints to use, clearing the previous state.
   * @return The contents of the image
   * @throws NotFoundException Any errors which occurred
   */
  @Override
  public Result decode(BinaryBitmap image, Map<DecodeHintType,?> hints) throws NotFoundException {
    setHints(hints);
    Log.e("xyc","MultiFormatReader decode ="+decodeInternal(image).toString());
    return decodeInternal(image);
  }

  /**
   * Decode an image using the state set up by calling setHints() previously. Continuous scan
   * clients will get a <b>large</b> speed increase by using this instead of decode().
   *解码图像使用状态设置通过调用setHints()之前。连续扫描
   *客户将< b > < / b >大速度增加使用这个代替decode()。
   * @param image The pixel data to decode
   * @return The contents of the image
   * @throws NotFoundException Any errors which occurred
   */
  public Result decodeWithState(BinaryBitmap image) throws NotFoundException {
    // Make sure to set up the default state so we don't crash
    if (readers == null) {
      setHints(null);
    }
    Log.e("xyc","MultiFormatReader decodeWithState ="+image);
    Result sss  = decodeInternal(image);
    Log.e("xyc","MultiFormatReader decodeWithState ="+sss);

    return decodeInternal(image);
  }

  /**
   * This method adds state to the MultiFormatReader. By setting the hints once, subsequent calls
   * to decodeWithState(image) can reuse the same set of readers without reallocating memory. This
   * is important for performance in continuous scan clients.
   *此方法添加MultiFormatReader状态。通过设置提示一次,后续调用
   * decodeWithState(图片)可以重用相同的读者没有重新分配内存。这
   *连续扫描性能最重要的是客户。
   * @param hints The set of hints to use for subsequent calls to decode(image)
   */
  public void setHints(Map<DecodeHintType,?> hints) {
    this.hints = hints;

    boolean tryHarder = hints != null && hints.containsKey(DecodeHintType.TRY_HARDER);
    @SuppressWarnings("unchecked")
    Collection<BarcodeFormat> formats =
        hints == null ? null : (Collection<BarcodeFormat>) hints.get(DecodeHintType.POSSIBLE_FORMATS);
    Collection<Reader> readers = new ArrayList<>();
    if (formats != null) {
      boolean addOneDReader =
          formats.contains(BarcodeFormat.UPC_A) ||
          formats.contains(BarcodeFormat.UPC_E) ||
          formats.contains(BarcodeFormat.EAN_13) ||
          formats.contains(BarcodeFormat.EAN_8) ||
          formats.contains(BarcodeFormat.CODABAR) ||
          formats.contains(BarcodeFormat.CODE_39) ||
          formats.contains(BarcodeFormat.CODE_93) ||
          formats.contains(BarcodeFormat.CODE_128) ||
          formats.contains(BarcodeFormat.ITF) ||
          formats.contains(BarcodeFormat.RSS_14) ||
          formats.contains(BarcodeFormat.RSS_EXPANDED);
      // Put 1D readers upfront in "normal" mode
      if (addOneDReader && !tryHarder) {
        readers.add(new MultiFormatOneDReader(hints));
      }
      if (formats.contains(BarcodeFormat.QR_CODE)) {
        readers.add(new QRCodeReader());
      }
      if (formats.contains(BarcodeFormat.DATA_MATRIX)) {
        //datamatrix解码
        readers.add(new DataMatrixReader());
      }
      if (formats.contains(BarcodeFormat.AZTEC)) {
        readers.add(new AztecReader());
      }
      if (formats.contains(BarcodeFormat.PDF_417)) {
         readers.add(new PDF417Reader());
      }
      if (formats.contains(BarcodeFormat.MAXICODE)) {
         readers.add(new MaxiCodeReader());
      }
      // At end in "try harder" mode
      if (addOneDReader && tryHarder) {
        readers.add(new MultiFormatOneDReader(hints));
      }
    }
    if (readers.isEmpty()) {
      if (!tryHarder) {
        readers.add(new MultiFormatOneDReader(hints));
      }

      readers.add(new QRCodeReader());
      readers.add(new DataMatrixReader());
      readers.add(new AztecReader());
      readers.add(new PDF417Reader());
      readers.add(new MaxiCodeReader());

      if (tryHarder) {
        readers.add(new MultiFormatOneDReader(hints));
      }
    }
    this.readers = readers.toArray(new Reader[readers.size()]);
  }

  @Override
  public void reset() {
    if (readers != null) {
      for (Reader reader : readers) {
        reader.reset();
      }
    }
  }


  /**
   *
   *
   * @param image
   * @return
   * @throws NotFoundException
     */
  private Result decodeInternal(BinaryBitmap image) throws NotFoundException {
    if (readers != null) {
      /**
       * Reader
       *  *定位和解码中的一些格式的条形码图像
       *,这里是实现解码的核心方法,包括数据的二值化和最内层的解码方法 Result result = decodeRow(rowNumber, row, hints),因为我这里只分析一维码的部分,所以预先设定的解码类型,这里会跳转到 这个类MultiFormatUPCEANReader中的decodeRow 方法.

       OneDReader.doDecode(BinaryBitmap image, Map<DecodeHintType, ?> hints) throws NotFoundException 对每一次获取的帧进行最大15次的解码,这里的解码只是针对一行数据.从中间区域开始,分别对上下依次获取的行数据进行解码,如果解码成功则返回.

       强调一点,zxing 不对获取的图片数据进行旋转,虽然支持旋转.但是支持对转动180 的一维码的解码
       */
      for (Reader reader : readers) {
        try {

//          Log.e("xyc","MultiformatReade  = decodeInternal = "+reader.decode(image, hints).toString());
          return reader.decode(image, hints);
        } catch (ReaderException re) {
          // continue
        }
      }
    }
    throw NotFoundException.getNotFoundInstance();
  }

}
