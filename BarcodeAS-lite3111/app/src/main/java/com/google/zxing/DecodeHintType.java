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

import java.util.List;

/**
 * Encapsulates a type of hint that a caller may pass to a barcode reader to help it
 * more quickly or accurately decode it. It is up to implementations to decide what,
 * if anything, to do with the information that is supplied.
 *封装的一种暗示,调用者可以通过条形码阅读器来帮助它
 *更快或准确解码。它是由实现决定什么,
 *如果有的话,与提供的信息。
 * @author Sean Owen
 * @author dswitkin@google.com (Daniel Switkin)
 * @see Reader#decode(BinaryBitmap,java.util.Map)
 */
public enum DecodeHintType {

  /**
   * Unspecified, application-specific hint. Maps to an unspecified {@link Object}.
   *
   * 未指明的,特定于应用程序的提示。映射到对象} { @link不详。
   */
  OTHER(Object.class),

  /**
   * Image is a pure monochrome image of a barcode. Doesn't matter what it maps to;
   * use {@link Boolean#TRUE}.
   *
   * 条码的图像是一个纯粹的黑白图像。无所谓什么地图;
   *使用{ @link布尔#真正的}。
   */
  PURE_BARCODE(Void.class),

  /**
   * Image is known to be of one of a few possible formats.
   * Maps to a {@link List} of {@link BarcodeFormat}s.
   * 图片是一些可能的格式之一。
   *地图的
   */
  POSSIBLE_FORMATS(List.class),

  /**
   * Spend more time to try to find a barcode; optimize for accuracy, not speed.
   * Doesn't matter what it maps to; use {@link Boolean#TRUE}.
   * 花更多的时间试图找到一个条形码;优化精度,而不是速度。
   *无所谓什么地图;
   */
  TRY_HARDER(Void.class),

  /**
   * Specifies what character encoding to use when decoding, where applicable (type String)
   * 指定字符编码解码时使用,适用(String类型)
   */
  CHARACTER_SET(String.class),

  /**
   * Allowed lengths of encoded data -- reject anything else. Maps to an {@code int[]}.
   * 允许的长度编码数据——拒绝一切。映射到一个{
   */
  ALLOWED_LENGTHS(int[].class),

  /**
   * Assume Code 39 codes employ a check digit. Doesn't matter what it maps to;
   * 假定代码39码使用校验位。无所谓什么地图;
   * use {@link Boolean#TRUE}.
   */
  ASSUME_CODE_39_CHECK_DIGIT(Void.class),

  /**
   * Assume the barcode is being processed as a GS1 barcode, and modify behavior as needed.
   * For example this affects FNC1 handling for Code 128 (aka GS1-128). Doesn't matter what it maps to;
   * use {@link Boolean#TRUE}.
   * 假设在处理条形码GS1条形码,并根据需要修改行为。
   * 128(例如这个影响FNC1处理代码(又名gs1 - 128)。无所谓什么地图;
   */
  ASSUME_GS1(Void.class),

  /**
   * If true, return the start and end digits in a Codabar barcode instead of stripping them. They
   * are alpha, whereas the rest are numeric. By default, they are stripped, but this causes them
   * to not be. Doesn't matter what it maps to; use {@link Boolean#TRUE}.
   * 如果这是真的,返回的开始和结束位Codabar条形码而不是剥夺他们。他们
   *是α,而其余的都是数字。默认情况下,它们剥夺了,但这将导致它们
   *不。无所谓什么地图;使用{ @link布尔#真正的}。
   *
   */
  RETURN_CODABAR_START_END(Void.class),

  /**
   * The caller needs to be notified via callback when a possible {@link ResultPoint}
   * is found. Maps to a {@link ResultPointCallback}.
   * 调用者通过回调时可能需要通知{ @link ResultPoint }
   *。映射到一个{ @link ResultPointCallback }。
   */
  NEED_RESULT_POINT_CALLBACK(ResultPointCallback.class),


  /**
   * Allowed extension lengths for EAN or UPC barcodes. Other formats will ignore this.
   * Maps to an {@code int[]} of the allowed extension lengths, for example [2], [5], or [2, 5].
   * If it is optional to have an extension, do not set this hint. If this is set,
   * and a UPC or EAN barcode is found but an extension is not, then no result will be returned
   * at all.
   * 允许扩展长度EAN或UPC条形码。其他格式将忽略这一点。
   *地图允许的{ @code int[]}扩展长度,例如[2],[5]或[2、5]。
   *如果它是可选的扩展,不设置这个提示。如果这是集,
   *和UPC或但扩展不是发现EAN条形码,然后没有结果返回
   *
   */
  ALLOWED_EAN_EXTENSIONS(int[].class),

  // End of enumeration values.
  ;

  /**
   * Data type the hint is expecting.
   * Among the possible values the {@link Void} stands out as being used for
   * hints that do not expect a value to be supplied (flag hints). Such hints
   * will possibly have their value ignored, or replaced by a
   * {@link Boolean#TRUE}. Hint suppliers should probably use
   * {@link Boolean#TRUE} as directed by the actual hint documentation.
   * 数据类型提示预计。
   *在可能的值{ @link空白}作为用于脱颖而出
   *暗示不期望一个值(标志提示)提供。这样的提示
   *可能会有自己的价值被忽视,或所取代
   * { @link布尔#真正的}。提示供应商应该使用
   * { @link布尔#真正的},由实际提示文档。
   *
   */
  private final Class<?> valueType;

  DecodeHintType(Class<?> valueType) {
    this.valueType = valueType;
  }
  
  public Class<?> getValueType() {
    return valueType;
  }

}
