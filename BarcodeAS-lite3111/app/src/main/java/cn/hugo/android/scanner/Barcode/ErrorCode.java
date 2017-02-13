//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.hugo.android.scanner.Barcode;

public enum ErrorCode {
    ImageTypeError(-8888),
    ValueOutRegion(-8887),
    NotProcess(-8886),
    HigherVersion(-8885);

    private final int val;

    private ErrorCode(int value) {
        this.val = value;
    }

    public int getValue() {
        return this.val;
    }
}
