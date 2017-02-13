//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.hugo.android.scanner.Barcode.ImageType;

public enum ImageType {
    EarMarkImage(0),
    PackageImage(1),
    BoxImage(2),
    AAImage(3),
    ABImage(4),
    PAImage(5),
    PBImage(6),
    XDZImage(7);

    private final int val;

    private ImageType(int value) {
        this.val = value;
    }

    public int getValue() {
        return this.val;
    }
}
