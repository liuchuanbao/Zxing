//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.hugo.android.scanner.Barcode;



import java.math.BigInteger;

import cn.hugo.android.scanner.Barcode.ResultObject.RS_S;
import cn.hugo.android.scanner.Barcode.ResultObject.VL_S;
import cn.hugo.android.scanner.Barcode.ResultObject.decode_16x16_data_Result;
import cn.hugo.android.scanner.Barcode.ResultObject.decode_20x20_data_Result;
import cn.hugo.android.scanner.Barcode.ResultObject.ubar_decode_Result;

public class Decode {
    static boolean g_lInit = false;
    static Object g_hRS;
    static String N = "729bf6a30a9159";
    static int[] g_DecodeTable = new int[]{1, 16, 17, 32, 33, 2, 3, 18, 19, 34, 4, 20, 21, 22, 37, 23, 24, 25, 26, 27, 11, 12, 13, 28, 29, 14, 30, 31, 45, 46, 47, 62, 63, 78, 79, 94, 95, 110, 126, 142, 158, 173, 174, 175, 189, 190, 191, 206, 207, 223, 35, 36, 50, 51, 52, 48, 49, 64, 65, 80, 66, 81, 82, 97, 98, 67, 68, 83, 84, 99, 113, 114, 129, 130, 145, 100, 115, 116, 131, 132, 146, 147, 148, 163, 164, 160, 161, 162, 176, 177, 178, 179, 193, 194, 195, 165, 180, 181, 196, 197, 182, 183, 198, 199, 215, 184, 185, 200, 201, 216, 186, 187, 188, 202, 203, 204, 205, 220, 221, 222, 217, 218, 232, 233, 234, 219, 235, 236, 251, 252, 237, 238, 239, 253, 254, 192, 208, 209, 224, 225, 210, 226, 241, 242, 243, 211, 212, 227, 228, 244, 213, 214, 229, 230, 231};
    static int[] g_Mask2 = new int[]{3, 2, 20, 15, 27, 14, 27, 20, 25, 9, 16, 6, 21, 5, 11, 11, 7, 13, 26, 6, 23, 24, 9, 9, 2, 30, 1, 5, 0, 10, 14};
    static int[] g_Mask1 = new int[]{0, 12, 21, 3, 11, 12, 16, 30, 21, 22, 18};

    public Decode() {
    }

    public static decode_20x20_data_Result decode_20x20_data(byte[] pData) {
        byte lLen = 0;
        byte[] szTmp = new byte[20];
        byte[] tmp = new byte[400];
        VL_S A = new VL_S();
        byte[] ucTmp = new byte[8];
        decode_20x20_data_Result result = new decode_20x20_data_Result();
        if(!g_lInit && ubar_init() == 0) {
            g_lInit = true;
        }

        if(g_lInit) {
            tmp = getByteByLength(pData, 0, 400);
            ubar_decode_Result udR = ubar_decode(szTmp, tmp);
            int lLen1;
            if(tagErrorCode.UBAR_OK.getValue() == udR.ec.getValue()) {
                szTmp = udR.temp;
                rsa_get_numb(szTmp, 10, A);
                lLen1 = rsa_put_uchar(ucTmp, lLen, 400L, A);
                System.arraycopy(ucTmp, 0, result.pOut, 7 - lLen1, lLen1);
                result.Result = 0;
                return result;
            } else {
                tmp = RotateBarCode(tmp, 90); //��ת90��
                udR = ubar_decode(szTmp, tmp);
                if(tagErrorCode.UBAR_OK.getValue() == udR.ec.getValue()) {
                    szTmp = udR.temp;
                    rsa_get_numb(szTmp, 10, A);
                    lLen1 = rsa_put_uchar(ucTmp, lLen, 400L, A);
                    System.arraycopy(ucTmp, 0, result.pOut, 7 - lLen1, lLen1);
                    result.Result = 0;
                    return result;
                } else {
                    tmp = RotateBarCode(tmp, 90); //��ת180��
                    udR = ubar_decode(szTmp, tmp);
                    if(tagErrorCode.UBAR_OK.getValue() == udR.ec.getValue()) {
                        szTmp = udR.temp;
                        rsa_get_numb(szTmp, 10, A);
                        lLen1 = rsa_put_uchar(ucTmp, lLen, 400L, A);
                        System.arraycopy(ucTmp, 0, result.pOut, 7 - lLen1, lLen1);
                        result.Result = 0;
                        return result;
                    } else {
                        tmp = RotateBarCode(tmp, 90); //��ת270��
                        udR = ubar_decode(szTmp, tmp);
                        if(tagErrorCode.UBAR_OK.getValue() == udR.ec.getValue()) {
                            szTmp = udR.temp;
                            rsa_get_numb(szTmp, 10, A);
                            lLen1 = rsa_put_uchar(ucTmp, lLen, 400L, A);
                            System.arraycopy(ucTmp, 0, result.pOut, 7 - lLen1, lLen1);
                            result.Result = 0;
                            return result;
                        } else {
                            result.Result = -2; //����ʧ��!
                            return result;
                        }
                    }
                }
            }
        } else {
            result.Result = -3;
            return result;
        }
    }

