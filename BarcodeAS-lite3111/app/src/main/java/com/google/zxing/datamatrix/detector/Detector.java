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

package com.google.zxing.datamatrix.detector;

import android.util.Log;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.detector.MathUtils;
import com.google.zxing.common.detector.WhiteRectangleDetector;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hugo.android.scanner.Barcode.Barcode;
import cn.hugo.android.scanner.Barcode.ImageType.EarMark;
import cn.hugo.android.scanner.Barcode.ImageType.ImageType;
import cn.hugo.android.scanner.Barcode.ResultObject.DecodeImageData_Result;

/**
 * <p>Encapsulates logic that can detect a Data Matrix Code in an image, even if the Data Matrix Code
 * is rotated or skewed, or partially obscured.</p>
 * >封装逻辑可以检测图像数据矩阵代码,即使数据矩阵代码
 * 旋转或倾斜,或部分模糊。< / p >
 *
 * @author Sean Owen
 */
public final class Detector {

    private final BitMatrix image;
    private final WhiteRectangleDetector rectangleDetector;

    /**
     * 、大致划出符号图像的位置
     *
     * @param image
     * @throws NotFoundException
     */
    public Detector(BitMatrix image) throws NotFoundException {
        this.image = image;
        /**
         * vWhiteRectangleDetector（在/core/com/google/zxing/common/detector中）就是用来做这项工作的。它的方法是从图像中间画一个30X30的框，
         * 然后依次向四边推行，检测四边上是否有黑色的点，直到每一边都没有黑色的点为止
         */
        rectangleDetector = new WhiteRectangleDetector(image);
    }

