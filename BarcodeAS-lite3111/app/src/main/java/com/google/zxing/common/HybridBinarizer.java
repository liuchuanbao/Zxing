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
 * This class implements a local thresholding algorithm, which while slower than the
 * GlobalHistogramBinarizer, is fairly efficient for what it does. It is designed for
 * high frequency images of barcodes with black data on white backgrounds. For this application,
 * it does a much better job than a global blackpoint with severe shadows and gradients.
 * However it tends to produce artifacts on lower frequency images and is therefore not
 * a good general purpose binarizer for uses outside ZXing.
 *
 * This class extends GlobalHistogramBinarizer, using the older histogram approach for 1D readers,
 * and the newer local approach for 2D readers. 1D decoding using a per-row histogram is already
 * inherently local, and only fails for horizontal gradients. We can revisit that problem later,
 * but for now it was not a win to use local blocks for 1D.
 *
 * This Binarizer is the default for the unit tests and the recommended class for library users.
 *
 * 这个类实现了一个局部阈值算法,虽然比慢
 * GlobalHistogramBinarizer,相当有效。它是专为
 *高频图像的条形码用黑色白色背景数据。对于这个应用程序,
 *它更好的工作比一个全球blackpoint严重的阴影和梯度。
 *但是它倾向于产生低频图像,因此没有工件
 *好通用binarizer zx外使用。
 *
 *这个类继承了GlobalHistogramBinarizer,使用旧的直方图方法1 d的读者,
 *和2 d的更新本地方法的读者。使用进行直方图已经1 d解码
 *固有的地方,只有失败的水平梯度。我们可以晚些时候重新考虑这一问题,
 *但是现在这不是赢得使用本地块1 d。
 *
 *这Binarizer是默认的单元测试和推荐的类库用户。
 *
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class HybridBinarizer extends GlobalHistogramBinarizer {

  // This class uses 5x5 blocks to compute local luminance, where each block is 8x8 pixels.
  // So this is the smallest dimension in each axis we can accept.
  /**
   * 这类使用5 x5块来计算局部亮度,其中每个块8×8像素。
   / /这是最小的尺寸在每个轴我们可以接受。
   */
  private static final int BLOCK_SIZE_POWER = 3;
  private static final int BLOCK_SIZE = 1 << BLOCK_SIZE_POWER; // ...0100...00
  private static final int BLOCK_SIZE_MASK = BLOCK_SIZE - 1;   // ...0011...11
  private static final int MINIMUM_DIMENSION = BLOCK_SIZE * 5;
  private static final int MIN_DYNAMIC_RANGE = 24;

  private BitMatrix matrix;

  public HybridBinarizer(LuminanceSource source) {
    super(source);
  }

  /**
   * Calculates the final BitMatrix once for all requests. This could be called once from the
   * constructor instead, but there are some advantages to doing it lazily, such as making
   * profiling easier, and not doing heavy lifting when callers don't expect it.
   * 计算最后BitMatrix一劳永逸地请求。从这可以称为一次
   *构造方法相反,但也有一些优势,懒洋洋地,等
   *分析更容易,不做繁重当调用者不要指望它。
   *
   */
  @Override
  public BitMatrix getBlackMatrix() throws NotFoundException {
    if (matrix != null) {
      Log.e("xyc","HybridBinarizer  = getBlackMatrix =="+matrix.toString());
      return matrix;
    }

//    他的类层次结构提供了一组方法亮度数据转换为1位数据。算法可以以多态方式各不相同
    LuminanceSource source = getLuminanceSource();
    int width = source.getWidth();
    int height = source.getHeight();
    if (width >= MINIMUM_DIMENSION && height >= MINIMUM_DIMENSION) {

      //获取矩阵数组
      byte[] luminances = source.getMatrix();
      Log.e("xyc","HybriBinarizer +getBlackMatrix" +luminances.toString());

      int subWidth = width >> BLOCK_SIZE_POWER;
      if ((width & BLOCK_SIZE_MASK) != 0) {
        subWidth++;
      }
      int subHeight = height >> BLOCK_SIZE_POWER;
      if ((height & BLOCK_SIZE_MASK) != 0) {
        subHeight++;
      }

//      计算一个黑色的点为每个像素块并保存它 看到下面的这个算法的讨论线程
      int[][] blackPoints = calculateBlackPoints(luminances, subWidth, subHeight, width, height);

//      是一个二维矩阵的碎片。下面的函数参数,整个常见 模块,x是列位置,y是一行的位置。订购总是x,y。原点在左上角。< / p >

      BitMatrix newMatrix = new BitMatrix(width, height);

//      每一块的图像,计算出平均黑色点使用一个5 x5网格的街区。也处理边界情况(部分块计算的基础在最后一行/列的像素也使用在前面的块)。
      calculateThresholdForBlock(luminances, subWidth, subHeight, width, height, blackPoints, newMatrix);

      matrix = newMatrix;
    } else {
      // If the image is too small, fall back to the global histogram approach.如果图像太小,回到全局直方图的方法。
      matrix = super.getBlackMatrix();
    }
    Log.e("xyc","HybridBinarizer  = getBlackMatrix  2 ="+matrix.toString());
    return matrix;
  }

  @Override
  public Binarizer createBinarizer(LuminanceSource source) {
    return new HybridBinarizer(source);
  }

  /**
   * 每一块的图像,计算出平均黑色点使用一个5 x5网格
   *的街区。也处理边界情况(部分块计算的基础
   *在最后一行/列的像素也使用在前面的块)。
   * For each block in the image, calculate the average black point using a 5x5 grid
   * of the blocks around it. Also handles the corner cases (fractional blocks are computed based
   * on the last pixels in the row/column which are also used in the previous block).
   */
  private static void calculateThresholdForBlock(byte[] luminances,
                                                 int subWidth,
                                                 int subHeight,
                                                 int width,
                                                 int height,
                                                 int[][] blackPoints,
                                                 BitMatrix matrix) {
    for (int y = 0; y < subHeight; y++) {
      int yoffset = y << BLOCK_SIZE_POWER;
      int maxYOffset = height - BLOCK_SIZE;
      if (yoffset > maxYOffset) {
        yoffset = maxYOffset;
      }
      for (int x = 0; x < subWidth; x++) {
        int xoffset = x << BLOCK_SIZE_POWER;
        int maxXOffset = width - BLOCK_SIZE;
        if (xoffset > maxXOffset) {
          xoffset = maxXOffset;
        }
        int left = cap(x, 2, subWidth - 3);
        int top = cap(y, 2, subHeight - 3);
        int sum = 0;
        for (int z = -2; z <= 2; z++) {
          int[] blackRow = blackPoints[top + z];
          sum += blackRow[left - 2] + blackRow[left - 1] + blackRow[left] + blackRow[left + 1] + blackRow[left + 2];
        }
        int average = sum / 25;
        thresholdBlock(luminances, xoffset, yoffset, average, width, matrix);
      }
    }
  }

  private static int cap(int value, int min, int max) {
    return value < min ? min : value > max ? max : value;
  }

  /**
   * Applies a single threshold to a block of pixels.
   * 单一阈值适用于一块像素。
   */
  private static void thresholdBlock(byte[] luminances,
                                     int xoffset,
                                     int yoffset,
                                     int threshold,
                                     int stride,
                                     BitMatrix matrix) {
    for (int y = 0, offset = yoffset * stride + xoffset; y < BLOCK_SIZE; y++, offset += stride) {
      for (int x = 0; x < BLOCK_SIZE; x++) {
        // Comparison needs to be <= so that black == 0 pixels are black even if the threshold is 0.
//        / /比较需要< =这黑= = 0像素是黑人即使阈值是0。
        if ((luminances[offset + x] & 0xFF) <= threshold) {
//         Log.e("xyc","HYbridBinarizer  thresholdBlock = "+xoffset + x+"  yoffset + y "+yoffset + y);
          matrix.set(xoffset + x, yoffset + y);
        }
      }
    }
  }

  /**
   * Calculates a single black point for each block of pixels and saves it away.
   * See the following thread for a discussion of this algorithm:
   *
   * 计算一个黑色的点为每个像素块并保存它。
   *看到下面的这个算法的讨论线程
   *  http://groups.google.com/group/zxing/browse_thread/thread/d06efa2c35a7ddc0
   */
  private static int[][] calculateBlackPoints(byte[] luminances,
                                              int subWidth,
                                              int subHeight,
                                              int width,
                                              int height) {
    int[][] blackPoints = new int[subHeight][subWidth];
    for (int y = 0; y < subHeight; y++) {
      int yoffset = y << BLOCK_SIZE_POWER;
      int maxYOffset = height - BLOCK_SIZE;
      if (yoffset > maxYOffset) {
        yoffset = maxYOffset;
      }
      for (int x = 0; x < subWidth; x++) {
        int xoffset = x << BLOCK_SIZE_POWER;
        int maxXOffset = width - BLOCK_SIZE;
        if (xoffset > maxXOffset) {
          xoffset = maxXOffset;
        }
        int sum = 0;
        int min = 0xFF;
        int max = 0;
        for (int yy = 0, offset = yoffset * width + xoffset; yy < BLOCK_SIZE; yy++, offset += width) {
          for (int xx = 0; xx < BLOCK_SIZE; xx++) {
            int pixel = luminances[offset + xx] & 0xFF;
            sum += pixel;
            // still looking for good contrast
            if (pixel < min) {
              min = pixel;
            }
            if (pixel > max) {
              max = pixel;
            }
          }
          // short-circuit min/max tests once dynamic range is met
          if (max - min > MIN_DYNAMIC_RANGE) {
            // finish the rest of the rows quickly
            for (yy++, offset += width; yy < BLOCK_SIZE; yy++, offset += width) {
              for (int xx = 0; xx < BLOCK_SIZE; xx++) {
                sum += luminances[offset + xx] & 0xFF;
              }
            }
          }
        }

        // The default estimate is the average of the values in the block.
        int average = sum >> (BLOCK_SIZE_POWER * 2);
        if (max - min <= MIN_DYNAMIC_RANGE) {
          // If variation within the block is low, assume this is a block with only light or only
          // dark pixels. In that case we do not want to use the average, as it would divide this
          // low contrast area into black and white pixels, essentially creating data out of noise.
          //
          // The default assumption is that the block is light/background. Since no estimate for
          // the level of dark pixels exists locally, use half the min for the block.
          average = min / 2;

          if (y > 0 && x > 0) {
            // Correct the "white background" assumption for blocks that have neighbors by comparing
            // the pixels in this block to the previously calculated black points. This is based on
            // the fact that dark barcode symbology is always surrounded by some amount of light
            // background for which reasonable black point estimates were made. The bp estimated at
            // the boundaries is used for the interior.

            // The (min < bp) is arbitrary but works better than other heuristics that were tried.
            int averageNeighborBlackPoint =
                (blackPoints[y - 1][x] + (2 * blackPoints[y][x - 1]) + blackPoints[y - 1][x - 1]) / 4;
            if (min < averageNeighborBlackPoint) {
              average = averageNeighborBlackPoint;
            }
          }
        }
        blackPoints[y][x] = average;
      }
    }
    return blackPoints;
  }

}