    public static decode_16x16_data_Result decode_16x16_data(byte[] pData) {
        byte lLen = 0;
        byte[] szTmp = new byte[20];
        byte[] tmp = new byte[256];
        VL_S A = new VL_S();
        byte[] ucTmp = new byte[8];
        decode_16x16_data_Result result = new decode_16x16_data_Result();
        if(!g_lInit && ubar_init() == 0) {
            g_lInit = true;
        }

        if(g_lInit) {
            tmp = getByteByLength(pData, 0, 256);
            ubar_decode_Result udR = ubar_decode(szTmp, tmp);
            int lLen1;
            if(tagErrorCode.UBAR_OK.getValue() == udR.ec.getValue()) {
                szTmp = udR.temp;
                rsa_get_numb(szTmp, 10, A);
                lLen1 = rsa_put_uchar(ucTmp, lLen, 256L, A);
                System.arraycopy(ucTmp, 0, result.pOut, 7 - lLen1, lLen1);
                result.Result = 0;
                return result;
            } else {
                tmp = RotateBarCode(tmp, 90); //��ת90��
                udR = ubar_decode(szTmp, tmp);
                if(tagErrorCode.UBAR_OK.getValue() == udR.ec.getValue()) {
                    szTmp = udR.temp;
                    rsa_get_numb(szTmp, 10, A);
                    lLen1 = rsa_put_uchar(ucTmp, lLen, 256L, A);
                    System.arraycopy(ucTmp, 0, result.pOut, 7 - lLen1, lLen1);
                    result.Result = 0;
                    return result;
                } else {
                    tmp = RotateBarCode(tmp, 90); //��ת180��
                    udR = ubar_decode(szTmp, tmp);
                    if(tagErrorCode.UBAR_OK.getValue() == udR.ec.getValue()) {
                        szTmp = udR.temp;
                        rsa_get_numb(szTmp, 10, A);
                        lLen1 = rsa_put_uchar(ucTmp, lLen, 256L, A);
                        System.arraycopy(ucTmp, 0, result.pOut, 7 - lLen1, lLen1);
                        result.Result = 0;
                        return result;
                    } else {
                        tmp = RotateBarCode(tmp, 90); //��ת270��
                        udR = ubar_decode(szTmp, tmp);
                        if(tagErrorCode.UBAR_OK.getValue() == udR.ec.getValue()) {
                            szTmp = udR.temp;
                            rsa_get_numb(szTmp, 10, A);
                            lLen1 = rsa_put_uchar(ucTmp, lLen, 256L, A);
                            System.arraycopy(ucTmp, 0, result.pOut, 7 - lLen1, lLen1);
                            result.Result = 0;
                            return result;
                        } else {
                            result.Result = -2; //����ʧ��!
                            return result;
                        }
                    }
                }
            }
        } else {
            result.Result = -3;
            return result;
        }
    }

    static byte[] RotateBarCode(byte[] szText, int lDirect) {
        byte[] szTemp = new byte[256];
        if(lDirect == 0) {
            return szText;
        } else {
            int j;
            int k;
            if(270 == lDirect) {
                for(j = 0; j < 16; ++j) {
                    for(k = 0; k < 16; ++k) {
                        szTemp[j * 16 + k] = szText[k * 16 + 15 - j];
                    }
                }

                return szTemp;
            } else if(90 == lDirect) {
                for(j = 0; j < 16; ++j) {
                    for(k = 0; k < 16; ++k) {
                        szTemp[j * 16 + k] = szText[(15 - k) * 16 + j];
                    }
                }

                return szTemp;
            } else if(180 != lDirect) {
                return szText;
            } else {
                for(j = 0; j < 16; ++j) {
                    for(k = 0; k < 16; ++k) {
                        szTemp[j * 16 + k] = szText[(15 - j) * 16 + 15 - k];
                    }
                }

                return szTemp;
            }
        }
    }

