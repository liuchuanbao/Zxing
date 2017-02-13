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

package com.google.zxing.common;

import android.util.Log;

import com.google.zxing.Binarizer;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;

/**
 * This Binarizer implementation uses the old ZXing global histogram approach. It is suitable
 * for low-end mobile devices which don't have enough CPU or memory to use a local thresholding
 * algorithm. However, because it picks a global black point, it cannot handle difficult shadows
 * and gradients.
 *这个Binarizer实现使用旧的zx全局直方图的方法。它是合适的
 *对低端手机,没有足够的CPU或内存使用当地的阈值
 *算法。然而,因为它选择一个全球黑一点,它不能处理困难的阴影
 *和梯度。
 *
 *更快的移动设备和桌面应用程序应该使用所有HybridBinarizer代替
 * Faster mobile devices and all desktop applications should probably use HybridBinarizer instead.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public class GlobalHistogramBinarizer extends Binarizer {

  private static final int LUMINANCE_BITS = 5;
  private static final int LUMINANCE_SHIFT = 8 - LUMINANCE_BITS;
  private static final int LUMINANCE_BUCKETS = 1 << LUMINANCE_BITS;
  private static final byte[] EMPTY = new byte[0];

  private byte[] luminances;
  private final int[] buckets;

  public GlobalHistogramBinarizer(LuminanceSource source) {
    super(source);
    luminances = EMPTY;
    buckets = new int[LUMINANCE_BUCKETS];
  }

  // Applies simple sharpening to the row data to improve performance of the 1D Readers.
  //简单的锐化适用于一维的行数据来提高性能的读者  第五步
  @Override
  public BitArray getBlackRow(int y, BitArray row) throws NotFoundException {

      /**
       * LuminanceSource  只提供不变的方法,因此作物和旋转创建副本。这是为了确保
       *一个读者不修改原来的亮度源和把它在一个未知的状态 (生成一个矩阵)
       *
       */
    LuminanceSource source = getLuminanceSource();
    int width = source.getWidth();
    if (row == null || row.getSize() < width) {
      row = new BitArray(width);
    } else {
      row.clear();
    }

    initArrays(width);

    byte[] localLuminances = source.getRow(y, luminances);
    int[] localBuckets = buckets;

    Log.e("xyc","GlobalHistogramBinarizer   =  initArrays  buckets = "+buckets.toString());
    for (int x = 0; x < width; x++) {
      int pixel = localLuminances[x] & 0xff;
      localBuckets[pixel >> LUMINANCE_SHIFT]++;
    }
