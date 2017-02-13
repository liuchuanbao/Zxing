//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.hugo.android.scanner.Barcode;

public enum tagErrorCode {
    UBAR_OK(0),
    UBAR_ERR_RS(1),
    UBAR_ERR_FileNotFound(2),
    UBAR_ERR_ImageNotSupport(3),
    UBAR_ERR_MemoryNotEnough(4),
    UBAR_ERR_NoBlocks(5),
    UBAR_ERR_NoSquare(6),
    UBAR_ERR_Angle(7),
    UBAR_ERR_Decode(8),
    UBAR_ERR_Hough(9),
    UBAR_ERR_Rotate(10),
    UBAR_ERR_Point(11),
    UBAR_ERR_Crc(12),
    UBAR_ERR_Main(13),
    UBAR_ERR_NotImgFile(14),
    UBAR_ERR_DJpg(15);

    private final int val;

    private tagErrorCode(int value) {
        this.val = value;
    }

    public int getValue() {
        return this.val;
    }
}
