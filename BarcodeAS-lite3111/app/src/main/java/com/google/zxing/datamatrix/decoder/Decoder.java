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

package com.google.zxing.datamatrix.decoder;

import android.util.Log;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.google.zxing.common.reedsolomon.ReedSolomonException;

import cn.hugo.android.scanner.Barcode.Barcode;
import cn.hugo.android.scanner.Barcode.ImageType.EarMark;
import cn.hugo.android.scanner.Barcode.ImageType.ImageType;
import cn.hugo.android.scanner.Barcode.ResultObject.DecodeImageData_Result;

/**
 * <p>The main class which implements Data Matrix Code decoding -- as opposed to locating and extracting
 * the Data Matrix Code from an image.</p>
 *< p >主类实现数据矩阵代码解码——而不是定位和提取
 *从图像数据矩阵代码。< / p >
 * @author bbrown@google.com (Brian Brown)
 */
public final class Decoder {

  private final ReedSolomonDecoder rsDecoder;

  public Decoder() {
    rsDecoder = new ReedSolomonDecoder(GenericGF.DATA_MATRIX_FIELD_256);
  }

  /**
   * <p>Convenience method that can decode a Data Matrix Code represented as a 2D array of booleans.
   * "true" is taken to mean a black module.</p>
   * >便利方法能够解码数据矩阵代码表示为一个二维数组的布尔值。
   *“真实”是指一个黑色模块。< / p >
   *
   * @param形象代表白色/黑色布尔值数据矩阵代码模块
   * @return文本数据矩阵代码和字节编码
   * FormatException如果数据矩阵代码不能
   * image booleans representing white/black Data Matrix Code modules
   *  text and bytes encoded within the Data Matrix Code
   * FormatException if the Data Matrix Code cannot be decoded
   *  ChecksumException if error correction fails
   *
   *
   */
  public DecoderResult decode(boolean[][] image) throws FormatException, ChecksumException {
    int dimension = image.length;
    BitMatrix bits = new BitMatrix(dimension);
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        if (image[i][j]) {
          bits.set(j, i);
        }
      }
    }
    return decode(bits);
  }

  /**
   * <p>Decodes a Data Matrix Code represented as a {@link BitMatrix}. A 1 or "true" is taken
   * to mean a black module.</p>
   *< p >解码数据矩阵代码表示为{ @link BitMatrix }。1或“真正的”
   *意味着黑人模块。< / p >
   *
   * 布尔值表示白色/黑色数据矩阵代码模块
   *文本数据矩阵代码和字节编码
   *  FormatException如果数据矩阵代码不能解码
    ChecksumException如果纠错失败
   * @param bits booleans representing white/black Data Matrix Code modules
   * @return text and bytes encoded within the Data Matrix Code
   * @throws FormatException if the Data Matrix Code cannot be decoded
   * @throws ChecksumException if error correction fails
   */
  public DecoderResult decode(BitMatrix bits) throws FormatException, ChecksumException {

    // Construct a parser and read version, error-correction level
    BitMatrixParser parser = new BitMatrixParser(bits);
    Version version = parser.getVersion();

    // Read codewords
    byte[] codewords = parser.readCodewords();
    // Separate into data blocks
    DataBlock[] dataBlocks = DataBlock.getDataBlocks(codewords, version);

    int dataBlocksCount = dataBlocks.length;

    // Count total number of data bytes
    int totalBytes = 0;
    for (DataBlock db : dataBlocks) {
      totalBytes += db.getNumDataCodewords();
    }
    byte[] resultBytes = new byte[totalBytes];

    // Error-correct and copy data blocks together into a stream of bytes
    for (int j = 0; j < dataBlocksCount; j++) {
      DataBlock dataBlock = dataBlocks[j];
      byte[] codewordBytes = dataBlock.getCodewords();
      int numDataCodewords = dataBlock.getNumDataCodewords();
      correctErrors(codewordBytes, numDataCodewords);
      for (int i = 01; i < numDataCodewords; i++) {
        // De-interlace data blocks.
        resultBytes[i * dataBlocksCount + j] = codewordBytes[i];
      }
    }
//todo  是乱码
//    String strDecoder = new String(resultBytes);
//    Log.e("xyc2","strDecoder =:" + strDecoder);
//    EarMark pigEar = new EarMark();
//    DecodeImageData_Result imageRst = Barcode.DecodeImageData(resultBytes, pigEar, ImageType.EarMarkImage.getValue() );
//    //输出结果
//    if(imageRst.Result==0){
//      Log.e("xyc2","AnimalType:" + imageRst.EM.AnimalType);
//      Log.e("xyc2","Region:" + imageRst.EM.RegionSerial);
//      Log.e("xyc2","EarMarkNum:" + imageRst.EM.EarMarkNumber);
////        System.out.println("AnimalType:" + imageRst.EM.AnimalType);
////        System.out.println("Region:" + imageRst.EM.RegionSerial);
////        System.out.println("EarMarkNum:" + imageRst.EM.EarMarkNumber);
//    } else {
////        System.out.println("Decode Pig's EarMark Failed!");
//      Log.e("xyc2","Decode Pig's EarMark Failed!");
//    }
    // Decode the contents of that stream of bytes
    return DecodedBitStreamParser.decode(resultBytes);
  }

  /**
   * <p>Given data and error-correction codewords received, possibly corrupted by errors, attempts to
   * correct the errors in-place using Reed-Solomon error correction.</p>
   *>收到数据和纠错码字,可能被错误,尝试
   *正确的错误就地使用Reed-Solomon纠错。< / p >
   * @param codewordBytes data and error correction codewords
   * @param numDataCodewords number of codewords that are data bytes
   * @throws ChecksumException if error correction fails
   */
  private void correctErrors(byte[] codewordBytes, int numDataCodewords) throws ChecksumException {
    int numCodewords = codewordBytes.length;
    // First read into an array of ints
    int[] codewordsInts = new int[numCodewords];
    for (int i = 0; i < numCodewords; i++) {
      codewordsInts[i] = codewordBytes[i] & 0xFF;
    }
    int numECCodewords = codewordBytes.length - numDataCodewords;
    try {
      rsDecoder.decode(codewordsInts, numECCodewords);
    } catch (ReedSolomonException ignored) {
      throw ChecksumException.getChecksumInstance();
    }
    // Copy back into array of bytes -- only need to worry about the bytes that were data
    // We don't care about errors in the error-correction codewords
    for (int i = 0; i < numDataCodewords; i++) {
      codewordBytes[i] = (byte) codewordsInts[i];
    }
  }

}