    private static byte[] getByteByLength(byte[] a, int StartMark, int Length) {
        int aLength = a.length;
        byte[] r = new byte[Length];
        if(aLength >= StartMark && aLength >= Length && aLength >= StartMark + Length) {
            boolean i = false;

            for(int var6 = 0; var6 < StartMark + Length; ++var6) {
                r[var6] = a[var6 + StartMark];
            }

            return r;
        } else {
            return r;
        }
    }

    static int ubar_init() {
        g_hRS = rs_init(5, 37, 1, 1, 20);
        return g_hRS == null?tagErrorCode.UBAR_ERR_MemoryNotEnough.getValue():tagErrorCode.UBAR_OK.getValue();
    }

    static RS_S rs_init(int symsize, int gfpoly, int fcr, int prim, int nroots) {
        RS_S rs = new RS_S();
        if(symsize > 16) {
            return null;
        } else if(fcr >= 1 << symsize) {
            return null;
        } else if(prim != 0 && prim < 1 << symsize) {
            if(nroots >= 1 << symsize) {
                return null;
            } else {
                rs.mm = symsize;
                rs.nn = (1 << symsize) - 1;
                rs.alpha_to = new byte[rs.nn + 1];
                if(rs.alpha_to == null) {
                    return null;
                } else {
                    rs.index_of = new byte[rs.nn + 1];
                    if(rs.index_of == null) {
                        return null;
                    } else {
                        rs.index_of[0] = (byte)rs.nn;
                        rs.alpha_to[rs.nn] = 0;
                        int sr = 1;

                        int i;
                        for(i = 0; i < rs.nn; ++i) {
                            rs.index_of[sr] = (byte)i;
                            rs.alpha_to[i] = (byte)sr;
                            sr <<= 1;
                            if((sr & 1 << symsize) > 0) {
                                sr ^= gfpoly;
                            }

                            sr &= rs.nn;
                        }

                        if(sr != 1) {
                            return null;
                        } else {
                            rs.genpoly = new byte[nroots + 1];
                            if(rs.genpoly == null) {
                                return null;
                            } else {
                                rs.fcr = (byte)fcr;
                                rs.prim = (byte)prim;
                                rs.nroots = nroots;

                                int iprim;
                                for(iprim = 1; iprim % prim != 0; iprim += rs.nn) {
                                    ;
                                }

                                rs.iprim = (byte)(iprim / prim);
                                rs.genpoly[0] = 1;
                                i = 0;

                                for(int root = fcr * prim; i < nroots; root += prim) {
                                    rs.genpoly[i + 1] = 1;

                                    for(int j = i; j > 0; --j) {
                                        if(rs.genpoly[j] != 0) {
                                            rs.genpoly[j] = (byte)(rs.genpoly[j - 1] ^ rs.alpha_to[modnn(rs, rs.index_of[rs.genpoly[j]] + root)]);
                                        } else {
                                            rs.genpoly[j] = rs.genpoly[j - 1];
                                        }
                                    }

                                    rs.genpoly[0] = rs.alpha_to[modnn(rs, rs.index_of[rs.genpoly[0]] + root)];
                                    ++i;
                                }

                                for(i = 0; i <= nroots; ++i) {
                                    rs.genpoly[i] = rs.index_of[rs.genpoly[i]];
                                }

                                return rs;
                            }
                        }
                    }
                }
            }
        } else {
            return null;
        }
    }

    static ubar_decode_Result ubar_decode(byte[] szText, byte[] szCode) {
        ubar_decode_Result udR = new ubar_decode_Result();
        String E = "10001";
        byte[] chMask = new byte[]{(byte)16, (byte)8, (byte)4, (byte)2, (byte)1};
        VL_S n = new VL_S();
        VL_S e = new VL_S();
        VL_S m = new VL_S();
        VL_S x = new VL_S();
        int[] lErrLoc = new int[32];
        byte[] uchBuff = new byte[32];
        byte[] szTemp = new byte[20];

        int j;
        for(j = 0; j < 155; ++j) {
            if(szCode[g_DecodeTable[j]] == 1) {
                uchBuff[j / 5] |= chMask[j % 5];
            }
        }

        for(j = 0; j < 31; ++j) {
            uchBuff[j] = (byte)(uchBuff[j] ^ g_Mask2[j]);
        }

        int lErr = rs_decode((RS_S)g_hRS, uchBuff, lErrLoc, 0);
        if(lErr >= 0 && lErr <= 11) {
            for(j = 0; j < 11; ++j) {
                uchBuff[j] = (byte)(uchBuff[j] ^ g_Mask1[j]);
            }

            rsa_get_numb(N.getBytes(), 16, n);
            rsa_get_numb(E.getBytes(), 16, e);
            rsa_get_uchar(uchBuff, 11, 32, m);
            rsa_trans(m, e, n, x);
            rsa_put_numb(szTemp, 10, x);
            szText = new byte[16];
            udR.ec = tagErrorCode.UBAR_OK;
            udR.temp = szTemp;
            return udR;
        } else {
            udR.ec = tagErrorCode.UBAR_ERR_RS;
            return udR;
        }
    }

