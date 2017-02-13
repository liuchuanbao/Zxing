//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.hugo.android.scanner.Barcode;


import cn.hugo.android.scanner.Barcode.ImageType.AnimalA;
import cn.hugo.android.scanner.Barcode.ImageType.EarMark;
import cn.hugo.android.scanner.Barcode.ImageType.ImageType;
import cn.hugo.android.scanner.Barcode.ResultObject.DecodeImageData_Result;
import cn.hugo.android.scanner.Barcode.ResultObject.DecodeLargeImageData_Result;
import cn.hugo.android.scanner.Barcode.ResultObject.decode_16x16_data_Result;
import cn.hugo.android.scanner.Barcode.ResultObject.decode_20x20_data_Result;

public class Barcode {
    public Barcode() {
    }

    public static byte[] EncodeImage(Object lpStruct, int ImageType) {
        byte[] Result32 = new byte[32];
        byte[] Result400 = new byte[400];
        switch(ImageType) {
        case 3: /*MotoScanManager.STATE_SNAP_PREVIEW*/
            Result32 = EncodeAAImage(lpStruct);
        default:
            encode_2020(Result400, Result32);
            return Result400;
        }
    }

    private static byte[] EncodeAAImage(Object lpStruct) {
        byte[] Result = new byte[32];
        AnimalA AA = (AnimalA)lpStruct;
        if(AA.Check()) {
            Result = AA.SetImage();
        }

        return Result;
    }

    private static byte[] encode_2020(byte[] Result400, byte[] Result32) {
        return Result400;
    }

    public static DecodeImageData_Result DecodeImageData(byte[] pData, Object lpStruct, int p_imageType) {
        DecodeImageData_Result DR = new DecodeImageData_Result();
        DR.Result = ErrorCode.NotProcess.getValue();
        if(p_imageType == ImageType.EarMarkImage.getValue()) {
            decode_16x16_data_Result r = DecodeImage.DecodeSmallImage(pData);
            if(r.Result == 0) {
                DR.Result = r.Result;
                DR.EM = DecodeEarMarkStruct(r.pOut, (EarMark)lpStruct);
            } else {
                DR.Result = r.Result;
            }
        }

        return DR;
    }

    public static DecodeLargeImageData_Result DecodeLargeImageData(byte[] pData, Object lpStruct, int p_imageType) {
        DecodeLargeImageData_Result DR = new DecodeLargeImageData_Result();
        DR.Result = ErrorCode.NotProcess.getValue();
        if(p_imageType == ImageType.AAImage.getValue()) {
            decode_20x20_data_Result r = DecodeImage.DecodeLargeImage(pData);
            if(r.Result == 0) {
                DR.Result = r.Result;
                DR.R = DecodeAnimalAStruct(r.pOut, (AnimalA)lpStruct);
            } else {
                DR.Result = r.Result;
            }
        }

        return DR;
    }

    private static Object DecodeAnimalAStruct(byte[] szValue, AnimalA pAnimalA) {
        boolean[] bitResult = new boolean[224];
        bitResult = ConvertCharArrayToBitArray(szValue, 28);
        pAnimalA.Version = 2;
        switch(pAnimalA.Version) {
        case 2: /*MotoScanManager.STATE_HANDSFREE*/
            DecodeAnimalA(pAnimalA, bitResult);
        case 1:
        default:
            return pAnimalA;
        }
    }

    static EarMark DecodeEarMarkStruct(byte[] szValue, EarMark pEarMark) {
        boolean[] bitResult = new boolean[56];
        bitResult = ConvertCharArrayToBitArray(szValue, 7);
        pEarMark.Version = ConvertBitArrayToInt(bitResult, 2, 4);
        switch(pEarMark.Version) {
        case 2: /*MotoScanManager.STATE_HANDSFREE*/
            DecodeEarMarkVersion2(pEarMark, bitResult);
        case 1:
        default:
            return pEarMark;
        }
    }

    static boolean[] ConvertCharArrayToBitArray(byte[] pBuf, int count) {
        boolean[] bitResult = new boolean[56];
        byte[] BitValue = new byte[]{(byte)-128, (byte)64, (byte)32, (byte)16, (byte)8, (byte)4, (byte)2, (byte)1};

        for(int i = 0; i < count; ++i) {
            for(int j = 0; j < 8; ++j) {
                if((pBuf[i] & BitValue[j]) == 0) {
                    bitResult[i * 8 + j] = false;
                } else {
                    bitResult[i * 8 + j] = true;
                }
            }
        }

        return bitResult;
    }

    static int ConvertBitArrayToInt(boolean[] bitArray, int index, int count) {
        int bitValue = 1;
        int resultValue = 0;

        for(int i = index + count - 1; i >= index; --i) {
            if(bitArray[i]) {
                resultValue += bitValue;
            }

            bitValue *= 2;
        }

        return resultValue;
    }

    static void DecodeEarMarkVersion2(EarMark pEarMark, boolean[] bitValue) {
        pEarMark.AnimalType = ConvertBitArrayToInt(bitValue, 10, 4);
        pEarMark.RegionSerial = ConvertBitArrayToInt(bitValue, 14, 14);
        pEarMark.EarMarkNumber = ConvertBitArrayToInt(bitValue, 28, 28);
    }

    static void DecodeAnimalA(AnimalA pAnimalA, boolean[] bitValue) {
        pAnimalA.RegionSerial = ConvertBitArrayToInt(bitValue, 8, 18);
        pAnimalA.MarkRegion = ConvertBitArrayToInt(bitValue, 26, 17);
        pAnimalA.BadgeNumber = ConvertBitArrayToInt(bitValue, 43, 14);

        for(int i = 0; i < 64; ++i) {
            int index = i + 57;
            boolean j = false;
            if(i > 7) {
                int var6 = i / 8;
            }

            int k = i % 8;
            if(bitValue[index]) {
                pAnimalA.Farmer = pAnimalA.Farmer + bitValue[k];
            }
        }

        pAnimalA.AnimalType = ConvertBitArrayToInt(bitValue, 121, 4);
        pAnimalA.Unit = ConvertBitArrayToInt(bitValue, 125, 3);
        pAnimalA.Amount = ConvertBitArrayToInt(bitValue, 128, 10);
        pAnimalA.Purpose = ConvertBitArrayToInt(bitValue, 138, 3);
        pAnimalA.PeriodOfValidity = ConvertBitArrayToInt(bitValue, 141, 4);
        pAnimalA.QuarantinerID = ConvertBitArrayToInt(bitValue, 145, 20);
        pAnimalA.QuarantineOrgID = ConvertBitArrayToInt(bitValue, 165, 18);
        pAnimalA.Year = ConvertBitArrayToInt(bitValue, 183, 5) + 2000;
        pAnimalA.Month = ConvertBitArrayToInt(bitValue, 188, 4);
        pAnimalA.Day = ConvertBitArrayToInt(bitValue, 192, 5);
        pAnimalA.TownRegionID = ConvertBitArrayToInt(bitValue, 197, 18);
    }
}
