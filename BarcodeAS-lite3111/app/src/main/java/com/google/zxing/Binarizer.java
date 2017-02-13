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

import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;

/**
 * This class hierarchy provides a set of methods to convert luminance data to 1 bit data.
 * It allows the algorithm to vary polymorphically, for example allowing a very expensive
 * thresholding technique for servers and a fast one for mobile. It also permits the implementation
 * to vary, e.g. a JNI version for Android and a Java fallback version for other platforms.
 *
 * 他的类层次结构提供了一组方法亮度数据转换为1位数据。
 *算法可以以多态方式各不相同,例如允许非常昂贵
 *阈值技术,服务器和一个快速移动。它还允许实现
 *不同,如JNI版本为Android和其他平台的Java版本回退。
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public abstract class Binarizer {

  private final LuminanceSource source;

  protected Binarizer(LuminanceSource source) {
    this.source = source;
  }

  public final LuminanceSource getLuminanceSource() {
    return source;
  }

  /**
   * Converts one row of luminance data to 1 bit data. May actually do the conversion, or return
   * cached data. Callers should assume this method is expensive and call it as seldom as possible.
   * This method is intended for decoding 1D barcodes and may choose to apply sharpening.
   * For callers which only examine one row of pixels at a time, the same BitArray should be reused
   * and passed in with each call for performance. However it is legal to keep more than one row
   * at a time if needed.
   *
   * @param y The row to fetch, which must be in [0, bitmap height)
   * @param row An optional preallocated array. If null or too small, it will be ignored.
   *            If used, the Binarizer will call BitArray.clear(). Always use the returned object.
   * @return The array of bits for this row (true means black).
   * @throws NotFoundException if row can't be binarized
   *
   * 将一行的亮度数据转换为1位数据。实际上可能执行转换,或返回
   *缓存数据。调用者应该承担这个方法是昂贵的,称之为尽可能很少。
   *此方法用于解码一维条形码,可以选择应用锐化。
   *电话只检查一行的像素,同一BitArray应该重用
   *并通过为每个调用的性能。然而保持超过一行是合法的
   *如果需要。
   *
   */
  public abstract BitArray getBlackRow(int y, BitArray row) throws NotFoundException;

  /**
   * Converts a 2D array of luminance data to 1 bit data. As above, assume this method is expensive
   * and do not call it repeatedly. This method is intended for decoding 2D barcodes and may or
   * may not apply sharpening. Therefore, a row from this matrix may not be identical to one
   * fetched using getBlackRow(), so don't mix and match between them.
   * 将亮度数据的二维数组转换为1位数据。如上所述,认为该方法是昂贵的
   *重复,不叫它。该方法用于解码2 d条形码和可能
   *可能不适用锐化。因此,从这一行矩阵可能不是相同的
   *获取使用getBlackRow(),所以不要混合和匹配。
   *
   *
   * @return The 2D array of bits for the image (true means black).
   * @throws NotFoundException if image can't be binarized to make a matrix
   */
  public abstract BitMatrix getBlackMatrix() throws NotFoundException;

  /**
   * Creates a new object with the same type as this Binarizer implementation, but with pristine
   * state. This is needed because Binarizer implementations may be stateful, e.g. keeping a cache
   * of 1 bit data. See Effective Java for why we can't use Java's clone() method.
   * 创建一个新的对象具有相同类型Binarizer实现,但原始
   *状态。这是必要的,因为Binarizer实现可能有状态,如保持缓存
   * 1位数据。看到有效的Java为什么我们不能使用Java的克隆()方法。
   *
   *
   * @param source The LuminanceSource this Binarizer will operate on.
   * @return A new concrete Binarizer implementation object.
   */
  public abstract Binarizer createBinarizer(LuminanceSource source);

  public final int getWidth() {
    return source.getWidth();
  }

  public final int getHeight() {
    return source.getHeight();
  }

}