    /**
     * <p>Detects a Data Matrix Code in an image.</p>
     * 检测图像数据矩阵代码
     *
     * @return {@link DetectorResult} encapsulating results of detecting a Data Matrix Code
     * @throws NotFoundException if no Data Matrix Code can be found
     */
    public DetectorResult detect() throws NotFoundException {
        //rectangleDetector,它决定了条形码的角落。
        ResultPoint[] cornerPoints = rectangleDetector.detect();
        ResultPoint pointA = cornerPoints[0];
        ResultPoint pointB = cornerPoints[1];
        ResultPoint pointC = cornerPoints[2];
        ResultPoint pointD = cornerPoints[3];


        // Point A and D are across the diagonal from one another,
        // as are B and C. Figure out which are the solid black lines
        // by counting transitions
/**
 * / /点A和D是对角线对面,
 / / B和c,找出哪些是坚实的黑色线条
 / /通过计算转换
 */

//  ResultPointsAndTransitions 转换的实体
        List<ResultPointsAndTransitions> transitions = new ArrayList<>(4);

        transitions.add(transitionsBetween(pointA, pointB));
        transitions.add(transitionsBetween(pointA, pointC));
        transitions.add(transitionsBetween(pointB, pointD));
        transitions.add(transitionsBetween(pointC, pointD));

        Collections.sort(transitions, new ResultPointsAndTransitionsComparator());

        // Sort by number of transitions. First two will be the two solid sides; last two
        // will be the two alternating black/white sides 通过转换的数量排序，前两个是实线，后两个是虚线

        //TODO
        ResultPointsAndTransitions lSideOne = transitions.get(0);
        ResultPointsAndTransitions lSideTwo = transitions.get(1);

        // Figure out which point is their intersection by tallying up the number of times we see the
//    找出哪些点是他们的交集计算我们看到的次数
        // endpoints in the four endpoints. One will show up twice. v四个端点的端点。个会出现两次。
        Map<ResultPoint, Integer> pointCount = new HashMap<>();
        increment(pointCount, lSideOne.getFrom());
        increment(pointCount, lSideOne.getTo());
        increment(pointCount, lSideTwo.getFrom());
        increment(pointCount, lSideTwo.getTo());

        ResultPoint maybeTopLeft = null;
        ResultPoint bottomLeft = null;
        ResultPoint maybeBottomRight = null;


        // 确定一个黑L；
        for (Map.Entry<ResultPoint, Integer> entry : pointCount.entrySet()) {
            ResultPoint point = entry.getKey();
            Integer value = entry.getValue();
            if (value == 2) {
                bottomLeft = point; // this is definitely the bottom left, then -- end of two L sides 这绝对是左下角,然后——L的双方
            } else {
                // Otherwise it's either top left or bottom right -- just assign the two arbitrarily now 否则它的左上角或右下角,只是分配两个任意现在
                if (maybeTopLeft == null) {
                    maybeTopLeft = point; //左上点
                } else {
                    maybeBottomRight = point; //右下点
                }
            }
        }

        if (maybeTopLeft == null || bottomLeft == null || maybeBottomRight == null) {//抛出条形码不存在
            throw NotFoundException.getNotFoundInstance();
        }

        // Bottom left is correct but top left and bottom right might be switched 左下角是正确的但左上角和右下角切换
        ResultPoint[] corners = {maybeTopLeft, bottomLeft, maybeBottomRight};
        // Use the dot product trick to sort them out 使用点积方法对它们进行排序
        ResultPoint.orderBestPatterns(corners);

        // Now we know which is which:  确定好的L的三个坐标点
        ResultPoint bottomRight = corners[0];
        bottomLeft = corners[1];
        ResultPoint topLeft = corners[2];

        // Which point didn't we find in relation to the "L" sides? that's the top right corner //寻找右上角
//    这一点我们没有找到与“L”国吗?右上角

        ResultPoint topRight;
        if (!pointCount.containsKey(pointA)) {
            topRight = pointA;
        } else if (!pointCount.containsKey(pointB)) {
            topRight = pointB;
        } else if (!pointCount.containsKey(pointC)) {
            topRight = pointC;
        } else {
            topRight = pointD;
        }

        // Next determine the dimension by tracing along the top or right side and counting black/white
        // transitions. Since we start inside a black module, we should see a number of transitions
        // equal to 1 less than the code dimension. Well, actually 2 less, because we are going to
        // end on a black module:

        /**
         *
         * 下确定维度通过跟踪沿顶部或右侧和计数黑色/白色
         / /转换。自从我们开始在一个黑色的模块,我们应该看到一个数量的转换
         / / 1不到的代码维度。实际上2少,因为我们要
         / /结束在一个黑色的模块:
         / /右上角点实际上是一个模块的角落里,两个黑色的模块之一
         / /毗邻白色模块在右上角。跟踪从左上方角落
         / /或右下角应该在这里工作。

         // The top right point is actually the corner of a module, which is one of the two black modules
         // adjacent to the white module at the top right. Tracing to that corner from either the top left
         // or bottom right should work here.

         */


        // 计算detaMatrix的宽度
        int dimensionTop = transitionsBetween(topLeft, topRight).getTransitions();
        // 计算detaMatrix的高度
        int dimensionRight = transitionsBetween(bottomRight, topRight).getTransitions();

        if ((dimensionTop & 0x01) == 1) {
            // it can't be odd, so, round... up?
            dimensionTop++;
        }
        dimensionTop += 2;

        if ((dimensionRight & 0x01) == 1) {
            // it can't be odd, so, round... up?
            dimensionRight++;
        }
        dimensionRight += 2;

        BitMatrix bits;
        ResultPoint correctedTopRight;

        // Rectanguar symbols are 6x16, 6x28, 10x24, 10x32, 14x32, or 14x44. If one dimension is more
        // than twice the other, it's certainly rectangular, but to cut a bit more slack we accept it as
        // rectangular if the bigger side is at least 7/4 times the other:
        /**
         * Rectanguar符号6乘16 6 x28 10 x24,10 x32 14 x32,或14 x44。如果一个维度
         / /两倍,当然这是矩形,但削减更松弛我们接受它
         / /矩形如果更大的至少是7/4次对方:
         */
        // TODO: 2016/10/19  
//    if (4 * dimensionTop >= 7 * dimensionRight || 4 * dimensionRight >= 7 * dimensionTop) {
        // The matrix is rectangular矩阵是长方形的

//      correctedTopRight =
//          correctTopRightRectangular(bottomLeft, bottomRight, topLeft, topRight, dimensionTop, dimensionRight);
//      if (correctedTopRight == null){
//        correctedTopRight = topRight;
//      }
//
//      dimensionTop = transitionsBetween(topLeft, correctedTopRight).getTransitions();
//      dimensionRight = transitionsBetween(bottomRight, correctedTopRight).getTransitions();
//
//      if ((dimensionTop & 0x01) == 1) {
//        // it can't be odd, so, round... up?
//        dimensionTop++;
//      }
//
//      if ((dimensionRight & 0x01) == 1) {
//        // it can't be odd, so, round... up?
//        dimensionRight++;
//      }
//
//      /**
//       *
//       *
//       *SampleGrid进行透视变换和采样变换，将原始图像中的符号图像纠正、变换为我们解码需要的规则的，以模块为单位的符号矩阵
//       */
//
//      Log.e("xyc","Detecor = sampleGrid topLeft="+topLeft);
//      Log.e("xyc","Detecor = sampleGrid bottomLeft="+bottomLeft);
//      Log.e("xyc","Detecor = sampleGrid bottomRight="+bottomRight);
//      Log.e("xyc","Detecor = sampleGrid correctedTopRight="+correctedTopRight);
//      Log.e("xyc","Detecor = sampleGrid dimensionTop="+dimensionTop);
//      Log.e("xyc","Detecor = sampleGrid dimensionRight="+dimensionRight);
//      Log.e("xyc","Detecor = sampleGrid image="+image.toString());
//      Log.e("xyc","Detecor = sampleGrid image="+image.toString("1","0"));
//      bits = sampleGrid(image, topLeft, bottomLeft, bottomRight, correctedTopRight, dimensionTop, dimensionRight);
//      Log.e("xyc","Detecor = sampleGrid1 ="+bits.toString("1","0"));
//      Log.e("xyc","Detecor = sampleGrid1 ="+bits.toString());


//    } else {
        // The matrix is square矩阵是广场
        // TODO: 2016/10/19
        int dimension = Math.min(dimensionRight, dimensionTop);
        // correct top right point to match the white module 正确的右上方的点匹配白色模块
        correctedTopRight = correctTopRight(bottomLeft, bottomRight, topLeft, topRight, dimension);
        if (correctedTopRight == null) {
            correctedTopRight = topRight;
        }

        // Redetermine the dimension using the corrected top right point 再决定使用修正的维度右上方的点
        // TODO: 2016/10/19
//      int dimensionCorrected = Math.max(transitionsBetween(topLeft, correctedTopRight).getTransitions(),
//                                transitionsBetween(bottomRight, correctedTopRight).getTransitions());
//      dimensionCorrected++;
//      if ((dimensionCorrected & 0x01) == 1) {
//        dimensionCorrected++;
//      }

//      bits = sampleGrid(image,
//                        topLeft,
//                        bottomLeft,
//                        bottomRight,
//                        correctedTopRight,
//                        dimensionCorrected,
//                        dimensionCorrected);
        bits = sampleGrid(image,
                topLeft,
                bottomLeft,
                bottomRight,
                correctedTopRight,
                18,
                18);

        Log.e("xyc", "Detecor = sampleGrid22222222 =" + bits.toString("1,", "0,"));
        Log.e("xyc", "Detecor = sampleGrid22222222 =" + bits.toString());

        String strDataMatrix = bits.toString("1,", "0,", 3);
        Log.e("xyc1", "strDataMatrix==" + strDataMatrix);
        if (strDataMatrix.length() > 0) {
            String[] str = strDataMatrix.split(",");
            //todo 错了
            byte[] matrixs = new byte[str.length];
            for (int i = 0; i < str.length; i++) {
                matrixs[i] = (byte) Integer.parseInt(str[i]);
            }
//      byte[] matrixs = new byte[]{
//              0,0,0,1,0,1,0,1,1,0,1,0,0,1,1,0,1,0,1,1,1,0,1,1,0,1,0,0,0,0,0,1,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,1,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,1,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,1,0,0,1,0,1,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,1,1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1,1,0,0,1,0,1,0,1,0,0,1,0,1,0,0,1,0,0,1,1,1,1,1,0,0,1,0,1,0,0,0,0,0,0,1,1,0,0,1,0,1,1,0,1,0,0,1,0,0,0,0,0,0,0,0,0,
//      };
            Log.e("xyc1", "matrix==" + matrixs.length);
            EarMark pigEar = new EarMark();
            //todo  想办法把选择的畜种名字传过来
            DecodeImageData_Result imageRst = Barcode.DecodeImageData(matrixs, pigEar, ImageType.EarMarkImage.getValue());
            //输出结果
            if (imageRst.Result == 0) {
                //发送线程
                EventBus.getDefault().post(imageRst);

                Log.e("xyc1", "AnimalType:" + imageRst.EM.AnimalType);
                Log.e("xyc1", "Region:" + imageRst.EM.RegionSerial);
                Log.e("xyc1", "EarMarkNum:" + imageRst.EM.EarMarkNumber);
//        System.out.println("AnimalType:" + imageRst.EM.AnimalType);
//        System.out.println("Region:" + imageRst.EM.RegionSerial);
//        System.out.println("EarMarkNum:" + imageRst.EM.EarMarkNumber);
            } else {
//        System.out.println("Decode Pig's EarMark Failed!");
                Log.e("xyc1", "Decode Pig's EarMark Failed!");
            }
//      }
        }

        return new DetectorResult(bits, new ResultPoint[]{topLeft, bottomLeft, bottomRight, correctedTopRight});
    }

