package com.kola.otdr.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * CRC数组处理工具类及数组合并
 * @author fusion group
 */

public class CrcUtil {

    /**
     * 为Byte数组最后添加两位CRC校验
     *
     * @param buf（验证的byte数组）
     * @return
     */
    public static byte[] setParamCRC(byte[] buf) {
        int checkCode = 0;
        checkCode = crc_16_CCITT_False(buf, buf.length);
        byte[] crcByte = new byte[2];
        crcByte[0] = (byte) ((checkCode >> 8) & 0xff);
        crcByte[1] = (byte) (checkCode & 0xff);
        // 将新生成的byte数组添加到原数据结尾并返回
        return concatAll(buf, crcByte);
    }

    /**
     * CRC-16/CCITT-FALSE x16+x12+x5+1 算法
     * 与CRC16 crc16实现相同
     * info
     * Name:CRC-16/CCITT-FAI
     * Width:16
     * Poly:0x1021
     * Init:0xFFFF
     * RefIn:False
     * RefOut:False
     * XorOut:0x0000
     *
     * @param bytes
     * @param length
     * @return
     */
    public static int crc_16_CCITT_False(byte[] bytes, int length) {
        int crc = 0xffff; // initial value
        int polynomial = 0x1021; // poly value
        for (int index = 0; index < bytes.length; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        //输出String字样的16进制
        String strCrc = Integer.toHexString(crc).toUpperCase();
        System.out.println(strCrc);
        return crc;
    }

    /***
     * CRC校验是否通过
     *
     * @param srcByte
     * @param length(验证码字节长度)
     * @return
     */
    public static boolean isPassCRC(byte[] srcByte, int length) {

        // 取出除crc校验位的其他数组，进行计算，得到CRC校验结果
        int calcCRC = calcCRC(srcByte, 0, srcByte.length - length);
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((calcCRC >> 8) & 0xff);
        bytes[1] = (byte) (calcCRC & 0xff);

        // 取出CRC校验位，进行计算
        int i = srcByte.length;
        byte[] b = { srcByte[i - 2] ,srcByte[i - 1] };

        // 比较
        return bytes[0] == b[0] && bytes[1] == b[1];
    }

    /**
     * 对buf中offset以前crcLen长度的字节作crc校验，返回校验结果
     * @param  buf
     * @param crcLen
     */
    private static int calcCRC(byte[] buf, int offset, int crcLen) {
        int start = offset;
        int end = offset + crcLen;
        int crc = 0xffff; // initial value
        int polynomial = 0x1021;
        for (int index = start; index < end; index++) {
            byte b = buf[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        return crc;
    }

    /**
     * 多个数组合并
     *
     * @param first
     * @param rest
     * @return
     */
    public static byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    /**
     * CRC-16/CCITT-FALSE x16+x12+x5+1 算法
     *
     * info
     * Name:CRC-16/CCITT-FAI
     * Width:16
     * Poly:0x1021
     * Init:0xFFFF
     * RefIn:False
     * RefOut:False
     * XorOut:0x0000
     *
     * @param hexString
     * @param
     * @return
     */
    public static String crc_16_CCITT_False(String hexString) {


        byte[] destByte = new byte[hexString.length()/2];
        int j=0;
        for(int i=0;i<destByte.length;i++) {

            byte high = (byte) (Character.digit(hexString.charAt(i), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(j + 1), 16) & 0xff);
            destByte[i] = (byte) (high << 4 | low);
            j+=2;
        }
        int crc = 0xffff; // initial value
        int polynomial = 0x1021; // poly value
        for (int index = 0; index < destByte.length; index++) {

            byte b = destByte[index];
            for (int i = 0; i < 8; i++) {

                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        String strCrc = Integer.toHexString(crc).toUpperCase();
        System.out.println("crc:"+crc);
        return strCrc;
    }


    //测试数据
    static byte[] data = {(byte)0xAA,0x0C,0x01,0x00,0x01,0x00,0x00,0x04,0x05,0x17,0x05,0x01,(byte)0xA0,(byte)0x86,0x01,0x00};
    //AA 0C 01 00 01 00 00 04 05 17 05 01 A0 86 01 00
    //结果为：F2E3
    public static void main(String[] s){
        byte[] crcData = CrcUtil.setParamCRC(data);
        if(CrcUtil.isPassCRC(crcData, 2)){
            System.out.println("验证通过");
        }else{
            System.out.println("验证失败");
        }

        String hexString = "043b35d468c4c534d4ea00000000008900000000000000000000edcd88962500007d000078d000f400c77c00ea000000c20001007700ba0277009a02000000000000ca1508b108ad0dad08af0dad08af08ae0fd90fe70d290a6c080808000800080808080bfd0bee0bef0f7b0f7207ff08010802080007ff08010eaf00000000000000003bab92c00a3b64c9433db2b8843c3ae4c28d3d0efdca1e3ae4c28d3d0efdca1e003c000a000c08080008080802030302020204b0390e042b0000000000000000";
       System.out.println("CRC16:"+CRC16.crc16(hexString.getBytes()));
       System.out.println("crc_16_CCITT_False:"+crc_16_CCITT_False(hexString.getBytes(),2));
        System.out.println(crc_16_CCITT_False(hexString));//1792
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("date:" +new Date(1321951763) +"  || "+format.format(new Date(1321951763)));
        System.out.println("date:" +new Date(1560758481) +"  || "+format.format(new Date(1560758481*1000)));
//        3933334;2e-5；;
        BigDecimal a = new BigDecimal(3933334);
        BigDecimal b = new BigDecimal(2e-5);
        BigDecimal c = a.multiply(b);
        System.out.println(c.toString());

    }
}