    static void rsa_get_numb(byte[] str, int system, VL_S pN) {
        int len = str.length;
        SetZero(pN);

        for(int i = 0; i < len && str[i] != 0; ++i) {
            MulVL(pN, (long)system, pN);
            int k;
            if(str[i] >= 48 && str[i] <= 57) {
                k = str[i] - 48;
            } else if(str[i] >= 65 && str[i] <= 70) {
                k = str[i] - 55;
            } else if(str[i] >= 97 && str[i] <= 102) {
                k = str[i] - 87;
            } else {
                k = 0;
            }

            AddVL(pN, (long)k, pN);
        }

    }

    static void rsa_get_uchar(byte[] str, int len, int system, VL_S pN) {
        SetZero(pN);

        for(int i = 0; i < len; ++i) {
            MulVL(pN, (long)system, pN);
            int k = str[i] % system;
            AddVL(pN, (long)k, pN);
        }

    }

    static int rsa_put_uchar(byte[] str, int lLen, long system, VL_S pN) {
        VL_S X = new VL_S();
        byte[] pTemp = str;
        if(pN.s_ulSize == 1 && pN.s_ulValue[0] == 0L) {
            byte var12 = 1;
            str[0] = 0;
            return var12;
        } else {
            Mov(X, pN);
            int i = 0;

            while(X.s_ulValue[X.s_ulSize - 1] > 0L) {
                long a = ModVL(X, system);
                pTemp[i] = (byte)((int)(a & 255L));
                ++i;
                DivVL(X, system, X);
            }

            pTemp[i] = 0;
            int j = i;
            lLen = i;

            for(i = 0; i < j / 2; ++i) {
                byte b = str[i];
                str[i] = str[j - 1 - i];
                str[j - 1 - i] = b;
            }

            return lLen;
        }
    }

    static void rsa_trans(VL_S pN, VL_S pA, VL_S pB, VL_S pX) {
        VL_S X = new VL_S();
        VL_S Y = new VL_S();
        VL_S Temp = new VL_S();
        long k = (long)(pA.s_ulSize * 32 - 32);

        for(long num = pA.s_ulValue[pA.s_ulSize - 1]; num > 0L; ++k) {
            num >>= 1;
        }

        SetZero(Temp);
        SetZero(Y);
        Mov(X, pN);

        for(long i = k - 2L; i >= 0L; --i) {
            MulVL(X, X.s_ulValue[X.s_ulSize - 1], Y);
            Mod(Y, pB, Y);

            long j;
            long n;
            for(n = 1L; n < (long)X.s_ulSize; ++n) {
                for(j = (long)Y.s_ulSize; j > 0L; --j) {
                    Y.s_ulValue[(int)j] = Y.s_ulValue[(int)(j - 1L)];
                }

                Y.s_ulValue[0] = 0L;
                ++Y.s_ulSize;
                MulVL(X, X.s_ulValue[(int)((long)X.s_ulSize - n - 1L)], Temp);
                Add(Y, Temp, Y);
                Mod(Y, pB, Y);
            }

            Mov(X, Y);
            if((pA.s_ulValue[(int)(i >> 5)] >> (int)(i & 31L) & 1L) > 0L) {
                MulVL(pN, X.s_ulValue[X.s_ulSize - 1], Y);
                Mod(Y, pB, Y);

                for(n = 1L; n < (long)X.s_ulSize; ++n) {
                    for(j = (long)Y.s_ulSize; j > 0L; --j) {
                        Y.s_ulValue[(int)j] = Y.s_ulValue[(int)(j - 1L)];
                    }

                    Y.s_ulValue[0] = 0L;
                    ++Y.s_ulSize;
                    MulVL(pN, X.s_ulValue[(int)((long)X.s_ulSize - n - 1L)], Temp);
                    Add(Y, Temp, Y);
                    Mod(Y, pB, Y);
                }

                Mov(X, Y);
            }
        }

        Mov(pX, X);
    }

