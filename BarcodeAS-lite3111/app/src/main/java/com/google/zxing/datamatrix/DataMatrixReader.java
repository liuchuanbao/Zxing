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

package com.google.zxing.datamatrix;

import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.datamatrix.decoder.Decoder;
import com.google.zxing.datamatrix.detector.Detector;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import cn.hugo.android.scanner.Barcode.Barcode;
import cn.hugo.android.scanner.Barcode.ImageType.EarMark;
import cn.hugo.android.scanner.Barcode.ImageType.ImageType;
import cn.hugo.android.scanner.Barcode.ResultObject.DecodeImageData_Result;

/**
 * This implementation can detect and decode Data Matrix codes in an image.
 *这个实现可以检测和解码数据矩阵代码在一个图像。
 * @author bbrown@google.com (Brian Brown)
 */
public final class DataMatrixReader implements Reader {

  private static final ResultPoint[] NO_POINTS = new ResultPoint[0];

  private final Decoder decoder = new Decoder();

  /**
   * Locates and decodes a Data Matrix code in an image.
   *
   * 定位和解码图像数据矩阵代码。
   *
   * @return表示内容的字符串编码的数据矩阵代码
   * @t NotFoundException如果一个数据矩阵代码不能被发现
   * @ FormatException如果一个数据矩阵代码不能解码
   * @t ChecksumException如果纠错失败
   *
   * @return a String representing the content encoded by the Data Matrix code
   * @throws NotFoundException if a Data Matrix code cannot be found
   * @throws FormatException if a Data Matrix code cannot be decoded
   * @throws ChecksumException if error correction fails
   */
  @Override
  public Result decode(BinaryBitmap image) throws NotFoundException, ChecksumException, FormatException {
    Result res= decode(image, null);
    Log.e("xyc","DataMatrixReader  decode 1="+res.toString());
    return decode(image, null);
  }