    /**
     * Calculates the position of the white top right module using the output of the rectangle detector
     * for a rectangular matrix
     */
    private ResultPoint correctTopRightRectangular(ResultPoint bottomLeft,
                                                   ResultPoint bottomRight,
                                                   ResultPoint topLeft,
                                                   ResultPoint topRight,
                                                   int dimensionTop,
                                                   int dimensionRight) {

        float corr = distance(bottomLeft, bottomRight) / (float) dimensionTop;
        int norm = distance(topLeft, topRight);
        float cos = (topRight.getX() - topLeft.getX()) / norm;
        float sin = (topRight.getY() - topLeft.getY()) / norm;

        ResultPoint c1 = new ResultPoint(topRight.getX() + corr * cos, topRight.getY() + corr * sin);

        corr = distance(bottomLeft, topLeft) / (float) dimensionRight;
        norm = distance(bottomRight, topRight);
        cos = (topRight.getX() - bottomRight.getX()) / norm;
        sin = (topRight.getY() - bottomRight.getY()) / norm;

        ResultPoint c2 = new ResultPoint(topRight.getX() + corr * cos, topRight.getY() + corr * sin);

        if (!isValid(c1)) {
            if (isValid(c2)) {
                return c2;
            }
            return null;
        }
        if (!isValid(c2)) {
            return c1;
        }

        int l1 = Math.abs(dimensionTop - transitionsBetween(topLeft, c1).getTransitions()) +
                Math.abs(dimensionRight - transitionsBetween(bottomRight, c1).getTransitions());
        int l2 = Math.abs(dimensionTop - transitionsBetween(topLeft, c2).getTransitions()) +
                Math.abs(dimensionRight - transitionsBetween(bottomRight, c2).getTransitions());

        if (l1 <= l2) {
            return c1;
        }

        return c2;
    }