    static void rsa_put_numb(byte[] str, int system, VL_S pN) {
        VL_S X = new VL_S();
        byte[] t = new byte[]{(byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)65, (byte)66, (byte)67, (byte)68, (byte)69, (byte)70};
        int k = 0;
        if(pN.s_ulSize == 1 && pN.s_ulValue[0] == 0L) {
            str[0] = 48;
        } else {
            Mov(X, pN);

            while(X.s_ulValue[X.s_ulSize - 1] > 0L) {
                long a = ModVL(X, (long)system);
                str[k] = t[(int)a];
                ++k;
                DivVL(X, (long)system, X);
            }

            str[k] = 0;
            boolean var11 = false;
            int j = 0;

            int i;
            for(i = 0; i < str.length; ++i) {
                if(str[i] != 0) {
                    ++j;
                }
            }

            for(i = 0; i < j / 2; ++i) {
                byte b = str[i];
                str[i] = str[j - 1 - i];
                str[j - 1 - i] = b;
            }

        }
    }

    static void Mod(VL_S pB, VL_S pA, VL_S pMode) {
        VL_S X = new VL_S();
        VL_S Y = new VL_S();
        new BigInteger("0");
        new BigInteger("0");
        SetZero(Y);
        Mov(X, pB);

        for(; Cmp(X, pA) >= 0L; Sub(X, Y, X)) {
            BigInteger div = BigInteger.valueOf(X.s_ulValue[X.s_ulSize - 1]);
            BigInteger num = BigInteger.valueOf(pA.s_ulValue[pA.s_ulSize - 1]);
            int len = X.s_ulSize - pA.s_ulSize;
            if(div.equals(num) && len == 0) {
                Sub(X, pA, X);
                break;
            }

            if(div.compareTo(num) <= 0 && len > 0) {
                --len;
                div = div.shiftLeft(32).add(BigInteger.valueOf(X.s_ulValue[X.s_ulSize - 2]));
            }

            div = div.divide(num.add(BigInteger.valueOf(1L)));
            MovVL64(Y, div);
            Mul(Y, pA, Y);
            if(len > 0) {
                Y.s_ulSize += len;

                int i;
                for(i = Y.s_ulSize - 1; i >= len; --i) {
                    Y.s_ulValue[i] = Y.s_ulValue[i - len];
                }

                for(i = 0; i < len; ++i) {
                    Y.s_ulValue[i] = 0L;
                }
            }
        }

        Mov(pMode, X);
    }

    public static void SetZero(VL_S pV) {
        pV.s_ulSize = 1;

        for(int i = 0; i < 40; ++i) {
            pV.s_ulValue[i] = 0L;
        }

    }

    public static void MulVL(VL_S pB, long A, VL_S pProd) {
        VL_S X = new VL_S();
        new BigInteger("0");
        long carry = 0L;
        Mov(X, pB);

        for(int i = 0; i < pB.s_ulSize; ++i) {
            BigInteger mul = BigInteger.valueOf(pB.s_ulValue[i]);
            mul = mul.multiply(BigInteger.valueOf(A)).add(BigInteger.valueOf(carry));
            X.s_ulValue[i] = mul.mod(BigInteger.valueOf(4294967296L)).longValue();
            carry = mul.shiftRight(32).mod(BigInteger.valueOf(4294967296L)).longValue();
        }

        if(carry > 0L) {
            ++X.s_ulSize;
            X.s_ulValue[X.s_ulSize - 1] = carry;
        }

        Mov(pProd, X);
    }

    public static void Mov(VL_S pB, VL_S pA) {
        SetZero(pB);
        pB.s_ulSize = pA.s_ulSize;

        for(int i = 0; i < pA.s_ulSize; ++i) {
            pB.s_ulValue[i] = pA.s_ulValue[i];
        }

    }

    public static void AddVL(VL_S pB, long A, VL_S pSum) {
        VL_S C = new VL_S();
        MovVL(C, A);
        Add(pB, C, pSum);
    }

    public static void Add(VL_S pB, VL_S pA, VL_S pSum) {
        VL_S Temp = new VL_S();
        long carry = 0L;
        new BigInteger("0");
        Mov(Temp, pB);
        if(Temp.s_ulSize < pA.s_ulSize) {
            Temp.s_ulSize = pA.s_ulSize;
        }

        for(int i = 0; i < Temp.s_ulSize; ++i) {
            BigInteger sum = new BigInteger(String.valueOf(pA.s_ulValue[i]));
            sum = sum.add(new BigInteger(String.valueOf(pB.s_ulValue[i] + carry)));
            Temp.s_ulValue[i] = sum.mod(BigInteger.valueOf(4294967296L)).longValue();
            carry = sum.shiftRight(32).mod(BigInteger.valueOf(4294967296L)).longValue();
        }

        Temp.s_ulValue[Temp.s_ulSize] = carry;
        Temp.s_ulSize = (int)((long)Temp.s_ulSize + carry);
        Mov(pSum, Temp);
    }

