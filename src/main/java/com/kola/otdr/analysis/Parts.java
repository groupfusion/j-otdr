package com.kola.otdr.analysis;

/**
 *
 * 工具类
 * @author: fusionGroup
 * @date: 2022-03-30 15:28
 */
public class Parts {
    public static int LENGTH_SHORT = 2;
    public static int LENGTH_LONG = 4;

    public static String separator = ",";

    //光速
    public static double SOL = 0.299792458D;

    public static double refraction=1.00029;//默认值，防止计算错误

    public static double factor(){
        double factor = 1e-4 * Parts.SOL / refraction;
        return factor;
    }

    /**
     * 读取整型数
     * get_uint /get_signed
     * @param b      字节内容
     * @param offset 起始位置偏移量
     * @param length 整型数字节长度 {@link #LENGTH_SHORT,#LENGTH_LONG}
     * @return "小端" 整型数值
     */
    public static int readInt(byte[] b, int offset, int length) {
        byte[] bytes = new byte[LENGTH_LONG];
        System.arraycopy(b, offset, bytes, 0, length);
        return bytesToInt(bytes);
    }

    /**
     * byte[]转int
     * <p>由低位到高位</P>
     *
     * @param bytes 字节数组
     * @return 整型int
     */
    public static int bytesToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < bytes.length; i++) {
            int shift = i * 8;
            value += (bytes[i] & 0x000000FF) << shift;
        }
        return value;
    }

    /**
     * 读取以0为结束符的字符串
     *
     * @param b      字节内容
     * @param offset 起始位置偏移量
     * @return UTF-8字符串
     */
    public static String readStringSpaceZero(byte[] b, int offset) {
        int length = 0;
        while (length + offset <= b.length && b[length + offset] != 0) {
            length++;
        }
        return readString(b, offset, length);
    }

    /**
     * 读取字符串
     *
     * @param b      字节内容
     * @param offset 起始位置偏移量
     * @param length 读取长度
     * @return UTF-8字符串
     */
    public static String readString(byte[] b, int offset, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(b, offset, bytes, 0, length);
        return new String(bytes);
    }

    public static String readHex(byte[] b, int offset, int length){
        byte[] bytes = new byte[length];
        System.arraycopy(b, offset, bytes, 0, length);
        String hstr = "";
//        for(byte :bytes){
//
//        }

//        for i in range(nbytes):
//            b = "%02X " % ord(fh.read(1))
//            hstr += b
//        return hstr;
//
//        for(var i=0; i<nbytes; i++) {
//            var b = (await fh.readUInt8()).toString(16);
//            b = ("00000" + b).substr(-2).toUpperCase()+" ";
//            hstr += b;
//        }
        return "";

    }
}