    /**
     * Calculates the position of the white top right module using the output of the rectangle detector
     * for a square matrix
     */
    private ResultPoint correctTopRight(ResultPoint bottomLeft,
                                        ResultPoint bottomRight,
                                        ResultPoint topLeft,
                                        ResultPoint topRight,
                                        int dimension) {

        float corr = distance(bottomLeft, bottomRight) / (float) dimension;
        int norm = distance(topLeft, topRight);
        float cos = (topRight.getX() - topLeft.getX()) / norm;
        float sin = (topRight.getY() - topLeft.getY()) / norm;

        ResultPoint c1 = new ResultPoint(topRight.getX() + corr * cos, topRight.getY() + corr * sin);

        corr = distance(bottomLeft, topLeft) / (float) dimension;
        norm = distance(bottomRight, topRight);
        cos = (topRight.getX() - bottomRight.getX()) / norm;
        sin = (topRight.getY() - bottomRight.getY()) / norm;

        ResultPoint c2 = new ResultPoint(topRight.getX() + corr * cos, topRight.getY() + corr * sin);

        if (!isValid(c1)) {
            if (isValid(c2)) {
                return c2;
            }
            return null;
        }
        if (!isValid(c2)) {
            return c1;
        }

        int l1 = Math.abs(transitionsBetween(topLeft, c1).getTransitions() -
                transitionsBetween(bottomRight, c1).getTransitions());
        int l2 = Math.abs(transitionsBetween(topLeft, c2).getTransitions() -
                transitionsBetween(bottomRight, c2).getTransitions());

        return l1 <= l2 ? c1 : c2;
    }