    public static void MovVL(VL_S pA, long A) {
        SetZero(pA);
        pA.s_ulValue[0] = A;
    }

    static long Cmp(VL_S pB, VL_S pA) {
        if(pB.s_ulSize > pA.s_ulSize) {
            return 1L;
        } else if(pB.s_ulSize < pA.s_ulSize) {
            return -1L;
        } else {
            for(int j = pB.s_ulSize - 1; j >= 0; --j) {
                if(pB.s_ulValue[j] > pA.s_ulValue[j]) {
                    return 1L;
                }

                if(pB.s_ulValue[j] < pA.s_ulValue[j]) {
                    return -1L;
                }
            }

            return 0L;
        }
    }

    static void MovVL64(VL_S pA, BigInteger A) {
        SetZero(pA);
        if(A.compareTo(BigInteger.valueOf(4294967295L)) > 0) {
            pA.s_ulSize = 2;
            pA.s_ulValue[1] = A.shiftRight(32).mod(BigInteger.valueOf(4294967296L)).longValue();
            pA.s_ulValue[0] = A.mod(BigInteger.valueOf(4294967296L)).longValue();
        } else {
            pA.s_ulSize = 1;
            pA.s_ulValue[0] = A.mod(BigInteger.valueOf(4294967296L)).longValue();
        }

    }

    static void Sub(VL_S pB, VL_S pA, VL_S pDiff) {
        VL_S Temp = new VL_S();
        new BigInteger("0");
        if(Cmp(pB, pA) <= 0L) {
            SetZero(pDiff);
        } else {
            Mov(Temp, pB);
            long carry = 0L;

            for(int i = 0; i < Temp.s_ulSize; ++i) {
                if(Temp.s_ulValue[i] <= pA.s_ulValue[i] && (Temp.s_ulValue[i] != pA.s_ulValue[i] || carry != 0L)) {
                    BigInteger num = BigInteger.valueOf(4294967296L + Temp.s_ulValue[i]);
                    Temp.s_ulValue[i] = num.subtract(BigInteger.valueOf(carry + pA.s_ulValue[i])).mod(BigInteger.valueOf(4294967296L)).longValue();
                    carry = 1L;
                } else {
                    Temp.s_ulValue[i] = Temp.s_ulValue[i] - carry - pA.s_ulValue[i];
                    carry = 0L;
                }
            }

            while(Temp.s_ulValue[Temp.s_ulSize - 1] == 0L && Temp.s_ulSize > 0) {
                --Temp.s_ulSize;
            }

            Mov(pDiff, Temp);
        }
    }

    static void Mul(VL_S pB, VL_S pA, VL_S pProd) {
        VL_S X = new VL_S();
        if(pA.s_ulSize == 1) {
            MulVL(pB, pA.s_ulValue[0], pProd);
        } else {
            new BigInteger("0");
            long carry = 0L;
            SetZero(X);
            X.s_ulSize = pB.s_ulSize + pA.s_ulSize - 1;

            for(int i = 0; i < X.s_ulSize; ++i) {
                long sum = carry;
                carry = 0L;

                for(int j = 0; j < pA.s_ulSize; ++j) {
                    if(i - j >= 0 && i - j < pB.s_ulSize) {
                        BigInteger mul = new BigInteger(String.valueOf(pB.s_ulValue[i - j]));
                        mul = new BigInteger(mul.multiply(BigInteger.valueOf(pA.s_ulValue[j])).toString());
                        carry += mul.shiftRight(32).longValue();
                        mul = mul.mod(BigInteger.valueOf(4294967296L));
                        sum += mul.longValue();
                    }
                }

                carry += sum >> 32;
                X.s_ulValue[i] = sum & 4294967295L;
            }

            if(carry > 0L) {
                ++X.s_ulSize;
                X.s_ulValue[X.s_ulSize - 1] = carry & 4294967295L;
            }

            Mov(pProd, X);
        }
    }

