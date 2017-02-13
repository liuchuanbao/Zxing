/*
 * Copyright 2008 ZXing authors
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

/**
 * Represents some type of metadata about the result of the decoding that the decoder
 * wishes to communicate back to the caller.
 *
 * 代表某种类型的元数据对解码器的解码的结果
 *希望沟通返回给调用者
 *
 * @author Sean Owen
 */
public enum ResultMetadataType {

  /**
   * Unspecified, application-specific metadata. Maps to an unspecified {@link Object}.
   * 未指明的,特定于应用程序的元数据
   */
  OTHER,

  /**
   * Denotes the likely approximate orientation of the barcode in the image. This value
   * is given as degrees rotated clockwise from the normal, upright orientation.
   * For example a 1D barcode which was found by reading top-to-bottom would be
   * said to have orientation "90". This key maps to an {@link Integer} whose
   * value is in the range [0,360).
   * 表示可能近似取向的条码图像。这个值
   *给出从正常度顺时针旋转,垂直方向。
   *例如1 d条形码由阅读全面发现
   *定位“90”。这个键映射到的{ @link整数}
   *值范围(0360)。
   *
   */
  ORIENTATION,

  /**
   * <p>2D barcode formats typically encode text, but allow for a sort of 'byte mode'
   * which is sometimes used to encode binary data. While {@link Result} makes available
   * the complete raw bytes in the barcode for these formats, it does not offer the bytes
   * from the byte segments alone.</p>
   *
   *  2 d条码格式通常编码的文本,但允许一种“字节模式”
   *有时被用来编码二进制数据。虽然} { @link结果是可用的
   *完整的原始字节的条形码格式,它不提供字节
   *从字节段孤独。< / p >
   *
   * < p >这地图{ @link java.util。}的字节数组对应列表
   *原始字节的字节段条形码,在秩序。< / p >
   *
   * <p>This maps to a {@link java.util.List} of byte arrays corresponding to the
   * raw bytes in the byte segments in the barcode, in order.</p>
   */
  BYTE_SEGMENTS,

  /**
   * Error correction level used, if applicable. The value type depends on the
   * format, but is typically a String.
   * 误差校正水平,如果适用的话。值类型取决于
   *格式,但通常是一个字符串。
   *
   */
  ERROR_CORRECTION_LEVEL,

  /**
   * For some periodicals, indicates the issue number as an {@link Integer}.
   * 对于一些期刊,表明问题数量作为
   *
   */
  ISSUE_NUMBER,

  /**
   * For some products, indicates the suggested retail price in the barcode as a
   * formatted {@link String}.
   * 对于某些产品,显示在条形码建议零售价格
   *格式化} { @link字符串。
   *
   */
  SUGGESTED_PRICE ,

  /**
   * For some products, the possible country of manufacture as a {@link String} denoting the
   * ISO country code. Some map to multiple possible countries, like "US/CA".
   * 对于某些产品,可能的国家生产{ @link字符串}表示
   * ISO国家代码。一些可能映射到多个国家,像“我们/ CA”。
   *
   */
  POSSIBLE_COUNTRY,

  /**
   * For some products, the extension text
   * 对于某些产品,扩展文本
   */
  UPC_EAN_EXTENSION,

  /**
   * PDF417-specific元数据PDF417-specific metadata
   *
   */
  PDF417_EXTRA_METADATA,

  /**
   * If the code format supports structured append and the current scanned code is part of one then the
   * sequence number is given with it.
   * 如果代码格式支持结构化的追加和当前的扫描代码是一个那么的一部分
   *给出序号。
   *
   */
  STRUCTURED_APPEND_SEQUENCE,

  /**
   * If the code format supports structured append and the current scanned code is part of one then the
   * parity is given with it.
   * 如果代码格式支持结构化的追加和当前的扫描代码是一个那么的一部分
   *平价了。
   *
   */
  STRUCTURED_APPEND_PARITY,
  
}