  @Override
  public Result decode(BinaryBitmap image, Map<DecodeHintType,?> hints)
      throws NotFoundException, ChecksumException, FormatException {
    DecoderResult decoderResult;
    ResultPoint[] points;
    if (hints != null && hints.containsKey(DecodeHintType.PURE_BARCODE)) {
      BitMatrix bits = extractPureBits(image.getBlackMatrix());


      decoderResult = decoder.decode(bits);
      points = NO_POINTS;
    } else {
      DetectorResult detectorResult = new Detector(image.getBlackMatrix()).detect();

//TODO 解码
// TODO: 2016/10/19
//      String strDataMatrix = detectorResult.getBits().toString("1,","0,",3);
//      Log.e("xyc1","strDataMatrix==" + strDataMatrix);
//      if(strDataMatrix.length()>0) {
//        String[] str = strDataMatrix.split(",");
//
//        //todo 错了
//        byte[] matrixs = new byte[str.length];
//        for (int i = 0; i < str.length; i++) {
//          matrixs[i] = (byte) Integer.parseInt(str[i]);
//        }
//
////      byte[] matrixs = new byte[]{
////              0,0,0,1,0,1,0,1,1,0,1,0,0,1,1,0,1,0,1,1,1,0,1,1,0,1,0,0,0,0,0,1,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,1,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,1,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,1,0,0,1,0,1,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,1,1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1,1,0,0,1,0,1,0,1,0,0,1,0,1,0,0,1,0,0,1,1,1,1,1,0,0,1,0,1,0,0,0,0,0,0,1,1,0,0,1,0,1,1,0,1,0,0,1,0,0,0,0,0,0,0,0,0,
////      };
//        Log.e("xyc1", "matrix==" + matrixs.length);
////      String t = new String(matrix);
////      Log.e("xyc1","strDataMatrix=2=" + t);
//        EarMark pigEar = new EarMark();
//        DecodeImageData_Result imageRst = Barcode.DecodeImageData(matrixs, pigEar, ImageType.EarMarkImage.getValue());
//        //输出结果
//        if (imageRst.Result == 0) {
//          Log.e("xyc1", "AnimalType:" + imageRst.EM.AnimalType);
//          Log.e("xyc1", "Region:" + imageRst.EM.RegionSerial);
//          Log.e("xyc1", "EarMarkNum:" + imageRst.EM.EarMarkNumber);
////        System.out.println("AnimalType:" + imageRst.EM.AnimalType);
////        System.out.println("Region:" + imageRst.EM.RegionSerial);
////        System.out.println("EarMarkNum:" + imageRst.EM.EarMarkNumber);
//        } else {
////        System.out.println("Decode Pig's EarMark Failed!");
//          Log.e("xyc1", "Decode Pig's EarMark Failed!");
//        }
//      }
   decoderResult = decoder.decode(detectorResult.getBits());

      points = detectorResult.getPoints();
    }
    Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points,
        BarcodeFormat.DATA_MATRIX);
    List<byte[]> byteSegments = decoderResult.getByteSegments();
    if (byteSegments != null) {
      result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
    }
    String ecLevel = decoderResult.getECLevel();
    if (ecLevel != null) {
      result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, ecLevel);
    }

    Result res= decode(image, null);
    Log.e("xyc","DataMatrixReader  decode 2="+res.toString());
    return result;
  }

  @Override
  public void reset() {
    // do nothing
  }

  /**
   * This method detects a code in a "pure" image -- that is, pure monochrome image
   * which contains only an unrotated, unskewed, image of a code, with some white border
   * around it. This is a specialized method that works exceptionally fast in this special
   * case.该方法检测到一个代码“纯洁”的形象——也就是说,纯粹的黑白图像
   *它只包含一个不旋转,unskewed、图像的代码,有一些白色的边境
   *。这是一个专业的方法,在这个特殊的特别快
   *
   * @see com.google.zxing.qrcode.QRCodeReader#extractPureBits(BitMatrix)
   */
  private static BitMatrix extractPureBits(BitMatrix image) throws NotFoundException {

    int[] leftTopBlack = image.getTopLeftOnBit();
    int[] rightBottomBlack = image.getBottomRightOnBit();
    if (leftTopBlack == null || rightBottomBlack == null) {
      throw NotFoundException.getNotFoundInstance();
    }

    int moduleSize = moduleSize(leftTopBlack, image);

    int top = leftTopBlack[1];
    int bottom = rightBottomBlack[1];
    int left = leftTopBlack[0];
    int right = rightBottomBlack[0];

    int matrixWidth = (right - left + 1) / moduleSize;
    int matrixHeight = (bottom - top + 1) / moduleSize;
    if (matrixWidth <= 0 || matrixHeight <= 0) {
      throw NotFoundException.getNotFoundInstance();
    }

    // Push in the "border" by half the module width so that we start
    // sampling in the middle of the module. Just in case the image is a
    // little off, this will help recover.
    int nudge = moduleSize / 2;
    top += nudge;
    left += nudge;

    // Now just read off the bits
    BitMatrix bits = new BitMatrix(matrixWidth, matrixHeight);
    for (int y = 0; y < matrixHeight; y++) {
      int iOffset = top + y * moduleSize;
      for (int x = 0; x < matrixWidth; x++) {
        if (image.get(left + x * moduleSize, iOffset)) {
          bits.set(x, y);
        }
      }
    }
    return bits;
  }

  private static int moduleSize(int[] leftTopBlack, BitMatrix image) throws NotFoundException {
    int width = image.getWidth();
    int x = leftTopBlack[0];
    int y = leftTopBlack[1];
    while (x < width && image.get(x, y)) {
      x++;
    }
    if (x == width) {
      throw NotFoundException.getNotFoundInstance();
    }

    int moduleSize = x - leftTopBlack[0];
    if (moduleSize == 0) {
      throw NotFoundException.getNotFoundInstance();
    }
    return moduleSize;
  }

}