    static void DivVL(VL_S pB, long A, VL_S pQuot) {
        VL_S X = new VL_S();
        long carry = 0L;
        Mov(X, pB);
        if(X.s_ulSize == 1) {
            X.s_ulValue[0] /= A;
            Mov(pQuot, X);
        } else {
            for(int i = X.s_ulSize - 1; i >= 0; --i) {
                BigInteger div = BigInteger.valueOf(carry);
                div = div.shiftLeft(32).add(BigInteger.valueOf(X.s_ulValue[i]));
                X.s_ulValue[i] = div.divide(BigInteger.valueOf(A)).mod(BigInteger.valueOf(4294967296L)).longValue();
                BigInteger mul = div.divide(BigInteger.valueOf(A)).multiply(BigInteger.valueOf(A));
                carry = div.subtract(mul).mod(BigInteger.valueOf(4294967296L)).longValue();
            }

            if(X.s_ulValue[X.s_ulSize - 1] == 0L) {
                --X.s_ulSize;
            }

            Mov(pQuot, X);
        }
    }

    public static int rs_decode(RS_S rs, byte[] data, int[] eras_pos, int no_eras) {
        byte[] lambda = new byte[rs.nroots + 1];
        byte[] s = new byte[rs.nroots];
        byte[] b = new byte[rs.nroots + 1];
        byte[] t = new byte[rs.nroots + 1];
        byte[] omega = new byte[rs.nroots + 1];
        byte[] root = new byte[rs.nroots];
        byte[] reg = new byte[rs.nroots + 1];
        byte[] loc = new byte[rs.nroots];

        int i;
        for(i = 0; i < rs.nroots; ++i) {
            s[i] = data[0];
        }

        int j;
        for(j = 1; j < rs.nn; ++j) {
            for(i = 0; i < rs.nroots; ++i) {
                if(s[i] == 0) {
                    s[i] = data[j];
                } else {
                    s[i] = (byte)(data[j] ^ rs.alpha_to[modnn(rs, rs.index_of[s[i]] + (rs.fcr + i) * rs.prim)]);
                }
            }
        }

        int syn_error = 0;

        for(i = 0; i < rs.nroots; ++i) {
            syn_error |= s[i];
            s[i] = rs.index_of[s[i]];
        }

        if(syn_error == 0) {
            byte var29 = 0;
            if(eras_pos != null) {
                for(i = 0; i < var29; ++i) {
                    eras_pos[i] = loc[i];
                }
            }

            return var29;
        } else {
            for(i = 1; i < rs.nroots; ++i) {
                lambda[i] = 0;
            }

            lambda[0] = 1;
            byte tmp;
            if(no_eras > 0) {
                lambda[1] = rs.alpha_to[modnn(rs, rs.prim * (rs.nn - 1 - eras_pos[0]))];

                for(i = 1; i < no_eras; ++i) {
                    byte u = (byte)modnn(rs, rs.prim * (rs.nn - 1 - eras_pos[i]));

                    for(j = i + 1; j > 0; --j) {
                        tmp = rs.index_of[lambda[j - 1]];
                        if(tmp != rs.nn) {
                            lambda[j] ^= rs.alpha_to[modnn(rs, u + tmp)];
                        }
                    }
                }
            }

            for(i = 0; i < rs.nroots + 1; ++i) {
                b[i] = rs.index_of[lambda[i]];
            }

            int r = no_eras;
            int el = no_eras;

            while(true) {
                while(true) {
                    ++r;
                    if(r > rs.nroots) {
                        int deg_lambda = 0;

                        for(i = 0; i < rs.nroots + 1; ++i) {
                            lambda[i] = rs.index_of[lambda[i]];
                            if(lambda[i] != rs.nn) {
                                deg_lambda = i;
                            }
                        }

                        for(i = 1; i < rs.nroots + 1; ++i) {
                            reg[i] = lambda[i];
                        }

                        int count = 0;
                        i = 1;

                        for(int k = rs.iprim - 1; i <= rs.nn; k = modnn(rs, k + rs.iprim)) {
                            byte q = 1;

                            for(j = deg_lambda; j > 0; --j) {
                                if(reg[j] != rs.nn) {
                                    reg[j] = (byte)modnn(rs, reg[j] + j);
                                    q ^= rs.alpha_to[reg[j]];
                                }
                            }

                            if(q == 0) {
                                root[count] = (byte)i;
                                loc[count] = (byte)k;
                                ++count;
                                if(count == deg_lambda) {
                                    break;
                                }
                            }

                            ++i;
                        }

                        byte var28;
                        if(deg_lambda != count) {
                            var28 = -1;
                            if(eras_pos != null) {
                                for(i = 0; i < var28; ++i) {
                                    eras_pos[i] = loc[i];
                                }
                            }

                            return var28;
                        }

                        int deg_omega = 0;

                        for(i = 0; i < rs.nroots; ++i) {
                            tmp = 0;

                            for(j = deg_lambda < i?deg_lambda:i; j >= 0; --j) {
                                if(s[i - j] != rs.nn && lambda[j] != rs.nn) {
                                    tmp ^= rs.alpha_to[modnn(rs, s[i - j] + lambda[j])];
                                }
                            }

                            if(tmp != 0) {
                                deg_omega = i;
                            }

                            omega[i] = rs.index_of[tmp];
                        }

                        omega[rs.nroots] = (byte)rs.nn;

                        for(j = count - 1; j >= 0; --j) {
                            byte num1 = 0;

                            for(i = deg_omega; i >= 0; --i) {
                                if(omega[i] != rs.nn) {
                                    num1 ^= rs.alpha_to[modnn(rs, omega[i] + i * root[j])];
                                }
                            }

                            byte num2 = rs.alpha_to[modnn(rs, root[j] * (rs.fcr - 1) + rs.nn)];
                            byte den = 0;

                            for(i = min(deg_lambda, rs.nroots - 1) & -2; i >= 0; i -= 2) {
                                if(lambda[i + 1] != rs.nn) {
                                    den ^= rs.alpha_to[modnn(rs, lambda[i + 1] + i * root[j])];
                                }
                            }

                            if(den == 0) {
                                var28 = -1;
                                if(eras_pos != null) {
                                    for(i = 0; i < var28; ++i) {
                                        eras_pos[i] = loc[i];
                                    }
                                }

                                return var28;
                            }

                            if(num1 != 0) {
                                data[loc[j]] ^= rs.alpha_to[modnn(rs, rs.index_of[num1] + rs.index_of[num2] + rs.nn - rs.index_of[den])];
                            }
                        }

                        if(eras_pos != null) {
                            for(i = 0; i < count; ++i) {
                                eras_pos[i] = loc[i];
                            }
                        }

                        return count;
                    }

                    byte discr_r = 0;

                    for(i = 0; i < r; ++i) {
                        if(lambda[i] != 0 && s[r - i - 1] != rs.nn) {
                            discr_r ^= rs.alpha_to[modnn(rs, rs.index_of[lambda[i]] + s[r - i - 1])];
                        }
                    }

                    discr_r = rs.index_of[discr_r];
                    if(discr_r == rs.nn) {
                        for(i = rs.nroots; i > 0; --i) {
                            b[i] = b[i - 1];
                        }

                        b[0] = (byte)rs.nn;
                    } else {
                        t[0] = lambda[0];

                        for(i = 0; i < rs.nroots; ++i) {
                            if(b[i] != rs.nn) {
                                t[i + 1] = (byte)(lambda[i + 1] ^ rs.alpha_to[modnn(rs, discr_r + b[i])]);
                            } else {
                                t[i + 1] = lambda[i + 1];
                            }
                        }

                        if(2 * el <= r + no_eras - 1) {
                            el = r + no_eras - el;

                            for(i = 0; i <= rs.nroots; ++i) {
                                if(lambda[i] == 0) {
                                    b[i] = (byte)rs.nn;
                                } else {
                                    b[i] = (byte)modnn(rs, rs.index_of[lambda[i]] - discr_r + rs.nn);
                                }
                            }
                        } else {
                            for(i = rs.nroots; i > 0; --i) {
                                b[i] = b[i - 1];
                            }

                            b[0] = (byte)rs.nn;
                        }

                        for(i = 0; i < rs.nroots + 1; ++i) {
                            lambda[i] = t[i];
                        }
                    }
                }
            }
        }
    }

    private static int min(int deg_lambda, int l) {
        return deg_lambda < l?deg_lambda:l;
    }

    static int modnn(RS_S rs, int x) {
        while(x >= rs.nn) {
            x -= rs.nn;
            x = (x >> rs.mm) + (x & rs.nn);
        }

        return x;
    }

    static long ModVL(VL_S pB, long A) {
        long carry = 0L;
        if(pB.s_ulSize == 1) {
            return pB.s_ulValue[0] % A;
        } else {
            for(int i = pB.s_ulSize - 1; i >= 0; --i) {
                BigInteger div = BigInteger.valueOf(pB.s_ulValue[i]);
                div = div.add(BigInteger.valueOf(carry * 4294967296L));
                carry = div.mod(BigInteger.valueOf(A)).mod(BigInteger.valueOf(4294967296L)).longValue();
            }

            return carry;
        }
    }
}