//      计算计算阈值
    int blackPoint = estimateBlackPoint(localBuckets);
    Log.e("xyc","GlobalHistogramBinarizer   =  blackPoint  blackPoint = "+blackPoint);

    int left = localLuminances[0] & 0xff;
    int center = localLuminances[1] & 0xff;
    for (int x = 1; x < width - 1; x++) {
      int right = localLuminances[x + 1] & 0xff;
      // A simple -1 4 -1 box filter with a weight of 2.
      int luminance = ((center * 4) - left - right) / 2;
      if (luminance < blackPoint) {
        row.set(x);
      }
      left = center;
      center = right;
    }

    Log.e("xyc","GlobalHistogramBinarizer +=getBlackRow= 2row = "+row.toString());
    return row;
  }

  // Does not sharpen the data, as this call is intended to only be used by 2D Readers.
  @Override
  public BitMatrix getBlackMatrix() throws NotFoundException {
    LuminanceSource source = getLuminanceSource();
    int width = source.getWidth();
    int height = source.getHeight();
    BitMatrix matrix = new BitMatrix(width, height);

    // Quickly calculates the histogram by sampling four rows from the image. This proved to be
    // more robust on the blackbox tests than sampling a diagonal as we used to do.
    //快速计算直方图的抽样四行形象。这被证明是
            //更健壮的黑盒测试我们用来做比采样一个对角。
    initArrays(width);
    int[] localBuckets = buckets;
    for (int y = 1; y < 5; y++) {
      int row = height * y / 5;
      byte[] localLuminances = source.getRow(row, luminances);
      int right = (width * 4) / 5;
      for (int x = width / 5; x < right; x++) {
        int pixel = localLuminances[x] & 0xff;
        localBuckets[pixel >> LUMINANCE_SHIFT]++;
      }
    }
    int blackPoint = estimateBlackPoint(localBuckets);
/**
 * 我们延迟阅读整个图像亮度直到黑色的点估计成功。
 / /虽然我们最终阅读四行两次,这是符合我们的座右铭
 / /快速“失败”,连续扫描是必要的。
 */
    // We delay reading the entire image luminance until the black point estimation succeeds.
    // Although we end up reading four rows twice, it is consistent with our motto of
    // "fail quickly" which is necessary for continuous scanning.
    byte[] localLuminances = source.getMatrix();
    for (int y = 0; y < height; y++) {
      int offset = y * width;
      for (int x = 0; x< width; x++) {
        int pixel = localLuminances[offset + x] & 0xff;
        if (pixel < blackPoint) {
          matrix.set(x, y);
        }
      }
    }
    Log.e("xyc","GlobalHistogramBinarizer +=getBlackMatrix=1 matrix = "+matrix.toString());
    return matrix;
  }

  @Override
  public Binarizer createBinarizer(LuminanceSource source) {
    return new GlobalHistogramBinarizer(source);
  }

  private void initArrays(int luminanceSize) {
    if (luminances.length < luminanceSize) {
      luminances = new byte[luminanceSize];
    }
    for (int x = 0; x < LUMINANCE_BUCKETS; x++) {
      buckets[x] = 0;
//      Log.e("xyc","GlobalHistogramBinarizer initArrays buckets[x] = "+buckets[x]);
    }
  }

  /**
   *计算阈值
   * @param buckets
   * @return
   * @throws NotFoundException
     */
  private static int estimateBlackPoint(int[] buckets) throws NotFoundException {
    // Find the tallest peak in the histogram.
    int numBuckets = buckets.length;
    int maxBucketCount = 0;
    int firstPeak = 0;
    int firstPeakSize = 0;

    //找到  数组中最多像素点的个数(maxBucketCount)  和  对应的 序号,序号其实就是亮度值(区间)firstPeak.
        //这里的 firstPeak 即可能是  黑色的区间,也可能是白色的区间.
    for (int x = 0; x < numBuckets; x++) {
      if (buckets[x] > firstPeakSize) {
        firstPeak = x;
        firstPeakSize = buckets[x];
      }
      if (buckets[x] > maxBucketCount) {
        maxBucketCount = buckets[x];
      }
    }

    // Find the second-tallest peak which is somewhat far from the tallest peak.
//    找到有点远的第二峰最高峰。
    int secondPeak = 0;
    int secondPeakScore = 0;
    for (int x = 0; x < numBuckets; x++) {
      int distanceToBiggest = x - firstPeak;
      // Encourage more distant second peaks by multiplying by square of distance.
      int score = buckets[x] * distanceToBiggest * distanceToBiggest;
      if (score > secondPeakScore) {
        secondPeak = x;
        secondPeakScore = score;
      }
    }

    // Make sure firstPeak corresponds to the black peak.
//    确保firstPeak对应黑峰。
    if (firstPeak > secondPeak) {
      int temp = firstPeak;
      firstPeak = secondPeak;
      secondPeak = temp;
    }

    // If there is too little contrast in the image to pick a meaningful black point, throw rather
    // than waste time trying to decode the image, and risk false positives.
//    如果有太少的对比图中选择一个有意义的黑色点,扔
    //比浪费时间试图解码图像,和风险假阳性。
    if (secondPeak - firstPeak <= numBuckets / 16) {
      throw NotFoundException.getNotFoundInstance();
    }

    // Find a valley between them that is low and closer to the white peak.
    int bestValley = secondPeak - 1;
    int bestValleyScore = -1;
    for (int x = secondPeak - 1; x > firstPeak; x--) {
      int fromFirst = x - firstPeak;
      //这里找 到两个峰比较远,同时又点少的区间.这里  fromFist 采用平方的形式,可以理解更接近于白色.
      int score = fromFirst * fromFirst * (secondPeak - x) * (maxBucketCount - buckets[x]);
//      Log.e("xyc","GlobalHistogramBinarizer =score +  "+score);
      if (score > bestValleyScore) {
        bestValley = x;
        bestValleyScore = score;
      }
    }
//      Log.e("xyc","GlobalHistogramBinarizer =estimateBlackPoint +  "+bestValley);
    return bestValley << LUMINANCE_SHIFT;
  }

}
