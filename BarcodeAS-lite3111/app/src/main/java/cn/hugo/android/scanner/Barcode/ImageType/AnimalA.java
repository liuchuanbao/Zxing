//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.hugo.android.scanner.Barcode.ImageType;

import java.io.UnsupportedEncodingException;

public class AnimalA {
    public int Version;
    public int RegionCode;
    public int RegionSerial;
    public int MarkRegion;
    public int BadgeNumber;
    public String Farmer;
    public byte[] notusefield1 = new byte[9];
    public int AnimalType;
    public int Unit;
    public int Amount;
    public int Purpose;
    public int PeriodOfValidity;
    public int QuarantinerID;
    public int QuarantineOrgID;
    public int Year;
    public int Month;
    public int Day;
    public int TownRegionID;
    public String CDJYZZCode;
    public byte[] notusefield = new byte[50];

    public AnimalA() {
    }

    public String getFarmer() {
        int index;
        for(index = 0; index < 9 && this.notusefield1[index] != 0; ++index) {
            ;
        }

        byte[] bytes = new byte[index];

        for(int e = 0; e < index; ++e) {
            bytes[e] = this.notusefield1[e];
        }

        try {
            String var5 = new String(bytes, "gb2312");
            return var5;
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
            return new String();
        }
    }

    public void setFarmer(String Farmer) {
        try {
            byte[] e = Farmer.getBytes("gb2312");
            int bc = e.length;
            int rc = this.notusefield1.length;
            if(bc < rc) {
                rc = bc;
            }

            for(int i = 0; i < rc; ++i) {
                this.notusefield1[i] = e[i];
            }

            this.Farmer = Farmer;
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }

    }

    public boolean Check() {
        return this.RegionSerial >= 0 && this.RegionSerial <= 262143?(this.MarkRegion >= 0 && this.MarkRegion <= 131071?(this.BadgeNumber >= 0 && this.BadgeNumber <= 16383?(this.AnimalType >= 0 && this.AnimalType <= 15?(this.Unit >= 0 && this.Unit <= 7?(this.Amount >= 0 && this.Amount <= 1023?(this.Purpose >= 0 && this.Purpose <= 7?(this.PeriodOfValidity >= 0 && this.PeriodOfValidity <= 15?(this.QuarantinerID >= 0 && this.QuarantinerID <= 1048575?(this.QuarantineOrgID >= 0 && this.QuarantineOrgID <= 262143?(this.Year >= 0 && this.Year <= 2036?(this.Month >= 0 && this.Month <= 12?(this.Day >= 0 && this.Day <= 31?this.TownRegionID >= 0 && this.TownRegionID <= 262143:false):false):false):false):false):false):false):false):false):false):false):false):false;
    }

    public byte[] SetImage() {
        boolean[] bitarray = new boolean[224];
        char[] BitValue = new char[8];

        int i;
        for(i = 0; i < 224; ++i) {
            bitarray[i] = false;
        }

        this.ConvertIntToBitArray(bitarray, 0, 4, 2);
        this.ConvertIntToBitArray(bitarray, 4, 4, 3);
        this.ConvertIntToBitArray(bitarray, 8, 18, this.RegionSerial);
        this.ConvertIntToBitArray(bitarray, 26, 17, this.MarkRegion);
        this.ConvertIntToBitArray(bitarray, 43, 14, this.BadgeNumber);

        for(i = 0; i < 8; ++i) {
            for(int j = 0; j < 8; ++j) {
                int index = i * 8 + j;
                if((this.notusefield1[i] & BitValue[j]) != 0) {
                    bitarray[index + 57] = true;
                }
            }
        }

        this.ConvertIntToBitArray(bitarray, 121, 4, this.AnimalType);
        this.ConvertIntToBitArray(bitarray, 125, 3, this.Unit);
        this.ConvertIntToBitArray(bitarray, 128, 10, this.Amount);
        this.ConvertIntToBitArray(bitarray, 138, 3, this.Purpose);
        this.ConvertIntToBitArray(bitarray, 141, 4, this.PeriodOfValidity);
        this.ConvertIntToBitArray(bitarray, 145, 20, this.QuarantinerID);
        this.ConvertIntToBitArray(bitarray, 165, 18, this.QuarantineOrgID);
        this.ConvertIntToBitArray(bitarray, 183, 5, this.Year - 2000);
        this.ConvertIntToBitArray(bitarray, 188, 4, this.Month);
        this.ConvertIntToBitArray(bitarray, 192, 5, this.Day);
        this.ConvertIntToBitArray(bitarray, 197, 18, this.TownRegionID);
        return this.ConvertBitArrayToCharArray(bitarray, 28);
    }

    void ConvertIntToBitArray(boolean[] bitArray, int pindex, int count, int value) {
        if(count > 0) {
            boolean[] pArray = new boolean[count];

            int index;
            for(index = 0; index < count; ++index) {
                pArray[index] = false;
            }

            for(index = count - 1; value > 0 && index >= 0; value /= 2) {
                if(value % 2 == 0) {
                    pArray[index--] = false;
                } else {
                    pArray[index--] = true;
                }
            }

            for(int i = pindex; i < pindex + count; ++i) {
                bitArray[i] = pArray[i - pindex];
            }

        }
    }

    private byte[] ConvertBitArrayToCharArray(boolean[] bitArray, int count) {
        byte[] pBuf = new byte[32];

        for(int i = 0; i < count; ++i) {
            pBuf[i] = this.ConvertBitArrayToInt(bitArray, i * 8, 8);
        }

        return pBuf;
    }

    private byte ConvertBitArrayToInt(boolean[] bitArray, int index, int count) {
        int bitValue = 1;
        byte resultValue = 0;

        for(int i = index + count - 1; i >= index; --i) {
            if(bitArray[i]) {
                resultValue = (byte)(resultValue + bitValue);
            }

            bitValue *= 2;
        }

        return resultValue;
    }
}
