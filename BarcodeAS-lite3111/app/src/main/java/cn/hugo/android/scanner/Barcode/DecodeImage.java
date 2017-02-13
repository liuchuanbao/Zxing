//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.hugo.android.scanner.Barcode;


import cn.hugo.android.scanner.Barcode.ResultObject.decode_16x16_data_Result;
import cn.hugo.android.scanner.Barcode.ResultObject.decode_20x20_data_Result;

public class DecodeImage {
    public DecodeImage() {
    }

    public static decode_16x16_data_Result DecodeSmallImage(byte[] szValue) {
        return Decode.decode_16x16_data(szValue);
    }

    public static decode_20x20_data_Result DecodeLargeImage(byte[] szValue) {
        return Decode.decode_20x20_data(szValue);
    }
}
