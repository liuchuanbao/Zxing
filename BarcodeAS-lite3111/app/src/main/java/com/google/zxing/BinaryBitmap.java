/*
 * Copyright 2009 ZXing authors
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

import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;

/**
 * This class is the core bitmap class used by ZXing to represent 1 bit data. Reader objects
 * accept a BinaryBitmap and attempt to decode it.
 *Resets any internal state the implementation has after a decode, to prepare it
 * for reuse
 * 这个类使用的核心位图类zx代表1位数据。读者对象
 *接受BinaryBitmap并试图破解它
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class BinaryBitmap {

  private final Binarizer binarizer;
  private BitMatrix matrix;

  public BinaryBitmap(Binarizer binarizer) {
    if (binarizer == null) {
      throw new IllegalArgumentException("Binarizer must be non-null.");
    }
    this.binarizer = binarizer;
  }

  /**
   * @return The width of the bitmap.
   */
  public int getWidth() {

    Log.e("xyc","BinaryBitmap +getWidth "+binarizer.getWidth());
    return binarizer.getWidth();
  }

  /**
   * @return The height of the bitmap.
   */
  public int getHeight() {
    Log.e("xyc","BinaryBitmap +getHeight "+binarizer.getHeight());

    return binarizer.getHeight();
  }

  /**
   * Converts one row of luminance data to 1 bit data. May actually do the conversion, or return
   * cached data. Callers should assume this method is expensive and call it as seldom as possible.
   * This method is intended for decoding 1D barcodes and may choose to apply sharpening.
   *
   * 将一行的亮度数据转换为1位数据。实际上可能执行转换,或返回
   *缓存数据。调用者应该承担这个方法是昂贵的,称之为尽可能很少。
   *此方法用于解码一维条形码,可以选择应用锐化
   *
   * @param y The row to fetch, which must be in [0, bitmap height)
   * @param row An optional preallocated array. If null or too small, it will be ignored.
   *            If used, the Binarizer will call BitArray.clear(). Always use the returned object.
   * @return The array of bits for this row (true means black).
   * @throws NotFoundException if row can't be binarized
   */
  public BitArray getBlackRow(int y, BitArray row) throws NotFoundException {
      //实现了矩阵

//    Log.e("xyc","BinaryBitmap +getBlackRow "+y+"row 1=="+row);
//    Log.e("xyc","BinaryBitmap +getBlackRow "+y+"row 3=="+row.toString());

//    BitArray mbitArray  = binarizer.getBlackRow(y, row);
//    Log.e("xyc","BinaryBitmap +getBlackRow =BitArray="+mbitArray.toString());

    return binarizer.getBlackRow(y, row);
  }

  /**
   * Converts a 2D array of luminance data to 1 bit. As above, assume this method is expensive
   * and do not call it repeatedly. This method is intended for decoding 2D barcodes and may or
   * may not apply sharpening. Therefore, a row from this matrix may not be identical to one
   * fetched using getBlackRow(), so don't mix and match between them.
   * 将亮度数据的二维数组转换为1。如上所述,认为该方法是昂贵的
   *重复,不叫它。该方法用于解码2 d条形码和可能
   *可能不适用锐化。因此,从这一行矩阵可能不是相同的
   *获取使用getBlackRow(),所以不要混合和匹配。
   *
   *
   * @return The 2D array of bits for the image (true means black).
   * @throws NotFoundException if image can't be binarized to make a matrix
   */
  public BitMatrix getBlackMatrix() throws NotFoundException {
    // The matrix is created on demand the first time it is requested, then cached. There are two
    // reasons for this:矩阵是按需创建第一次请求,然后缓存
    // 1. This work will never be done if the caller only installs 1D Reader objects, or if a
    //    1D Reader finds a barcode before the 2D Readers run.
    // 2. This work will only be done once even if the caller installs multiple 2D Readers.
    if (matrix == null) {
      matrix = binarizer.getBlackMatrix();
    }
    Log.e("xyc","BinaryBitmap +getBlackMatrix() ="+matrix.toString("0","1"));
    return matrix;
  }

  /**
   * @return Whether this bitmap can be cropped.
   */
  public boolean isCropSupported() {
    return binarizer.getLuminanceSource().isCropSupported();
  }

  /**
   * Returns a new object with cropped image data. Implementations may keep a reference to the
   * original data rather than a copy. Only callable if isCropSupported() is true.
   *返回一个新对象与裁剪图像数据。可以保持一个参考实现
   *原始数据而不是一个副本。只有可调用isCropSupported()是正确的。
   *
   * @param left The left coordinate, which must be in [0,getWidth())
   * @param top The top coordinate, which must be in [0,getHeight())
   * @param width The width of the rectangle to crop.
   * @param height The height of the rectangle to crop.
   * @return A cropped version of this object.
   */
  public BinaryBitmap crop(int left, int top, int width, int height) {
    LuminanceSource newSource = binarizer.getLuminanceSource().crop(left, top, width, height);

    Log.e("xyc","BinaryBitmap +BinaryBitmap() "+"left"+left+"top"+top+"width"+width+"height"+height);
    return new BinaryBitmap(binarizer.createBinarizer(newSource));
  }

  /**
   * 这个位图是否支持逆时针旋转
   * @return Whether this bitmap supports counter-clockwise rotation.
   */
  public boolean isRotateSupported() {
    return binarizer.getLuminanceSource().isRotateSupported();
  }

  /**
   * Returns a new object with rotated image data by 90 degrees counterclockwise.
   * Only callable if {@link #isRotateSupported()} is true.
   * 返回一个新对象由90度逆时针旋转后的图像数据。
   *只调用如果{ @link # isRotateSupported()}是正确的。
   *
   * @return A rotated version of this object.
   */
  public BinaryBitmap rotateCounterClockwise() {
    LuminanceSource newSource = binarizer.getLuminanceSource().rotateCounterClockwise();
    return new BinaryBitmap(binarizer.createBinarizer(newSource));
  }

  /**
   * Returns a new object with rotated image data by 45 degrees counterclockwise.
   * Only callable if {@link #isRotateSupported()} is true.
   *返回一个新对象由45度逆时针旋转后的图像数据。
   *只调用如果{ @link # isRotateSupported()}是正确的。
   * @return A rotated version of this object.
   */
  public BinaryBitmap rotateCounterClockwise45() {
    LuminanceSource newSource = binarizer.getLuminanceSource().rotateCounterClockwise45();
    return new BinaryBitmap(binarizer.createBinarizer(newSource));
  }

  @Override
  public String toString() {
    try {

      Log.e("xyc","BinaryBitmap   toString=="+getBlackMatrix().toString());
      return getBlackMatrix().toString();
    } catch (NotFoundException e) {
      return "";
    }
  }

}