    private boolean isValid(ResultPoint p) {
        return p.getX() >= 0 && p.getX() < image.getWidth() && p.getY() > 0 && p.getY() < image.getHeight();
    }

    private static int distance(ResultPoint a, ResultPoint b) {
        return MathUtils.round(ResultPoint.distance(a, b));
    }

    /**
     * Increments the Integer associated with a key by one.
     * 增量的整数的一个关键
     */
    private static void increment(Map<ResultPoint, Integer> table, ResultPoint key) {
        Integer value = table.get(key);
        table.put(key, value == null ? 1 : value + 1);
    }

    private static BitMatrix sampleGrid(BitMatrix image,
                                        ResultPoint topLeft,
                                        ResultPoint bottomLeft,
                                        ResultPoint bottomRight,
                                        ResultPoint topRight,
                                        int dimensionX,
                                        int dimensionY) throws NotFoundException {

        GridSampler sampler = GridSampler.getInstance();

        return sampler.sampleGrid(image,
                dimensionX,
                dimensionY,
                0.5f,
                0.5f,
                dimensionX - 0.5f,
                0.5f,
                dimensionX - 0.5f,
                dimensionY - 0.5f,
                0.5f,
                dimensionY - 0.5f,
                topLeft.getX(),
                topLeft.getY(),
                topRight.getX(),
                topRight.getY(),
                bottomRight.getX(),
                bottomRight.getY(),
                bottomLeft.getX(),
                bottomLeft.getY());
    }

    /**
     * Counts the number of black/white transitions between two points, using something like Bresenham's algorithm.
     * 计数的黑/白两个点之间的转换,使用类似Bresenham算法。
     */
    private ResultPointsAndTransitions transitionsBetween(ResultPoint from, ResultPoint to) {
        // See QR Code Detector, sizeOfBlackWhiteBlackRun()
        int fromX = (int) from.getX();
        int fromY = (int) from.getY();
        int toX = (int) to.getX();
        int toY = (int) to.getY();
        //判断y增量和x的增量的大小
        boolean steep = Math.abs(toY - fromY) > Math.abs(toX - fromX);
        if (steep) {
            int temp = fromX;
            fromX = fromY;
            fromY = temp;
            temp = toX;
            toX = toY;
            toY = temp;
        }

        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);
        int error = -dx / 2;
        int ystep = fromY < toY ? 1 : -1;
        int xstep = fromX < toX ? 1 : -1;
        int transitions = 0;
        boolean inBlack = image.get(steep ? fromY : fromX, steep ? fromX : fromY);
        for (int x = fromX, y = fromY; x != toX; x += xstep) {
            boolean isBlack = image.get(steep ? y : x, steep ? x : y);
            if (isBlack != inBlack) {
                transitions++;
                inBlack = isBlack;
            }
            error += dy;
            if (error > 0) {
                if (y == toY) {
                    break;
                }
                y += ystep;
                error -= dx;
            }
        }
        return new ResultPointsAndTransitions(from, to, transitions);
    }

    /**
     * Simply encapsulates two points and a number of transitions between them.
     * 只是封装了两个点和它们之间的转换。
     */
    private static final class ResultPointsAndTransitions {

        private final ResultPoint from;
        private final ResultPoint to;
        private final int transitions;

        private ResultPointsAndTransitions(ResultPoint from, ResultPoint to, int transitions) {
            this.from = from;
            this.to = to;
            this.transitions = transitions;
        }

        ResultPoint getFrom() {
            return from;
        }

        ResultPoint getTo() {
            return to;
        }

        public int getTransitions() {
            return transitions;
        }

        @Override
        public String toString() {
            return from + "/" + to + '/' + transitions;
        }
    }

    /**
     * Orders ResultPointsAndTransitions by number of transitions, ascending.
     * 订单 ResultPointsAndTransitions转换的数量,提升
     */
    private static final class ResultPointsAndTransitionsComparator
            implements Comparator<ResultPointsAndTransitions>, Serializable {
        @Override
        public int compare(ResultPointsAndTransitions o1, ResultPointsAndTransitions o2) {
            return o1.getTransitions() - o2.getTransitions();
        }
    }

}
