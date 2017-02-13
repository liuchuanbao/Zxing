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

import java.util.Map;

/**
 * Implementations of this interface can decode an image of a barcode in some format into
 * the String it encodes. For example, {@link com.google.zxing.qrcode.QRCodeReader} can
 * decode a QR code. The decoder may optionally receive hints from the caller which may help
 * it decode more quickly or accurately.
 *
 * See {@link com.google.zxing.MultiFormatReader}, which attempts to determine what barcode
 * format is present within the image as well, and then decodes it accordingly.
 *
 * 这个接口的实现可以解码图像条码的一些格式
 *编码的字符串。例如,{ @link com.google.zxing.qrcode。QRCodeReader }可以
 *解码一个二维码。解码器可能会选择性地接收来自调用者的暗示这可能帮助
 *它更快地解码或准确。
 *
 *
 *
 *
 * @author Sean Owen
 * @author dswitkin@google.com (Daniel Switkin)
 */
public interface Reader {

  /**
   * Locates and decodes a barcode in some format within an image.
   *定位和解码中的一些格式的条形码图像。
   *
//   * @param条码解码的图像形象
//   * @return条码编码的字符串
//   * @throw NotFoundException如果没有找到潜在的条形码
//   * @throws ChecksumException如果找到潜在的条形码,但没有通过校验和
//   * @throws FormatException如果找到潜在的条形码格式是无效的

   * @param image image of barcode to decode
   * @return String which the barcode encodes
   * @throws NotFoundException if no potential barcode is found
   * @throws ChecksumException if a potential barcode is found but does not pass its checksum
   * @throws FormatException if a potential barcode is found but format is invalid
   */
  Result decode(BinaryBitmap image) throws NotFoundException, ChecksumException, FormatException;

  /**
   * Locates and decodes a barcode in some format within an image. This method also accepts
   * hints, each possibly associated to some data, which may help the implementation decode.
   *定位和解码中的一些格式的条形码图像。这种方法还接受
   *提示,每一个可能相关的一些数据,这可能有助于实现解码。
   *
//   * @param条码解码的图像形象
//   * @param提示作为{传递@link java.util。地图}与{ @link com.google.zxing.DecodeHintType }
   *任意数据。的
   *数据的意义取决于提示类型。可能或不可能实现
   *任何与这些提示。
   * @return条码编码的字符串
   * @param image image of barcode to decode
   * @param hints passed as a {@link java.util.Map} from {@link com.google.zxing.DecodeHintType}
   * to arbitrary data. The
   * meaning of the data depends upon the hint type. The implementation may or may not do
   * anything with these hints.
   * @return String which the barcode encodes
   * @throws NotFoundException if no potential barcode is found
   * @throws ChecksumException if a potential barcode is found but does not pass its checksum
   * @throws FormatException if a potential barcode is found but format is invalid
   */
  Result decode(BinaryBitmap image, Map<DecodeHintType,?> hints)
      throws NotFoundException, ChecksumException, FormatException;

  /**
   * Resets any internal state the implementation has after a decode, to prepare it
   * for reuse.
   * 重置任何内部状态实现解码后,准备它
   *重用
   */
  void reset();

}