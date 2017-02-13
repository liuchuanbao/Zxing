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

/**
 * The purpose of this class hierarchy is to abstract different bitmap implementations across
 * platforms into a standard interface for requesting greyscale luminance values. The interface
 * only provides immutable methods; therefore crop and rotation create copies. This is to ensure
 * that one Reader does not modify the original luminance source and leave it in an unknown state
 * for other Readers in the chain.
 *
 * 他这个类层次结构的目的是抽象不同的位图实现
 *平台成为一个标准接口请求灰度亮度值。的接口
 *只提供不变的方法,因此作物和旋转创建副本。这是为了确保
 *一个读者不修改原来的亮度源和把它在一个未知的状态
 *其他读者的链。
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public abstract class LuminanceSource {

  private final int width;
  private final int height;

  protected LuminanceSource(int width, int height) {
    this.width = width;
    this.height = height;
  }

  /**
   * Fetches one row of luminance data from the underlying platform's bitmap. Values range from
   * 0 (black) to 255 (white). Because Java does not have an unsigned byte type, callers will have
   * to bitwise and with 0xff for each value. It is preferable for implementations of this method
   * to only fetch this row rather than the whole image, since no 2D Readers may be installed and
   * getMatrix() may never be called.
   *获取一行的亮度数据从底层平台的位图。值的范围从
   * 0(黑色)到255(白色)。因为Java没有一个无符号字节类型,调用者
   *为每个值位和0 xff。最好对该方法的实现
   *只卖这一行而不是整个图像,因为没有2 d读者可以安装
   * getMatrix()可能永远不会被调用。
   *
   * y行获取,必须在[0,获得())
   * 行一个可选的预先分配数组。如果null或太小,它将被忽略。
   *总是使用返回的对象,忽视了。数组的长度。
   *数组包含亮度数据。
   *
   * @param y The row to fetch, which must be in [0,getHeight())
   * @param row An optional preallocated array. If null or too small, it will be ignored.
   *            Always use the returned object, and ignore the .length of the array.
   * @return An array containing the luminance data.
   */
  public abstract byte[] getRow(int y, byte[] row);

  /**
   * Fetches luminance data for the underlying bitmap. Values should be fetched using:
   * {@code int luminance = array[y * width + x] & 0xff}
   *获取亮度数据底层的位图。值应该获取使用:
   * { @code int亮度= array[y *宽度+ x]& 0 xff }
   *
   * @return行二维数组的亮度值。不使用的结果。它可能是长度
   *大于宽*高字节在一些平台上。不要修改的内容吗
   *的结果。
   *
   * @return A row-major 2D array of luminance values. Do not use result.length as it may be
   *         larger than width * height bytes on some platforms. Do not modify the contents
   *         of the result.
   */
  public abstract byte[] getMatrix();

  /**
   * @return The width of the bitmap.
   */
  public final int getWidth() {

    Log.e("xyc","LuminanceSource  + getWidth = width = "+width);
    return width;
  }

  /**
   * @return The height of the bitmap.
   */
  public final int getHeight() {
    return height;
  }

  /**
   * @return Whether this subclass supports cropping.
   */
  public boolean isCropSupported() {
    return false;
  }

  /**
   * Returns a new object with cropped image data. Implementations may keep a reference to the
   * original data rather than a copy. Only callable if isCropSupported() is true.
   *返回一个新对象与裁剪图像数据。可以保持一个参考实现
   *原始数据而不是一个副本。只有可调用isCropSupported()是正确的。
   *
   * @param左左坐标,必须在[0,getWidth())
   * @param前顶部坐标,必须在[0,获得())
   * @param宽度矩形的宽度。
   * @param身高矩形的高的作物。
   * @return裁剪版本的这个对象。
   * @param left The left coordinate, which must be in [0,getWidth())
   * @param top The top coordinate, which must be in [0,getHeight())
   * @param width The width of the rectangle to crop.
   * @param height The height of the rectangle to crop.
   * @return A cropped version of this object.
   */
  public LuminanceSource crop(int left, int top, int width, int height) {
    throw new UnsupportedOperationException("This luminance source does not support cropping.");
  }

  /**这个子类是否支持逆时针旋转
   * @return Whether this subclass supports counter-clockwise rotation.
   */
  public boolean isRotateSupported() {
    return false;
  }

  /**
   * @return a wrapper of this {@code LuminanceSource} which inverts the luminances it returns -- black becomes
   *  white and vice versa, and each value becomes (255-value).
   *
   *  一个包装{ @code LuminanceSource }反转光泽它返回——黑色
   *白色,反之亦然,每个值(255 -值)。
   */
  public LuminanceSource invert() {
    return new InvertedLuminanceSource(this);
  }

  /**
   * Returns a new object with rotated image data by 90 degrees counterclockwise.
   * Only callable if {@link #isRotateSupported()} is true.
   * 返回一个新对象由90度逆时针旋转后的图像数据。
   *只调用如果{ @link # isRotateSupported()}是正确的。
   *
   * @return A rotated version of this object.
   */
  public LuminanceSource rotateCounterClockwise() {
    throw new UnsupportedOperationException("This luminance source does not support rotation by 90 degrees.");
  }

  /**
   * Returns a new object with rotated image data by 45 degrees counterclockwise.
   * Only callable if {@link #isRotateSupported()} is true.
   *
   * 返回一个新对象由45度逆时针旋转后的图像数据。
   *只调用如果{ @link # isRotateSupported()}是正确的。
   * @return A rotated version of this object.
   */
  public LuminanceSource rotateCounterClockwise45() {
    throw new UnsupportedOperationException("This luminance source does not support rotation by 45 degrees.");
  }

  @Override
  public final String toString() {
    byte[] row = new byte[width];
    StringBuilder result = new StringBuilder(height * (width + 1));
    for (int y = 0; y < height; y++) {
      row = getRow(y, row);
      for (int x = 0; x < width; x++) {
        int luminance = row[x] & 0xFF;
        char c;
        if (luminance < 0x40) {
          c = '#';
        } else if (luminance < 0x80) {
          c = '+';
        } else if (luminance < 0xC0) {
          c = '.';
        } else {
          c = ' ';
        }
        result.append(c);
      }
      result.append('\n');
    }

    Log.e("xyc ","LuminanceSource =="+result.toString());
    return result.toString();
  }

}
